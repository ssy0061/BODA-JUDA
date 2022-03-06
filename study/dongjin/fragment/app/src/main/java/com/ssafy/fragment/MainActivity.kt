package com.ssafy.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.fragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val detailFragment by lazy {  DetailFragment()}

    val listFragment by lazy { ListFragment()}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFragment()

        binding.btnSend.setOnClickListener {
            listFragment.setValue("값 전달하기")
        }

    }

    fun goDetail(){


        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameLayout, detailFragment)
        transaction.addToBackStack("detail")
        transaction.commit()
    }

    fun goBack(){
        onBackPressed()
    }

    fun setFragment(){

        val bundle = Bundle()
        bundle.putString("key1","List Fragment")
        bundle.putInt("key2",20210331)

        listFragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        // 3. 트랜잭션을 통해 프래그먼트 삽입
        transaction.add(R.id.frameLayout, listFragment)
        transaction.commit()
    }
}