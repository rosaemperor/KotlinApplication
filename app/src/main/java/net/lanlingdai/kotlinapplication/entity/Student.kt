package net.lanlingdai.kotlinapplication.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import io.reactivex.annotations.NonNull

@Entity(tableName = "Student")
class Student {
    @NonNull
    @ColumnInfo(name = "realName")
    var name : String = "中洋使用"

    var age : Int = 0
    @PrimaryKey
    var ID : Int = 410

}