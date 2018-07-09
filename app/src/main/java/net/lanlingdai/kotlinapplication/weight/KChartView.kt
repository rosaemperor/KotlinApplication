package net.lanlingdai.kotlinapplication.weight

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import net.lanlingdai.kotlinapplication.R

class KChartView     constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
: BaseKChartView(context, attrs, defStyleAttr, defStyleRes){
    lateinit var mProgressBar : ProgressBar
    var isRefreshing  = false
    var isLoadMoreEnd = false
    var mLastScrollEnable = false
    var mLastScaleEnable = false
    lateinit var mRefreshListener : KChartRefreshListener
     var mMACDDraw : MACDDraw
     var mRSIDraw: RSIDraw
     var mMainDraw: MainDraw
     var mKDJDraw: KDJDraw
     var mVolumeDraw: VolumeDraw
     var mBOLLDraw: BOLLDraw


    override fun onLeftSide() {
        showLoadding()
    }

    private fun showLoadding() {
        if(!isLoadMoreEnd && ! isRefreshing){
          isRefreshing = true
            mProgressBar.visibility = View.VISIBLE
            mRefreshListener.onLoadMoreBegin(this)
            mLastScaleEnable = mScaleEnable
            mLastScrollEnable = mScrollEnable
            mScaleEnable = false
            mScrollEnable =false
        }
    }



    override fun onRightSide() {
    }
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?,attrs: AttributeSet?) : this(context , attrs , 0)
    constructor(context: Context?,attrs: AttributeSet?,defStyleAttr: Int) : this(context , attrs , defStyleAttr,0)

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
        setMainDraw(mMainDraw as IChartDraw<Any>)
        addChildDraw("VOL",mVolumeDraw as IChartDraw<Any>)
        addChildDraw("MACD",mMACDDraw as IChartDraw<Any>)
        addChildDraw("KDJ",mKDJDraw as IChartDraw<Any>)
        addChildDraw("RSI",mRSIDraw as IChartDraw<Any>)
        addChildDraw("BOLL",mBOLLDraw as IChartDraw<Any>)

        context?.let {
            var array = context.obtainStyledAttributes(attrs, R.styleable.KChartView)
            array?.let {
                try{

                mPointWidth = array.getDimension(R.styleable.KChartView_kc_point_width ,getDimension(R.dimen.chart_point_width))
                setTextSize(array.getDimension(R.styleable.KChartView_kc_text_size,getDimension(R.dimen.chart_text_size)))
                setTextColor(array.getColor(R.styleable.KChartView_kc_text_color, getColor(R.color.chart_text)))
                mLineWidth = array.getDimension(R.styleable.KChartView_kc_line_width, getDimension(R.dimen.chart_line_width))
                mBackgroundPaint.color = array.getColor(R.styleable.KChartView_kc_background_color, getColor(R.color.chart_background))
                mSelectedLintPaint.color = array.getColor(R.styleable.KChartView_kc_selected_line_color,getColor(R.color.chart_text))
                mSelectedLintPaint.strokeWidth = array.getDimension(R.styleable.KChartView_kc_selected_line_width, getDimension(R.dimen.chart_line_width))
                mGridPaint.strokeWidth = array.getDimension(R.styleable.KChartView_kc_grid_line_width , getDimension(R.dimen.chart_grid_line_width))
                mGridPaint.color = array.getColor(R.styleable.KChartView_kc_grid_line_color , getColor(R.color.chart_grid_line))

                //macd
                mMACDDraw.mMACDWidth = array.getDimension(R.styleable.KChartView_kc_macd_color , getDimension(R.dimen.chart_candle_width))
                mMACDDraw.setMACDColor(array.getColor(R.styleable.KChartView_kc_macd_color, getColor(R.color.chart_ma20)))
                mMACDDraw.setDEAColor(array.getColor(R.styleable.KChartView_kc_dea_color,getColor(R.color.chart_ma10)))
                mMACDDraw.setDIFColor(array.getColor(R.styleable.KChartView_kc_dif_color,getColor(R.color.chart_ma5)))

                //kdj
                mKDJDraw.mKPaint.color = array.getColor(R.styleable.KChartView_kc_k_color,getColor(R.color.chart_ma5))
                mKDJDraw.mDPaint.color = array.getColor(R.styleable.KChartView_kc_d_color,getColor(R.color.chart_ma10))
                mKDJDraw.mJPaint.color = array.getColor(R.styleable.KChartView_kc_j_color, getColor(R.color.chart_ma20))

                //rsi
                mRSIDraw.mRSI1Paint.color = array.getColor(R.styleable.KChartView_kc_dif_color, getColor(R.color.chart_ma5))
                mRSIDraw.mRSI2Paint.color = array.getColor(R.styleable.KChartView_kc_dea_color,getColor(R.color.chart_ma10))
                mRSIDraw.mRSI3Paint.color = array.getColor(R.styleable.KChartView_kc_macd_color,getColor(R.color.chart_ma20))

                //boll
                mBOLLDraw.mUpPaint.color = array.getColor(R.styleable.KChartView_kc_dif_color, getColor(R.color.chart_ma5))
                mBOLLDraw.mMbPaint.color = array.getColor(R.styleable.KChartView_kc_dea_color,getColor(R.color.chart_ma10))
                mBOLLDraw.mDnPaint.color = array.getColor(R.styleable.KChartView_kc_macd_color,getColor(R.color.chart_ma20))

                //main
                mMainDraw.ma5Paint.color = array.getColor(R.styleable.KChartView_kc_dif_color, getColor(R.color.chart_ma5))
                mMainDraw.ma10Paint.color = array.getColor(R.styleable.KChartView_kc_dea_color,getColor(R.color.chart_ma10))
                mMainDraw.ma20Paint.color = array.getColor(R.styleable.KChartView_kc_macd_color,getColor(R.color.chart_ma20))
                //设置蜡烛宽度
                mMainDraw.mCandleWidth = array.getDimension(R.styleable.KChartView_kc_candle_width, getDimension(R.dimen.chart_candle_width))
                mMainDraw.mCandleLineWidth = array.getDimension(R.styleable.KChartView_kc_candle_line_width , getDimension(R.dimen.chart_candle_line_width))
                mMainDraw.mSelectorBackgroundPaint.color = array.getColor(R.styleable.KChartView_kc_selector_background_color, getColor(R.color.chart_selector))
                mMainDraw.mSelectorTextPaint.textSize = array.getDimension(R.styleable.KChartView_kc_selector_text_size , getDimension(R.dimen.chart_selector_text_size))
                mMainDraw.mCandleSolid = array.getBoolean(R.styleable.KChartView_kc_candle_solid , true)

                //tab
                mKChildTabView.mIndicatorColor = array.getColor(R.styleable.KChartView_kc_tab_indicator_color , getColor(R.color.chart_tab_indicator))
                mKChildTabView.setBackgroundColor(array.getColor(R.styleable.KChartView_kc_tab_background_color , getColor(R.color.chart_tab_background)))
                var colorSatteList = array.getColorStateList(R.styleable.KChartView_kc_tab_text_color)
                if(colorSatteList ==null) {
                    mKChildTabView.setTextColor(ContextCompat.getColorStateList(context, R.color.tab_text_color_selector))
                }else{
                    mKChildTabView.setTextColor(colorSatteList)
                }

                }
                catch (e: Exception){
                    e.printStackTrace()
                }
                finally {
                    array.recycle()
                }

            }
        }




    }

    private fun getDimension(resId : Int) : Float{
        return resources.getDimension(resId)
    }

    private fun getColor(resId: Int) : Int{
        return ContextCompat.getColor(context, resId)
    }

    private  fun hideLoading(){
        mProgressBar.visibility = View.GONE
        mScrollEnable = mLastScrollEnable
        mScaleEnable = mLastScaleEnable
    }

    /**
     * 刷新完成
     */
    fun refresheComplete(){
        isRefreshing = false
        hideLoading()
    }
    /**
     * 刷新完成，但无数据
     */
    fun  refreshEnd(){
        isLoadMoreEnd = true
        isRefreshing = false
        hideLoading()
    }




}