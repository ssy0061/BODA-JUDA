package com.dh.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dh.recyclerview.databinding.ActivityMainBinding
import com.dh.recyclerview.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 데이터 불러오기
        val data = loadData()
        // Adapter 생성
        val customAdapter = CustomAdapter(data)
        // 화면의 recycler view와 연결
        binding.recyclerView.adapter = customAdapter
        // 레이아웃 매니저 섷정
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

class CustomAdapter(val listData :MutableList<Memo>) : RecyclerView.Adapter<CustomAdapter.Holder>() {


    class Holder(val binding: ItemRecyclerBinding) :RecyclerView.ViewHolder(binding.root){

        lateinit var currentMemo: Memo // 항상담기면 nullable 안해도됨

        init{
            binding.root.setOnClickListener{
                Toast.makeText(binding.root.context,"클릭된 아이템 : ${currentMemo.title}", Toast.LENGTH_SHORT).show()
            }
        }
        fun setMemo(memo:Memo){

            currentMemo = memo
            with(binding){
                textNo.text = "${memo.no}"
                textTitle.text = memo.title

                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val formattedDate = sdf.format(memo.timestamp)
                textDate.text = "${formattedDate}"
            }
        }
    }

    // 목록이 30개면 30개까지 보여주고, 그 뒤부터는 재사용
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    // 목록의 개수만큼 호출된다
    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 사용할 데이터 꺼내기
        val memo = listData.get(position)
        // 홀더에 데이터 전달
        holder.setMemo(memo)
    }

    override fun getItemCount() = listData.size

}