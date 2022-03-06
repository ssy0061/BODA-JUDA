package com.ssafy.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.recyclerview.databinding.ActivityMainBinding
import com.ssafy.recyclerview.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 1. 데이터를 불러온다.
        val data = loadData()
        // 2. 아답터를 생성
        val customAdapter = CustomAdapter(data)
        // 3. 화면의 RecyclerView와 연결
        binding.recyclerView.adapter = customAdapter
        // 4. 레이아웃 매니저 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun loadData() : MutableList<Memo>{
        val memoList = mutableListOf<Memo>()
        for(no in 1..100){
            val title = "이것이 안드로이드다 $no"
            val date = System.currentTimeMillis()
            val memo = Memo(no, title, date)
            memoList.add(memo)
        }
        return memoList
    }
}

class CustomAdapter(val listData:MutableList<Memo>) : RecyclerView.Adapter<CustomAdapter.Holder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 1. 사용할 데이터를 꺼내고
        val memo = listData.get(position)
        // 2. 홀더에 데이터를 전달
        holder.setMemo(memo)
    }

    override fun getItemCount() = listData.size


    class Holder(val binding:ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root){
        lateinit var currentMemo:Memo
        // 클릭처리는 init에서만 한다.
        init {
            binding.root.setOnClickListener {
                Toast.makeText(binding.root.context,"클릭된 아이템 : ${currentMemo.title}", Toast.LENGTH_LONG).show()
            }
        }

        // 3. 받은 데이터를 화면에 출력
        fun setMemo(memo:Memo){
            currentMemo = memo
            with(binding){
                textNo.text = "${memo.no}"
                textTitle.text = memo.title

                val temp = SimpleDateFormat("yyyy-MM-dd")
                val formattedDate = temp.format(memo.timestamp)
                textDate.text = formattedDate
            }
        }
    }
}