package net.lanlingdai.kotlinapplication.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import io.reactivex.annotations.NonNull

@Entity(tableName = "Student")
class Student : Parcelable {
    @NonNull
    @ColumnInfo(name = "realName")
    var name: String = "使用"

    var age: Int = 0

    @PrimaryKey
    var ID: Int = 410

    constructor(source: Parcel) : this(
    )
    constructor()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Student> = object : Parcelable.Creator<Student> {
            override fun createFromParcel(source: Parcel): Student = Student(source)
            override fun newArray(size: Int): Array<Student?> = arrayOfNulls(size)
        }
    }
}