package com.aeye.nextlabel.feature.main.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.aeye.nextlabel.R
import com.aeye.nextlabel.databinding.FragmentCameraBinding
import com.aeye.nextlabel.feature.main.ContainerViewModel
import com.aeye.nextlabel.feature.main.MainActivity
import com.aeye.nextlabel.global.BUNDLE_KEY_TO_MOVE
import com.aeye.nextlabel.global.REQUEST_KEY_TO_MOVE
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment: Fragment() {

    val binding by lazy { FragmentCameraBinding.inflate(layoutInflater) }
    val viewModel: ContainerViewModel by viewModels(ownerProducer = {requireParentFragment()})

    // TODO: activity를 MainActivity로 바로 사용하지 않게 수정
    lateinit var activity: MainActivity

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity = context as MainActivity

        if (!hasPermissions(activity)) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
        }

        outputDirectory = getOutputDirectory()
        binding.btnCamera.setOnClickListener { takePhoto() }
        cameraExecutor = Executors.newSingleThreadExecutor()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    private fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                startCamera()
            } else {
                activity.finish()
            }
        }
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.pvCamera.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().setTargetResolution(Size(3000, 3000)).build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch(exc: Exception) {
                Log.e(TAG, "Binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(activity))
    }

    /** Helper function used to create a timestamped file */
    private fun createFile(baseFolder: File, format: String, extension: String) =
        File(baseFolder, SimpleDateFormat(format, Locale.US)
            .format(System.currentTimeMillis()) + extension)

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(activity),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Image capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Image capture succeeded: ${output.savedUri}"
                    val savedUri = output.savedUri?: Uri.fromFile(photoFile)
                    Log.d(TAG, msg)

                    val mimeType = MimeTypeMap.getSingleton()
                        .getMimeTypeFromExtension(savedUri.toFile().extension)

                    MediaScannerConnection.scanFile(
                        context,
                        arrayOf(savedUri.toFile().absolutePath),
                        arrayOf(mimeType)
                    ) { _, uri ->
                        viewModel.imageSavedUri = uri
                        setFragmentResult(REQUEST_KEY_TO_MOVE, bundleOf(BUNDLE_KEY_TO_MOVE to "NEXT"))
                        Log.d(TAG, "Image capture scanned into media store: $uri")
                    }
                }
            }
        )
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity.externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else activity.filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private val TAG = "CameraFragment_debuk"
        private val FILENAME = "yyyy-MM-dd-HH-mm-ss"
        private const val PHOTO_EXTENSION = ".jpg"
        val PERMISSIONS_REQUEST_CODE = 10
        val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}