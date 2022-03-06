package com.ssy.scopefunction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ssy.scopefunction.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        studyRun()


//        binding.button.setOnClickListener {}
//        binding.textView.text = "반가워"
        with(binding) {
            button.setOnClickListener {}
            textView.text = "반가워"
        }

    }
    // 지연초기화와 같이 safe콜 남용을 막아줌
    // 스코프 함수는 크게 5가지
    // run, let, apply, also
    // with
    fun studyRun() {
        val phones = mutableListOf("010-1234-5678", "010-3456-8978", "010-4578-1345")
        val list = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val names = mutableListOf("Scott", "Kelly", "Michael")

//        phones.add("010-2345-9871")
        phones.run {
            add("010-2345-9871")
        }

        val seoulPeople = SeoulPeople()

//        seoulPeople.persons.add(Person("Scott", "010-1234-5678", 19))
        // 마지막 구문 반환
        val resultRun = seoulPeople.persons.run {
            add(Person("Scott", "010-1234-5678", 19))
            11
        }

        // alias를 쓰고 싶을 때
        // 마지막 구문 반환
        val resultLet = seoulPeople.persons.let { persons ->
            persons.add(Person("Scott", "010-1234-5678", 20))
            12
        }

        // 마지막 구문과 상관없이 persons 배열을 반환환
       val resultApply = seoulPeople.persons.apply {
            add(Person("Scott", "010-1234-5678", 21))
            11
        }

        // alias를 쓰고 싶을 때
        // 마지막 구문과 상관없이 persons 배열을 반환환
        val resultAlso = seoulPeople.persons.also { persons ->
            persons.add(Person("Scott", "010-1234-5678", 22))
            12
        }
        Log.d("스코프함수", "resultApply: ${resultRun}")
        Log.d("스코프함수", "resultApply: ${resultLet}")
        Log.d("스코프함수", "resultApply: ${resultApply}")
        Log.d("스코프함수", "resultApply: ${resultAlso}")
    }
}

class SeoulPeople {
    var persons = mutableListOf<Person>()
    init {
//        persons.add(Person("Scott", "010-1234-5678", 19))
//        persons.add(Person("Kelly", "010-1234-5678", 20))
//        persons.add(Person("Michael", "010-1234-5678", 21))
    }
}

data class Person (
    var name:String = "",
    var phone:String = "",
    var age:Int = 21
)