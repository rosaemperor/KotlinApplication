package net.lanlingdai.kotlinapplication.weight

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar

class KChartView : BaseKChartView{
    lateinit var progressBar : ProgressBar
    var isRefreshing  = false
    var isLoadMoreEnd = false
    var mLastScrollEnable = false
    var mLastScaleEnable = false
    lateinit var mRefreshListener : KChartRefreshListener
    lateinit var mMACDDraw : MACDDraw



    override fun onLeftSide() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRightSide() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
}