package net.lanlingdai.kotlinapplication.weight

import android.animation.ValueAnimator
import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.view.GestureDetectorCompat
import android.util.AttributeSet
import android.view.ScaleGestureDetector
import android.widget.AdapterViewAnimator
import net.lanlingdai.kotlinapplication.R

class BaseKChartView : ScrollAndScaleView{
    private var mTranslateX = Float.MIN_VALUE
    private var mWidth = 0
    private var mTopPadding : Int = 0
    private var mBottomPadding : Int = 0
    private var mMainScaleY : Float =1f
    private var mChildScaleY : Float = 1f
    private var mDataLen : Float = 0f
    private var mMainMaxValue = Float.MAX_VALUE
    private var mMainMinValue = Float.MIN_VALUE
    private var mChildMaxValue =Float.MAX_VALUE
    private var mChildMinValue = Float.MIN_VALUE
    private var mStartIndex = 0
    private var mStopIndex = 0
    private var mPointWidth : Float = 6f
    private var mGridRows =4
    private var mGridColumns =4
    private var mGridPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mSelectedLintPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mSelectedIndex = 0
    private lateinit var mMainDraw : IChartDraw
    private lateinit var iAdapter : IAdapter


    private var mDataSetObserver : DataSetObserver = object : DataSetObserver(){
        override fun onChanged() {
            mItemCount = iAdapter.getCount()
            notifyChanged()
        }

        override fun onInvalidated() {
            mItemCount = iAdapter.getCount()
            notifyChanged()
        }
    }
    private var mItemCount = 0
    private lateinit var mChildDraw : IChartDraw
    private var mChildDraws : List<IChartDraw> = ArrayList()

    private lateinit var mValueFormatter : IValueFormatter
    private lateinit var mDataFormatter : IDateTimeFormatter

//    protected lateinit var

    private lateinit var mAnimator: ValueAnimator
    private var mAnimatorDuration : Long = 500
    private var mOverScrollRange : Float = 0f
    private lateinit var mOnSelectedChangedListener: OnSelectedChangedListener

