package net.lanlingdai.kotlinapplication.weight

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import net.lanlingdai.kotlinapplication.R

class TabView : RelativeLayout{
    lateinit var mTextView :TextView
    var mIndicator : View
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
    init {
        addView(LayoutInflater.from(context).inflate(R.layout.item_tab,null))
        mTextView= findViewById(R.id.tab_text)
        mIndicator = findViewById(R.id.indicator)
    }
    fun setTextColor(color : ColorStateList){
        color?.let {
            mTextView.setTextColor(color)
        }
    }

    fun setText(text : String){
        mTextView.text = text
    }

    fun setIndicatorColor(color : Int){
        mIndicator.setBackgroundColor(color)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        mIndicator.visibility = if (selected) View.VISIBLE else View.INVISIBLE
    }
}