package net.lanlingdai.kotlinapplication.weight

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TabHost
import android.widget.TextView
import net.lanlingdai.kotlinapplication.R
import java.util.jar.Attributes

class KChartTabView constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes),View.OnClickListener{
    lateinit var mLlContainer : LinearLayout
    lateinit var mTvFullScreen : TextView
    lateinit var mTabSelectListener : TabSelectListener
     var mSelectedIndex = 0
    lateinit var  mColorStateList : ColorStateList
    var mIndicatorColor = 0

    override fun onClick(v: View?) {
        if(mSelectedIndex >= 0 && mSelectedIndex < mLlContainer.childCount ){
            mLlContainer.getChildAt(mSelectedIndex).isSelected = false
        }
        mSelectedIndex =  mLlContainer.indexOfChild(v)
        v?.let {v.isSelected = true  }
        mTabSelectListener.onTabSelected(mSelectedIndex)
    }
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr,0)

    init {
        var view = LayoutInflater.from(context).inflate(R.layout.layout_tab,null,false)
        var layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewUtil.Dp2PX(context,30f))
        view.layoutParams = layoutParams
        addView(view)
        mLlContainer = findViewById(R.id.ll_container)
        mTvFullScreen = findViewById(R.id.tv_fullScreen)
        mTvFullScreen.setOnClickListener({
            v->
            var activity = context as Activity
            var isVertical = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            if(isVertical){
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }else{
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            }

        })
        mTvFullScreen.isSelected = true


    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 添加选项卡
     */
    fun addTab(text : String){
        var tabView = TextView(context)
        tabView.setOnClickListener(this)
        tabView.text = text
        tabView.setTextColor(mColorStateList)
        mLlContainer.addView(tabView)
        //默认选中第一个
        if(mLlContainer.childCount == 1){
            tabView.isSelected = true
            mSelectedIndex = 0
            onTabSelected(mSelectedIndex)
        }
    }

    private fun onTabSelected(position: Int) {
        if (mTabSelectListener != null){
            mTabSelectListener.onTabSelected(position)
        }
    }
    fun setTextColor(color : ColorStateList?){

        mColorStateList = color!!
        for ( i in 0.. mLlContainer.childCount){
          var tabView= mLlContainer.getChildAt(i) as TabView
            tabView.setTextColor(mColorStateList)
        }
        mTvFullScreen.setTextColor(mColorStateList)
    }
    fun setIndicatorColor(color : Int){
        mIndicatorColor = color
        var tabView : TabView
        for( i in 0 .. mLlContainer.childCount ){
            tabView= mLlContainer.getChildAt(i) as TabView
            tabView.setIndicatorColor(mIndicatorColor)      }
    }

    fun setOnTabSelectedListenter(onTabSelectListener: TabSelectListener){
        mTabSelectListener = onTabSelectListener
    }


}