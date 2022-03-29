package com.aeye.nextlabel.feature.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aeye.nextlabel.R
import com.aeye.nextlabel.databinding.FragmentLoginBinding
import com.aeye.nextlabel.feature.common.BaseFragment
import com.aeye.nextlabel.feature.main.MainActivity
import com.aeye.nextlabel.model.dto.UserForLogin
import com.aeye.nextlabel.util.InputValidUtil
import com.aeye.nextlabel.util.Status

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::bind, R.layout.fragment_login) {
    private val userViewModel: UserViewModel by activityViewModels()

    private val btnJoin = binding.textButtonJoin  // button join
    private val btnLogin = binding.containedButtonLogin  // button login: move to home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initLiveDataObserver()
    }

    private fun init() {
        btnJoin.setOnClickListener {
            // TODO: joinFragment로 이동 
        }

        btnLogin.setOnClickListener {
            if (checkInputForm()) {
                login()
            }
        }
    }
    
    private fun initLiveDataObserver() {
        // 로그인 요청 결과에 따른 ui 변경 
        userViewModel.loginRequestLiveData.observe(requireActivity()) {
            when(it.status) {
                Status.SUCCESS -> {
                    // TODO: dismissLoading() 
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                Status.LOADING -> {
                    // TODO: showLoading() 
                }
                Status.ERROR -> {
                    // TODO: dismissLoading() 
                }
            }
        }
    }

    private fun login() {
        val userId = binding.userId.text.toString()
        val password = binding.password.text.toString()

        userViewModel.login(UserForLogin(userId, password))
    }

    private fun checkInputForm(): Boolean {
        var result = 1

        val userId = binding.userId.text.toString()
        val password = binding.password.text.toString()

        if(!InputValidUtil.isValidUserId(userId)) {
            result *= 0
            binding.userId.error = resources.getText(R.string.userIdErrorMessage)
        }
        if(!InputValidUtil.isValidPassword(password)) {
            result *= 0
            binding.password.error = resources.getText(R.string.passwordErrorMessage)
        }

        return when(result) {
            1 -> true
            else -> false
        }
    }

}