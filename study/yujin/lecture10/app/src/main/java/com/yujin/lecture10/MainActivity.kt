package com.yujin.lecture10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yujin.lecture10.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 클래스 사용
//        var cls = Test() // 메모리에 올라간 클래스 => 인스턴스
//
//        cls.name = "김유진"
//        cls.function()

//        var log = Log()

//        Log.d()
        var parent = Parent()
        parent.showHouse()

        var child = Child()
        child.showMoney()
        child.showHouse()

        var son = Son()
        Log.d("SON","${son.getNumber()}")
        Log.d("SON","${son.getNumber("글")}")
        
    }
    // 상속을 하는 이유
    // 1. 기존의 작성된 코드를 재활용하기 위해서
    // 2. 코드를 재활용하는데 -> 좀 더 체계적으로 계층구조화해서 사용하기 위해서

    open class Parent {
        var money = 5000000000
        open var house = "강남 200평 아파트"

        open fun showHouse() {
            Log.d("Parent", "my House=${house}")
        }
    }

    class Child : Parent() {
        override var house = "강남 10평 아파트"
        fun showMoney() {
            Log.d("Child", "money = ${money}")
        }

        override fun showHouse() {
            Log.d("Child", "my House=${house}")
        }
    }

    class Son {
        fun getNumber(): Int {
            return 1
        }

        fun getNumber(input:String): Int {
            return 2
        }
    }

//    class Log{
//        companion object { // 클래스 초기화 없이 사용할 수 있다.
//            fun d(param1: String, param2: String) {
//                print("${param1} : ${param2}")
//            }
//        }
//    }
//
//    class Test{
//        var name:String = "" // 변수 - 프로퍼티
//        fun function(){ // 함수 - 메서드
//            var name_local = "로컬 김유진"
//        }
//
//
//    }
}