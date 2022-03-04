**TIL > Android**

<br>

## 🔥 TIL: Android

> * 작성자: Sanghyun Park
> * 최근 수정일시: 2022. **03. 04.** (금)



<br>

#### 001. Android App 만들기와 실행하기

<br>

**View Binding**

> *View binding* is a feature that allows you to more easily write code that interacts with views. Once view binding is enabled in a module, it generates a *binding class* for each XML layout file present in that module. An instance of a binding class contains direct references to all views that have an ID in the corresponding layout.

<br>

```kotlin
// build.gradle (Module)

android {
	buildFeatures {
        viewBinding true
    }
}
```

<br>

```kotlin
val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    binding.btnSay.setOnClickListener {
        binding.textSay.text = "Hello, Kotlin! and Android!"
        
        for (i in 1..10) {
	        binding.textSay.append("Hello! ${i}\n")            
        }
    }
}
```



<br>

#### 002. Android에서 Log를 사용하는 방법

<br>

```kotlin
val TAG = "MainActivity"

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    binding.btnSay.setOnClickListener {
        Log.d(TAG, "button touched")
    }
}
```



<br>

#### 003. 클래스

<br>

```kotlin
class Log {
    // 인스턴스 생성 없이 바로 접근 가능
    companion object {
        val name = "James"
        fun d(tag: String, msg: String) {
			println("[$tag]: $msg")
        }
    }
}

println(Log.name)
Log.d("FE", "organize the vuex")
```



<br>

#### 004. Null Safety

<br>

```kotlin
var myNum = 7

// nullable values
var myName: Int? = null

// NPE: myName에는 `null`이 담겨 있음
var result = myName.plus(13)

// safe calls
// myName에는 `null`이 담겨 있기 때문에 `.plus()`가 실행되지 않음, 그대로 `null`
var result = myName?.plus(13)

// Elvis operator
var result = myName?.plus(13) ?: 0
```



<br>

#### 005. 지연 초기화

<br>

```kotlin
class Person {
	val name = "James"
    val age = 27
    val address = "Seoul"
    val contact = "2dend0713@gmail.com"
}

// 선언 먼저 하고 나중에 초기화 해서 사용
lateinit var person: Person
person = Person()

// 나중에 호출해서 사용
val person by lazy { Person() }
println(person.name)
```



<br>

#### 006. 스코프 함수

<br>

```kotlin
data class Person (val name: String, val contact: String, val age: Int)

class SeoulPeople {
    var people = mutableListOf<Person>()
    init {
        people.add(Person("James", "010-6655-3446", "27"))
        people.add(Person("Lucy", "010-6123-4421", "28"))
        people.add(Person("Duke", "010-4456-7687", "29"))
    }
}

seoulPeople = SeoulPeople()

// run: 스코프 내 마지막 실행문의 결과 반환
val result = seoulPeople.people.run {
    add(Person("Kelly", "010-43657-2276, "24"))
	size
}

// let using alias: 스코프 내 마지막 실행문의 결과 반환
val result = seoulPeople.people.let { people ->
    people.add(Person("Kelly", "010-43657-2276, "24"))
    size
}
                      
// apply: 자기 자신 반환
val result = seoulPeople.people.apply {
    add(Person("Kelly", "010-43657-2276, "24"))
}

// also using alias: 자기 자신 반환
val result = seoulPeople.people.also { people ->
    people.add(Person("Kelly", "010-43657-2276, "24"))
}

// with
val binding lazy { ActivityMainBinding.inflate(layoutInflater) }
with(binding) {
    button.setOnClickListener { ... }
    imageView.setImageLevel(50)
    textView.text = "Hello"
}
```