    private lateinit var mMainRect : Rect
    private lateinit var mChildRect : Rect
    private lateinit var mTabRect : Rect
    private  var mLineWidth : Float = 0f


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
    init {
        setWillNotDraw(false)
        mDetector = GestureDetectorCompat(context,this)
        mScaleDetector = ScaleGestureDetector(context ,this)
        mTopPadding  = resources.getDimension(R.dimen.chart_top_padding).toInt()
        mBottomPadding = resources.getDimension(R.dimen.chart_bottom_padding).toInt()


        //因为KChart TabView还没有定义，，，所以暂时不添加，以后补上

        mAnimator = ValueAnimator.ofFloat(0f,1f)
        mAnimator.duration= mAnimatorDuration
        mAnimator.addUpdateListener { object : ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                invalidate()
            }
        }
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.mWidth = w
        initRect(w,h)
        //缺少KChartTABView
        setTranslateXFromScrollX(mScrollX)

    }
    override fun onLeftSide() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRightSide() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMinScrollX(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMaxScrollX(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




    fun notifyChanged(){
        if(mItemCount!=0){
            mDataLen = (mItemCount-1)*mPointWidth
            checkAndFixScrollX()
            setTranslateXFromScrollX(mScrollX)
        }else{
            scrollX = 0
        }
        invalidate()
    }



    private fun setTranslateXFromScrollX(scrollX : Int){
        mTranslateX = scrollX + getMinTranslateX()
    }


    private fun getMinTranslateX() : Float{
        return -mDataLen + mWidth / mScaleX - mPointWidth /2
    }

    private fun initRect(w : Int,h : Int){
        //不行了，，，没有KChartTabView.有点进行不下去
        var mMainChildSpace  = 0
        var displayHeight = h - mTopPadding - mBottomPadding - mMainChildSpace
        var mMainHeight = displayHeight * 0.75f.toInt()
        var mChildHeight = displayHeight * 0.25f.toInt()
        mMainRect  = Rect(0,mTopPadding,mWidth,mTopPadding+mMainHeight)
        mTabRect  = Rect(0,mMainRect.bottom, mWidth ,mMainChildSpace+ mMainRect.bottom)
        mChildRect = Rect(0 , mTabRect.bottom , mWidth , mTabRect.bottom + mChildHeight)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            canvas.drawColor(mBackgroundPaint.color)
            if(mWidth == 0 || mMainRect.height() == 0 || mItemCount == 0){
                return
            }
            calculateValue()
            canvas.save()
            canvas.scale(1f,1f)
            drawGird(canvas)
//            drawK(canvas)
//            drawText(canvas)
//            drawValue(canvas,isLongPress ? mSelectedIndex : mStopIndex)
            canvas.restore()

        }

    }

    /**
     * 绘制背景表格
     */
    private fun drawGird(canvas: Canvas) {
        //-----------------------上方k线图------------------------
        var rowSpace = mMainRect.height() / mGridRows
        for( i in 0.. mGridRows ){
            canvas.drawLine(0f,rowSpace * i+mMainRect.top as Float ,mWidth as Float, rowSpace * i + mMainRect.top as Float,mGridPaint )
        }
        //-----------------------下方子图-------------------------
        var columnSpace = mWidth / mGridColumns as Float
        for( i in 0..mGridColumns ){
            canvas.drawLine(columnSpace * i ,mMainRect.top as Float , columnSpace * i ,mMainRect.bottom as Float, mGridPaint)
        }
    }

    private fun calculateValue() {
        if(!isLongPress){
            mSelectedIndex = -1
        }
        mMainMaxValue = Float.MAX_VALUE
        mMainMinValue = Float.MIN_VALUE
        mChildMaxValue = Float.MAX_VALUE
        mChildMinValue = Float.MIN_VALUE
        mStartIndex = indexOfTranslateX(xToTranslateX(0))
        mStopIndex  = indexOfTranslateX(xToTranslateX(mWidth))

        for(i in mStartIndex..mStopIndex) {
           var point : IKLine = getItem(i) as IKLine
            if( mMainDraw != null){
                mMainMaxValue = Math.max(mMainMaxValue,mMainDraw.getMaxPoint(point))
                mMainMinValue = Math.min(mMainMinValue, mMainDraw.getMinPoint(point))
            }
            if(mChildDraw != null){
                mChildMaxValue = Math.max(mChildMaxValue , mChildDraw.getMaxPoint(point))
                mChildMinValue = Math.min(mChildMinValue,mChildDraw.getMinPoint(point))
            }
        }
        if(mMainMaxValue != mMainMinValue){
           var padding = (mMainMinValue - mMainMinValue) * 0.05f
            mMainMaxValue += padding
            mMainMinValue -= padding
        }else{
            //当最大值和最小值都相等的时候 分别增大最大值和 减小最小值
            mMainMaxValue += Math.abs(mMainMaxValue * 0.05f)
            mMainMinValue -= Math.abs(mMainMinValue * 0.05f)
            if( mMainMaxValue == 0f){
                mMainMaxValue == 1f
            }
        }
        mMainScaleY = mMainRect.height() * 1f /(mMainMaxValue - mMainMinValue)
        mChildScaleY = mChildRect.height() * 1f /(mChildMaxValue - mChildMinValue)
        if (mAnimator.isRunning){
            var value :Float = mAnimator.getAnimatedValue() as Float
            mStopIndex = (mStartIndex + Math.round(value * (mStopIndex - mStartIndex)))
        }
    }

    private fun getItem(position: Int): Any {
        if(iAdapter != null){
            return iAdapter.getItem(position)
        }else{
            return Any()//可能有问题
        }
    }

    private fun indexOfTranslateX(xToTranslateX: Float): Int {
        return indexOfTranslateX(translationX, 0 , mItemCount -1)
    }
    private fun indexOfTranslateX(xToTranslateX: Float,start : Int ,end :Int) : Int{
        if(end == start){
            return start
        }
        if(end - start == 1){
            var startValue = getX(start)
            var endValue = getX(end)
            return if (Math.abs(xToTranslateX - startValue) < Math.abs(xToTranslateX - endValue))  start else end
        }
        var mid = start + (end - start ) /2
        var midValue = getX(mid)
        if(translationX < midValue){
            return indexOfTranslateX(translationX,start,mid)
        }else if(translationX > midValue){
            return indexOfTranslateX(translationX, mid ,end)
        }else{
           return mid
        }
    }

    /**
     * 根据索引的位置，返回X的坐标
     */
    fun getX(position : Int ) : Float{
        return position * mPointWidth
    }

    private fun xToTranslateX(x: Int): Float {
        return -mTranslateX + x/mScaleX
    }
}