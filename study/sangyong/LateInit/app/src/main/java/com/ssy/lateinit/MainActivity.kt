package com.ssy.lateinit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    // <지연초기화>
    // 지연 초기화는 클래스에 사용
    // 클래스와 null safety에 대해 알아야 함
    var name2:String = "Scott"
    // name2이 특정조건에 의해 사용되지 않으면 자원 낭비
    // 지연초기화는 메모리 낭비를 막기 위함

    // 1. lateinit
    // var 변수명:타입
    lateinit var name:String // 기본형(Int, Long, Float, Double)에는 사용 불가

    lateinit var person:Person

    // 2. lazy
    // val 변수명 by lazy {변수에 들어갈 클래스 생성자 또는 값}
    val age by lazy { Person() }

    // lazy는 값을 입력해놓고 변경하지 않을 때
    // 값 변경이 필요하면 lateinit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        person = Person()

    }

    class Person {
        var name = ""
        var age = ""
        var address = ""
        var tel = ""
    }
}

