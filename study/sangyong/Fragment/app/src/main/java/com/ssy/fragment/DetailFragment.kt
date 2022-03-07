package com.ssy.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssy.fragment.databinding.FragmentDetailBinding
import com.ssy.fragment.databinding.FragmentListBinding

class DetailFragment : Fragment() {

    lateinit var binding: FragmentDetailBinding
    lateinit var mainActivity: MainActivity

    // ctrl + o 누르고 타이핑하여 검색
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainActivity) mainActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            mainActivity.goBack()
        }
    }

    // onAttach로 메인activity에 붙는 순간
    // mainActivity는 context를 담아놓고

    // onCreateView가 되면
    // binding 생성하여 담아놓고 binding.root는 안드로이드한테 전달

    // viewCreated가 되면
    // 버튼에 리스너 달고 goback
}