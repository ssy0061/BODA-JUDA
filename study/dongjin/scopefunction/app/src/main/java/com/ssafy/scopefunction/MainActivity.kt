package com.ssafy.scopefunction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ssafy.scopefunction.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        studyRun()

        with(binding){
            button.setOnClickListener {  }
            imageView.setImageLevel(50)
            textView.text = "반가워"
        }

    }


    //스코프함수
    //run, let, apply, also
    //with
    //1. run
    fun studyRun(){
        val phone = mutableListOf("010-1234-5678", "010-3456-8978", "010-4587,1345")
        val list = mutableListOf(1,2,3,4,5,6,7,8,9)
        val names = mutableListOf("Scott", "kelly","Michael")

        val seoulPeople = SeoulPeople()
        var resultRun = seoulPeople.persons.run {
            add(Person("Scott", "010-1234-5678",19))
            add(Person("Scott", "010-1234-5678",19))
            add(Person("Scott", "010-1234-5678",19))
            11
        }

        var resultLet = seoulPeople.persons.let{ persons ->
            persons.add(Person("Scott", "010-1234-5678",19))
            persons.add(Person("Scott", "010-1234-5678",19))
            persons.add(Person("Scott", "010-1234-5678",19))
            12
        }

        var resultApply = seoulPeople.persons.apply {
            add(Person("Scott", "010-1234-5678",19))
            add(Person("Scott", "010-1234-5678",19))
            add(Person("Scott", "010-1234-5678",19))
            11
        }
        val TAG = "스코프함수"


        var resultAlso = seoulPeople.persons.also{ persons ->
            persons.add(Person("Scott", "010-1234-5678",19))
            persons.add(Person("Scott", "010-1234-5678",19))
            persons.add(Person("Scott", "010-1234-5678",19))
            12
        }
        Log.d(TAG, "resulApply=${resultRun}")
        Log.d(TAG, "resulApply=${resultLet}")
        Log.d(TAG, "resulApply=${resultApply}")
        Log.d(TAG, "resulApply=${resultAlso}")
    }
}

class SeoulPeople{
    var persons = mutableListOf<Person>()
    init{
        persons.add(Person("Scott", "010-1234-5678",19))
        persons.add(Person("Scott", "010-1234-5678",19))
        persons.add(Person("Scott", "010-1234-5678",19))
    }
}

data class Person(
    var name:String = "",
    var phone:String = "",
    var age:Int = 21
)