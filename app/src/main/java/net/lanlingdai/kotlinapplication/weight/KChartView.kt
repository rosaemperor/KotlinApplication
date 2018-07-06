package net.lanlingdai.kotlinapplication.weight

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout

class KChartView : BaseKChartView{
    lateinit var mProgressBar : ProgressBar
    var isRefreshing  = false
    var isLoadMoreEnd = false
    var mLastScrollEnable = false
    var mLastScaleEnable = false
    lateinit var mRefreshListener : KChartRefreshListener
    lateinit var mMACDDraw : MACDDraw
    lateinit var mRSIDraw: RSIDraw
    lateinit var mMainDraw: MainDraw
    lateinit var mKDJDraw: KDJDraw
     var mVolumeDraw: VolumeDraw
    lateinit var mBOLLDraw: BOLLDraw


    override fun onLeftSide() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRightSide() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes){

    }
    init {
        mProgressBar = ProgressBar(context)
        var layoutParams = RelativeLayout.LayoutParams(dp2px(50f),dp2px(50f))
        layoutParams.addRule(CENTER_IN_PARENT)
        addView(mProgressBar,layoutParams)
        mProgressBar.visibility = View.GONE
        mVolumeDraw = VolumeDraw(this)
        mMainDraw = MainDraw(this)
        mBOLLDraw = BOLLDraw(this)
        mRSIDraw = RSIDraw(this)
        mMACDDraw = MACDDraw(this)
        mKDJDraw = KDJDraw(this)
        addChildDraw("",mVolumeDraw)
    }

}