package net.lanlingdai.kotlinapplication.entity

import android.databinding.BaseObservable
import android.os.Parcel
import android.os.Parcelable

class Type : BaseObservable, Parcelable {
    private lateinit var name: String

    private lateinit var type: String

    constructor(source: Parcel) : this(
    )
    constructor()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Type> = object : Parcelable.Creator<Type> {
            override fun createFromParcel(source: Parcel): Type = Type(source)
            override fun newArray(size: Int): Array<Type?> = arrayOfNulls(size)
        }
    }
}