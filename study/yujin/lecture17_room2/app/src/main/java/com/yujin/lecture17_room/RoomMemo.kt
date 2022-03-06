package com.yujin.lecture17_room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "room_memo")
 class RoomMemo{
    @PrimaryKey(autoGenerate = true) // no 에 값이 없을 때 자동 증가된 숫자값을 db에 입력해준다.
    @ColumnInfo
    var no: Long? = null
    @ColumnInfo
    var content:String = ""
    @ColumnInfo(name = "date")
    var datetime:Long = 0

    constructor(content:String, datetime:Long){
        this.content = content
        this.datetime = datetime
    }

}
