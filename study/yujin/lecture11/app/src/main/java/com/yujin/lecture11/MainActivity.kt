package com.yujin.lecture11

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.yujin.lecture11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // 지연 초기화는 기본적으로 class 대상으로 하는 거다.
    // 지연초기화 -> 메모리 낭비를 막기위해서 나온 기술
    // var을 사용하니까 나중에 값을 다시 바꿀 수 있다.
    var name: String = "Scott"   // 특정 조건을 만족하지 않으면 사용하지 않는다.
    // 메모리 자원 낭비이다.
    // 문자열이 작은 경우는 크게 문제 되지 않지만 커지면 문제다.

    // lateinit : 코드 상에서 name2변수에 값을 넣을 것이다.
    // 기본형(Int, Long, 등)에서는 사용할 수 없다.
    lateinit var name2: String // String은 기본형이 아니다.
    lateinit var person: Person

    // lazy
    // val 변수명 by lazy {변수에 들어갈 클래스 생성자}
    // lazy는 읽기 전용, 한번 값을 정하고 바꾸지 않는 경우에 사용한다.
    val age by lazy { 21 } // lazy는 기본형도 사용할 수 있다.,이런 작은 값을 지연초기화 하기 위해서 사용하는건 아니다.!!!!!
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    // inflate하는 일은 ActivityMainBinding울 만들어서 반환한다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var myName: String = "메시"
        var number: Int? = null // nullable
        var newVariable: Activity? = null    // 클래스의 경우 new를 하지 않으면 담을 수 없고
        // new를 하면 메모리를 차지하기 때문에 null로 초기화하는 경우가 있다.

        Log.d("Null Test", "문자열의 길이는 = ${myName.length}")

        // null 이 아닌 경우에는 plus와 같은 클래스의 기능을 사용할 수 있다.
        var number2: Int = 30
        var result = number2.plus(50)

        // null인 경우에는 null이라는 메모리 공간을 차지하는 객체로 존재하는데 여기에는 plus와 같은 메서드가 정의되어 있지 않다.
        number?.plus(4) // ?이 없다면 Null Pointer Exception 때문에 App이 죽는다.
        // 1. nullable 로 개발자에게 환기시킨다.
        // 2. safe call 변수에 ? 를 붙이고 메서드를 호출한다.
        // 만약 변수가 null을 가리키고 있다면 ?뒤의 내용은 실행되지 않는다.

        var result2 = number?.plus(4) ?: 23 // Elvis Expression, number가 null인 경우 23을 result2에 대입함
        var result3 = result2.plus(53)

        name2 = "김유진"
        person = Person()
        studyRun()

//        binding.button.setOnClickListener {}
//        binding.imageView.setImageLevel(50)
//        binding.text.text= "반가워"
        // with는 바인딩할때 많이 사용한다.
        with(binding) {
            button.setOnClickListener {}
            imageView.setImageLevel(50)
            text.text = "반가워"
        }


    }

    // 스코프 함수
    // run, let, apply, also
    // with <- 애만 성격이 조금 다르다.
    fun studyRun() {
        val phones = mutableListOf("010-1234-5678", "010-3456-8978", "010-9512-2276")
        val list = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val names = mutableListOf("Scott", "Kelly", "Michael")


        val seoulPeople = SeoulPeople()

//            seoulPeople.person.add(Person("Scott", 21, "010-1234-5678"))
//            seoulPeople.person.add(Person("Scott", 21, "010-1234-5678"))
//            seoulPeople.person.add(Person("Scott", 21, "010-1234-5678"))
        // seoulPeople.person 생략 가능
        val resultRun = seoulPeople.person.run {
            add(Person("Scott", 21, "010-1234-5678"))
            add(Person("Scott", 21, "010-1234-5678"))
            add(Person("Scott", 21, "010-1234-5678"))
            11
        }


        val resultLet = seoulPeople.person.let { person -> // Aliasing할 수 있다.
//                add(Person("Scott", 21, "010-1234-5678")) 바로 사용할 수는 없다.
            person.add(Person("Scott", 21, "010-1234-5678"))
        }

        // 사용법은 run과 같지만 반환타입이 다르다.
        // scope 함수 안에서 자기 자신을 되돌려준다.
        val resultApply = seoulPeople.person.apply {
            add(Person("Scott", 21, "010-1234-5678"))
        }

        Log.d("스코프함수", "resultRun = ${resultRun}") // run의 경우 맨 마지막 항목이 리턴된다.
        Log.d("스코프함수", "resultApply = ${resultApply}") // apply의 경우 자기 자신을 리턴한다.

        // 사용법은 let과 같지만 반환 타입이 다르다.
        //
        val resultAlso = seoulPeople.person.also { person ->
            person.add(Person("Scott", 21, "010-1234-5678"))
        }
    }
}

class SeoulPeople {
    val person = mutableListOf<Person>()

    init {
        person.add(Person("Scott", 21, "010-1234-5678"))
        person.add(Person("Yujin", 31, "010-9512-2227"))
        person.add(Person("mam", 61, "010-5678-9999"))

    }
}

data class Person(
    var name: String = "",
    var age: Int = 21,
    var phone: String = ""
)