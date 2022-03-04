package com.ssy.kotlinsyntaxclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var parent = Parent()
//        parent.showHouse()
//
//        var child = Child()
//        child.showHouse()
        var son = Son()
        var result = son.getNumber("가")
        Log.d("클래스", "overload=${result} ${son.getNumber()}")
    }
}

// 상속을 사용하는 이유
// 1. 기존의 작성된 코드를 재활용하기 위해서
// 2. 코드를 체계적인 구조로로 사용하기 위서

open class Parent {
    var money = 5000000000
    open var house = "강남 200평 아파트"

    open fun showHouse() {
        Log.d("클래스", "my house=${house}")
    }
}

class Child : Parent() {
    // 자식클래스는 부모클래스의 프라퍼티(속성)과 메서드 사용 가능

    override var house = "강남 10평 오피스텔"

    fun showMoney() {
        Log.d("클래스", "money=${money}")
    }

    override fun showHouse() {
        Log.d("클래스", "my house=${house}")
    }
}

// overrode
// 같은 클래스내에서 같은 이름의 메서드를 매개변수, 반환 자료형을 다르게 하여 사용
class Son {

    fun getNumber() : Int {
        return 1
    }

    fun getNumber(param:String) : Int {
        return 2
    }
}