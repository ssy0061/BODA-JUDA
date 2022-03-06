package com.yujin.lecture17_room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yujin.lecture17_room.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

class RecyclerAdapter(val roomMemoList:List<RoomMemo>) :RecyclerView.Adapter<RecyclerAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setMemo(roomMemoList.get(position))
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class Holder(val binding:ItemRecyclerBinding):RecyclerView.ViewHolder(binding.root){
        fun setMemo(roomMemo:RoomMemo){
            with(binding){
                textNo.text = "${roomMemo.no}"
                textContent.text = roomMemo.content
                val sdf =SimpleDateFormat("yyyy/MM/dd hh:mm")
                textDatetime.text = sdf.format(roomMemo.datetime)
            }
        }
    }
}