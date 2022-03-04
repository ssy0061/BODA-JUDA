**TIL > Android**

<br>

## 🔥 TIL: Android

> * 작성자: Sanghyun Park
> * 최근 수정일시: 2022. **03. 04.** (금)



<br>

#### 001. 리스너 이해하기

<br>

```kotlin
// listener 작성
val listener = object : View.OnClickListener {
    // `ctrl + i`: overriding 해야 하는 메서드 확인 가능 
    override fun onClick(p0: View?) {
        Log.d("ClickListener", "클릭 되었습니다.")
    }
}

// listener 사용
with (binding) {
    button.setOnClickListener(listener)
}

// listener의 메서드가 한 개인 경우, 람다식처럼 사용 가능
with (binding) {
    button.setOnClickListener {
        Log.d("ClickListener", "클릭 되었습니다.")
    }
}
```



<br>

#### 002. Values Resource File

<br>

```kotlin
// 높이, 너비, 크기
<dimen name="hei_btn">80dp</dimen>
<dimen name="size_text">20sp</dimen>

// 색
<color name="text">#F86496</color>

// 스트링(content)
<string name="tView_content">Hello, Android and Kotlin</string>
```



<br>
