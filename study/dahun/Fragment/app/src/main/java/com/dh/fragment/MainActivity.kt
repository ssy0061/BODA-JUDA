package com.dh.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dh.fragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    val listFragment by lazy { ListFragment() }
    val detailFragment by lazy { DetailFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFragment()

        binding.btnSend.setOnClickListener {
            listFragment.setValue("값 전달해보자!!")
        }
    }

    fun goDetail(){
//        val detailFragment = DetailFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameLayout, detailFragment)
        // 지금 여기 전체 동작이 stack에 담긴다 따라서 이 트랜잭션을 뺄 수 있다
        transaction.addToBackStack("detail")
        transaction.commit()
    }

    fun goBack(){
        onBackPressed()
    }

    fun setFragment(){

        val bundle = Bundle()
        bundle.putString("key1", "List fragment")
        bundle.putInt("key2", 20220305)

        listFragment.arguments = bundle

        // 사용할 프레그먼트 생성
//        val listFragment = ListFragment()
        // 트랜잭션 생성
        val transaction = supportFragmentManager.beginTransaction()
        // 트랜잭션 통한 프레그먼트 삽입
        transaction.add(R.id.frameLayout, listFragment)
        transaction.commit()
    }
}