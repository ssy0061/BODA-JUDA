package com.dh.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dh.fragment.databinding.FragmentListBinding

class ListFragment : Fragment() {

    lateinit var binding:FragmentListBinding
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        // 내 fragment를 호출한 activity가 들어온다
        super.onAttach(context)

        if(context is MainActivity) mainActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            arguments?.apply {
                textTitle.text = getString("key1")
                textValue.text = "${getInt("key2")}"
            }
            btnNext.setOnClickListener {
                mainActivity.goDetail()
            }
        }
    }

    fun setValue(value:String){
        binding.textFromActivity.text = value
    }
}