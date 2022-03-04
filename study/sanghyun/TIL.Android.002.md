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

#### 003. RecyclerView

<br>

```kotlin
class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 1. load data
        val memoList = loadMemos()

        // 2. create custom adapter
        val customAdapter = CustomAdapter(memoList)

        // 3. binding
        binding.recyclerView.adapter = customAdapter

        // 4. layout manager
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

    fun loadMemos(): MutableList<Memo> {
        val memoList = mutableListOf<Memo>()
        for (idx in 1..100) {
            val title = "이것이 안드로이드다 $idx"
            val date = System.currentTimeMillis()
            val memo = Memo(idx, title, date)
            memoList.add(memo)
        }

        return memoList
    }
}

class CustomAdapter(val memoList: MutableList<Memo>) : RecyclerView.Adapter<CustomAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 1. 사용할 데이터 추출
        val memo = memoList.get(position)

        // 2. 해당 데이터 홀더에 전달
        holder.setMemo(memo)
    }

    override fun getItemCount() = memoList.size

    class Holder(val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        var currentMemo: Memo? = null

        // 불필요한 호출을 막기 위해 init에 클릭리스너 작성
        init {
            binding.root.setOnClickListener {
                Toast.makeText(binding.root.context, "클릭된 아이템: ${currentMemo?.title}", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. representation
        fun setMemo(memo: Memo) {
            currentMemo = memo
            with (binding) {
                textNo.text = "${memo.no}"
                textTitle.text = memo.title

                val sdf = SimpleDateFormat("yyyy-dd-mm")
                val formattedDate = sdf.format(memo.timestamp)
                textDate.text = formattedDate
            }
        }
    }
}
```



<br>

#### 004. Fragment

<br>

**MainActivity.kt**

```kotlin
class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    // 1. fragment 생성
    val listFragment by lazy { ListFragment() }
    val detailFragment by lazy { DetailFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFragment()

        binding.btnSend.setOnClickListener {
            listFragment.setValue("값 전달하기")
        }
    }

    fun goDetail() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameLayout, detailFragment)
        transaction.addToBackStack("detail")
        transaction.commit()
    }

    fun goBack() {
        onBackPressed()
    }

    fun setFragment() {
        val bundle = Bundle()
        bundle.putString("key1", "List Fragment")
        bundle.putInt("key2", 20220305)

        listFragment.arguments = bundle

        // 2. transaction
        val transaction = supportFragmentManager.beginTransaction()

        // 3. inserting fragments by transaction
        transaction.add(R.id.frameLayout, listFragment)
        transaction.commit()
    }
}
```

<br>

**ListFragment.kt**

```kotlin
class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) mainActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            arguments?.apply {
                textTitle.text = getString("key1")
                textValue.text = "${getInt("key2")}"
            }

            btnNext.setOnClickListener {
                mainActivity.goDetail()
            }
        }
    }

    fun setValue(value: String) {
        binding.textFromActivity.text = value
    }
}
```

<br>

**DetailFragment.kt**

```kotlin
class DetailFragment : Fragment() {

    lateinit var binding: FragmentDetailBinding
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) mainActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            mainActivity.goBack()
        }
    }
}
```

