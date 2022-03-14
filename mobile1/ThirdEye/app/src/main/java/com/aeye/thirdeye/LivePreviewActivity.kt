/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aeye.thirdeye

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.aeye.thirdeye.objectdetector.ObjectDetectorProcessor
import com.aeye.thirdeye.preference.PreferenceUtils
import com.aeye.thirdeye.vibrator.TextToSpeechUtil
import com.google.android.gms.common.annotation.KeepName
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.mlkit.common.model.CustomRemoteModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.linkfirebase.FirebaseModelSource
import java.io.IOException
import java.util.ArrayList

/** Live preview demo for ML Kit APIs. */
@KeepName
class LivePreviewActivity :
    AppCompatActivity(), OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var selectedModel = OBJECT_DETECTION_CUSTOM

    lateinit var remoteConfig: FirebaseRemoteConfig
    // firebase monitoring
    private val firebasePerformance = FirebasePerformance.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_live_preview)

        preview = findViewById(R.id.preview_view)
        if (preview == null) {
            Log.d(TAG, "Preview is null")
        }

        graphicOverlay = findViewById(R.id.graphic_overlay)
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null")
        }

        createCameraSource(selectedModel)

        val detailButton = findViewById<Button>(R.id.button_live_preview_detail).apply { 
            setOnClickListener {
                // TODO: 상세 정보 음성 안내  
            }
        }
        
        val refreshButton = findViewById<Button>(R.id.button_live_preview_refresh).apply { 
            setOnClickListener {
                // TODO: 재인식 
            }
        }

        // object detection의 결과
        val result = findViewById<TextView>(R.id.tv_detection_result)

        val voiceButton = findViewById<Button>(R.id.button_live_preview_voice).apply { 
            setOnClickListener {
                TextToSpeechUtil(this@LivePreviewActivity, result.text.toString())
            }
        }

    }

    @Synchronized
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedModel = parent?.getItemAtPosition(pos).toString()
        Log.d(TAG, "Selected model: $selectedModel")
        preview?.stop()
        createCameraSource(selectedModel)
        startCameraSource()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Do nothing.
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        Log.d(TAG, "Set facing")
        if (cameraSource != null) {
            if (isChecked) {
                cameraSource?.setFacing(CameraSource.CAMERA_FACING_FRONT)
            } else {
                cameraSource?.setFacing(CameraSource.CAMERA_FACING_BACK)
            }
        }
        preview?.stop()
        startCameraSource()
    }

    private fun createCameraSource(model: String) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = CameraSource(this, graphicOverlay)
        }
        try {
            when (model) {
                OBJECT_DETECTION -> {
                    Log.i(TAG, "Using Object Detector Processor")
                    val objectDetectorOptions = PreferenceUtils.getObjectDetectorOptionsForLivePreview(this)
                    cameraSource!!.setMachineLearningFrameProcessor(
                        ObjectDetectorProcessor(this, objectDetectorOptions)
                    )
                }
                OBJECT_DETECTION_CUSTOM -> {
                    Log.i("확인", "Using Custom Object Detector Processor")
                    val localModel =
//            LocalModel.Builder().setAssetFilePath("custom_models/snack_01_410.tflite").build()
                        LocalModel.Builder().setAssetFilePath("custom_models/snack_02_17.tflite").build()

                    // remoteModel + firebase 원격 구성 추가
                    configureRemoteConfig()
                    remoteConfig.fetchAndActivate()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val modelName = remoteConfig.getString("model_name")
                                val downloadTrace = firebasePerformance.newTrace("download_model")
                                downloadTrace.start()
                                Log.d("확인", "modelName: $modelName")

                                // Load the remoteModel (기존 모델 하나만 다운로드하는 코드와 같음)
                                val remoteModel =
                                    CustomRemoteModel
                                        .Builder(FirebaseModelSource.Builder(modelName).build())
                                        .build()
                                Log.d("확인", "localModel: $localModel")
                                Log.d("확인", "remoteModel: $remoteModel")
                                val remoteModelManager = RemoteModelManager.getInstance()

                                // 모델 다운로드 확인 후 실행
                                remoteModelManager.isModelDownloaded(remoteModel)
                                    .addOnSuccessListener {
                                        Log.d("확인", "다운로드 확인: $it")
                                        // false이면 모델 다운로드
                                        if (!it) {
                                            val downloadConditions = DownloadConditions.Builder()
                                                .requireWifi()
                                                .build()
                                            remoteModelManager.download(remoteModel, downloadConditions)
                                                .addOnSuccessListener {
                                                    Log.d("확인", "다운로드 성공")
                                                    // 모델 적용하여 실행
                                                    startObjectDetectorWithRemoteModel(remoteModel)
                                                }
                                                .addOnFailureListener {
                                                    Log.d("확인", "다운로드 실패")
                                                }
                                        } else {
                                            // 모델 적용하여 실행
                                            startObjectDetectorWithRemoteModel(remoteModel)
                                        }
                                    }
                                    .addOnFailureListener {
                                        Log.d("확인", "다운로드 확인 불가")
                                    }
                            } else {
                                showToast("Failed to fetch model name.")
                            }
                        }

                }
                CUSTOM_AUTOML_OBJECT_DETECTION -> {
                    Log.i(TAG, "Using Custom AutoML Object Detector Processor")
                    val customAutoMLODTLocalModel =
                        LocalModel.Builder().setAssetManifestFilePath("automl/manifest.json").build()
                    val customAutoMLODTOptions =
                        PreferenceUtils.getCustomObjectDetectorOptionsForLivePreview(
                            this,
                            customAutoMLODTLocalModel
                        )
                    cameraSource!!.setMachineLearningFrameProcessor(
                        ObjectDetectorProcessor(this, customAutoMLODTOptions)
                    )
                }

                else -> Log.e(TAG, "Unknown model: $model")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Can not create image processor: $model", e)
            Toast.makeText(
                applicationContext,
                "Can not create image processor: " + e.message,
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    // 원격 구성을 통해 모델 선택하기
    // Firebase 원격 구성
    private fun configureRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            // 업데이트 간격을 너무 길게해서 변수 변경이 즉시 반영되지 않았던 문제
            // https://firebase.google.com/docs/remote-config/get-started?platform=android&hl=ko#throttling
            // 개발 도중에는 짧게해서 사용해도 문제 없지만 서비스 중에는 업데이트 시간 고려해야함
            minimumFetchIntervalInSeconds = 5
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    // 모델 적용하여 실행
    private fun startObjectDetectorWithRemoteModel(remoteModel: CustomRemoteModel) {
        val customObjectDetectorOptions =
            PreferenceUtils.getCustomObjectDetectorOptionsForLivePreviewWithRemoteModel(this, remoteModel)
        Log.d("확인", "옵션: ${customObjectDetectorOptions}")
        cameraSource!!.setMachineLearningFrameProcessor(
            ObjectDetectorProcessor(this, customObjectDetectorOptions)
        )
    }

    private fun showToast(text: String) {
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private fun startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null")
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null")
                }
                preview!!.start(cameraSource, graphicOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                cameraSource!!.release()
                cameraSource = null
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        createCameraSource(selectedModel)
        startCameraSource()
    }

    /** Stops the camera. */
    override fun onPause() {
        super.onPause()
        preview?.stop()
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (cameraSource != null) {
            cameraSource?.release()
        }
    }

    companion object {
        private const val OBJECT_DETECTION = "Object Detection"
        private const val OBJECT_DETECTION_CUSTOM = "Custom Object Detection"
        private const val CUSTOM_AUTOML_OBJECT_DETECTION = "Custom AutoML Object Detection (Flower)"
        private const val FACE_DETECTION = "Face Detection"
        private const val TEXT_RECOGNITION_LATIN = "Text Recognition Latin"
        private const val TEXT_RECOGNITION_CHINESE = "Text Recognition Chinese"
        private const val TEXT_RECOGNITION_DEVANAGARI = "Text Recognition Devanagari"
        private const val TEXT_RECOGNITION_JAPANESE = "Text Recognition Japanese"
        private const val TEXT_RECOGNITION_KOREAN = "Text Recognition Korean"
        private const val BARCODE_SCANNING = "Barcode Scanning"
        private const val IMAGE_LABELING = "Image Labeling"
        private const val IMAGE_LABELING_CUSTOM = "Custom Image Labeling (Birds)"
        private const val CUSTOM_AUTOML_LABELING = "Custom AutoML Image Labeling (Flower)"
        private const val POSE_DETECTION = "Pose Detection"
        private const val SELFIE_SEGMENTATION = "Selfie Segmentation"

        private const val TAG = "LivePreviewActivity"
    }
}
