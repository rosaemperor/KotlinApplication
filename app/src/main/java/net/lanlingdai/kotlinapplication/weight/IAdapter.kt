package net.lanlingdai.kotlinapplication.weight

import android.database.DataSetObserver
import java.text.FieldPosition
import java.util.*

interface IAdapter{
    /**
     * 获得点的条目数
     */
    fun getCount() : Int

    /**
     * 通过序列号获取实体
     */
    fun getItem(position: Int) : Any

    /**
     * 通过序列号获取时间
     */
    fun getDate(position: Int) : Date

    /**
     * 注册数据观察者
     */
    fun registerDataSetObserver(observer: DataSetObserver)

    /**
     * 移除数据观察者
     */
    fun unregisterDataSetObserver(observer: DataSetObserver)

    /**
     * 刷新数据时调用
     */
    fun notifyDataSetChanged()
}

