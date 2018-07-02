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
import android.widget.TextView
import net.lanlingdai.kotlinapplication.R
import java.util.jar.Attributes

class KChartTabView : RelativeLayout,View.OnClickListener{
    lateinit var mLlContainer : LinearLayout
    lateinit var mTvFullScreen : TextView
    lateinit var mTabSelectListener : TabSelectListener
     var mSelectedIndex = 0
    lateinit var  mColorStateList : ColorStateList
    var mIndicatorColor = 0

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

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
        mColorStateList?.let {
            mTvFullScreen.setTextColor(mColorStateList)
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }



}