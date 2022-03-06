package com.yujin.lecture17_room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomMemoDAO {
    @Query("select * from room_memo")
    fun getAll():List<RoomMemo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inser(memo:RoomMemo)

    fun delete(memo:RoomMemo)
}