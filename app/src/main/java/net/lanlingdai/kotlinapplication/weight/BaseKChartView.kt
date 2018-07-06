package net.lanlingdai.kotlinapplication.weight

import android.animation.ValueAnimator
import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.view.GestureDetectorCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.AdapterViewAnimator
import net.lanlingdai.kotlinapplication.R
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseKChartView : ScrollAndScaleView{
    private var mTranslateX = Float.MIN_VALUE
    var mChildDrawPosition = 0
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
     var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
     var mSelectedLintPaint = Paint(Paint.ANTI_ALIAS_FLAG)
     var mSelectedIndex = 0
    private lateinit var mMainDraw : IChartDraw<Any>
     lateinit var iAdapter : IAdapter
    lateinit var mKChildTabView: KChartTabView

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
    private  lateinit  var  mChildDraw : IChartDraw<Any>
    private var  mChildDraws : ArrayList<IChartDraw<T>>  = ArrayList()

    private lateinit var mValueFormatter : IValueFormatter
    private lateinit var mDataFormatter : IDateTimeFormatter

//    protected lateinit var

    private lateinit var mAnimator: ValueAnimator
    private var mAnimatorDuration : Long = 500
    private var mOverScrollRange : Float = 0f
    private lateinit var mOnSelectedChangedListener: OnSelectedChangedListener

    private lateinit var mMainRect : Rect
     lateinit var mChildRect : Rect
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

    override fun getMinScrollX(): Int {
        return -(mOverScrollRange / mScaleX).toInt()
    }

    override fun getMaxScrollX(): Int {
        return Math.round(getMaxTranslateX() - getMinTranslateX())
    }

    /**
     * 获取平移的最大值
     */
    private fun getMaxTranslateX(): Float {
      return  if(!isFullScreen()) getMinTranslateX() else mPointWidth / 2
    }

    /**
     * 数据是否充满屏幕
     */
    private fun isFullScreen(): Boolean {
        return mDataLen >= mWidth / mScaleX
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

    /**
     *
     */
    private fun setTranslateXFromScrollX(scrollX : Int){
        mTranslateX = scrollX + getMinTranslateX()
    }

    /**
     * 获取平移的最小值
     */
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
            drawK(canvas)
            drawText(canvas)
            drawValue(canvas,if(isLongPress) mSelectedIndex else mStopIndex )
            canvas.restore()

        }

    }

    private fun drawValue(canvas: Canvas, position: Int) {
        var fontMetrics = mTextPaint.getFontMetrics()
        var textHeight = fontMetrics.descent - fontMetrics.ascent
        var baseLine = (textHeight - fontMetrics.bottom- fontMetrics.top )/2
        if(position >= 0 && position < mItemCount){
            var y = mMainRect.top + baseLine - textHeight
            var x = 0f
            mMainDraw .drawText(canvas,this, position, x, y)

            y = mChildRect.top + baseLine
            x = mTextPaint.measureText(mChildDraw.getValueFormatter().format(mChildMaxValue)+ "")
            mChildDraw.drawText(canvas,this,position,x,y)
        }

    }

    /**
     * 绘制文字部分
     */
     fun drawText(canvas: Canvas) {
        var fontMetrics = mTextPaint.fontMetrics
        var textHeight = fontMetrics.descent - fontMetrics.ascent
        var baseLine = (textHeight - fontMetrics.bottom - fontMetrics.top) /2 //计算得到文字的位置
        //---------------------  画上方K线图的值--------------
        canvas.drawText(formatValue(mMainMaxValue) ,0f,(baseline+mMainRect.top).toFloat(), mTextPaint)
        canvas.drawText(formatValue(mMainMinValue),0f,mMainRect.bottom - textHeight + baseLine ,mTextPaint)
        var rowValue = (mMainMaxValue - -mMainMinValue) / mGridRows
        var rowSpece = mMainRect.height()/ mGridRows
        for(i in 1.. mGridRows ){
            var text = formatValue(rowValue * (mGridRows - i ) + mMainMinValue)
            canvas.drawText(text,0f,fixTextY(rowSpece * i +mMainRect.top),mTextPaint)
        }
        //-----------------------绘制下方子图的值----------------------
        canvas.drawText(mChildDraw.getValueFormatter().format(mChildMaxValue),0f,mChildRect.top+baseLine,mTextPaint)
        canvas.drawText(mChildDraw.getValueFormatter().format(mChildMinValue),0f,mChildRect.bottom.toFloat(),mTextPaint)

        //-----------------------绘制时间线---------------------------
        var columnSpace = mWidth / mGridColumns
        var y = mChildRect.bottom + baseLine
        var startX = getX(mStartIndex) - mPointWidth / 2
        var stopX  = getX(mStopIndex) + mPointWidth / 2
        for(i in 1..mGridColumns ){
            var translateX = xToTranslateX(columnSpace * i)
            if (translateX >= startX && translateX <= stopX){
                var index = indexOfTranslateX(translateX)
                var text = formatDataTime(iAdapter.getDate(index))
                canvas.drawText(text,columnSpace * i - mTextPaint.measureText(text) /2 ,y, mTextPaint)
            }
        }
        var translateX = xToTranslateX(0)
        if(translateX >= startX && translateX <= stopX){
            canvas.drawText(formatDataTime(iAdapter.getDate(mStartIndex)),0f,y,mTextPaint)
        }
        translateX = xToTranslateX(mWidth)
        if(translateX >= startX && translateX <= stopX){
            var text = formatDataTime(iAdapter.getDate(mStopIndex))
            canvas.drawText(text,mWidth- mTextPaint.measureText(text),y ,mTextPaint)
        }
        if(isLongPress){
            var point = getItem(mSelectedIndex) as IKLine
            var text = formatValue(point.getClosePrice())
            var r = textHeight /2
            y = getMainY(point.getClosePrice())
            var x :Float = 0f
            if(translateXtoX(getX(mSelectedIndex)) < mWidth){
                x = 0f
                canvas.drawRect(x,y-r,mTextPaint.measureText(text),y+r,mBackgroundPaint)
            }else{
                x = mWidth - mTextPaint.measureText(text)
                canvas.drawRect(x,y-r,mWidth.toFloat(),y+r,mBackgroundPaint)
            }
            canvas.drawText(text,x,fixTextY(y.toInt()),mTextPaint)
        }

    }

     fun translateXtoX(x: Float): Float {
        return (mTranslateX +x) * mScaleX
    }



    /**
     * 格式化时间
     */
     fun formatDataTime(date: Date): String {
       return mDataFormatter.format(date)
    }


     fun formatValue(value: Float): String {
        return mValueFormatter.format(value)
    }

    /**
     * 绘制K线
     */
    private fun drawK(canvas: Canvas) {
        canvas.save()
        canvas.translate(mTranslateX*mScaleX,0f)
        canvas.scale(mScaleX.toFloat() , 1f)
        for (i in mStartIndex .. mStopIndex ){
            var currentPoint = getItem(i)
            var currentPointX = getX(i)
            var lastPoint = if(i == 0) currentPoint else getX(i-1)
            var lastX  = if(i == 0) currentPointX else getX(i-1)
            mMainDraw?.let {
                mMainDraw.drawTranslated(lastPoint,currentPoint,lastX,currentPointX,canvas,this,i)
            }
            mChildDraw.drawTranslated(lastPoint,currentPoint,lastX,currentPointX ,canvas ,this , i)
        }
        if(isLongPress){
            var point = getItem(mSelectedIndex) as IKLine
            var x = getX(mSelectedIndex)
            var y  = getMainY(point.getClosePrice())
            canvas.drawLine(x,mMainRect.top.toFloat(),x,mMainRect.bottom.toFloat(),mSelectedLintPaint)//绘制竖线
            canvas.drawLine(-mTranslateX,y,-mTranslateX+mWidth/mScaleX , y , mSelectedLintPaint) // 绘制横线
            canvas.drawLine(x,mChildRect.top.toFloat(),x,mChildRect.bottom.toFloat(),mSelectedLintPaint)
        }
    }

     fun getMainY(value : Float): Float {
        return (mMainMaxValue - value) * mMainScaleY + mMainRect.top
    }

    fun getChildY(value : Float) : Float{
        return (mMainMaxValue - value) * mChildScaleY + mChildRect.top
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

     fun getItem(position: Int): Any {
        if(iAdapter != null){
            return iAdapter.getItem(position)
        }else{
            return Any()//可能有问题
        }
    }

    private fun indexOfTranslateX(xToTranslateX: Float): Int {
        return indexOfTranslateX(xToTranslateX, 0 , mItemCount -1)
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

    /**
     * 文字剧中问题
     */
    private fun fixTextY(i: Int): Float {
        var fontMetrics = mTextPaint.getFontMetrics()
        return  y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent
    }

    fun dp2px(value : Float) : Int{
        var fontScale = context.resources.displayMetrics.scaledDensity
        return (value * fontScale + 0.5f).toInt()
    }

    override fun onLongPress(e: MotionEvent?) {
        super.onLongPress(e)
        e?.let {
            var lastIndex = mSelectedIndex
            calculateSelectedX(e.x)
            if (lastIndex != mSelectedIndex){
                onSelectedChanged(this,getItem(mSelectedIndex),mSelectedIndex)
            }
            invalidate()
        }

    }

     fun onSelectedChanged(baseKChartView: BaseKChartView, item: Any, mSelectedIndex: Int) {
        mOnSelectedChangedListener.onSelectedChanged(baseKChartView,item,mSelectedIndex)
    }

    private fun calculateSelectedX(x: Float) {
        mSelectedIndex = indexOfTranslateX(xToTranslateX(x.toInt()))
        if(mSelectedIndex < mStartIndex){
            mSelectedIndex = mStartIndex
        }
        if (mSelectedIndex > mStopIndex){
            mSelectedIndex = mStopIndex
        }
    }

    /**
     * 设置子图的绘制方法
     */
    fun setChildDraw(position: Int){
        this.mChildDraw = mChildDraws.get(position)
        mChildDrawPosition = position
        invalidate()
    }
    /**
     * 给子区域添加画图方法
     */
    fun<T> addChildDraw(name : String ,childDraw : IChartDraw<T>){
        mChildDraws.add(childDraw)
        mKChildTabView.addTab(name)
    }
    /**
     * 设置开始动画
     */
    fun startAnimation(){
        mAnimator.start()
    }
    /**
     * 设置动画时间
     */
    fun setAnimationDuration(duration : Long){
        mAnimator.setDuration(duration)
    }

    /**
     * 设置表格行数
     */
    fun setGridRows(gridRows : Int){
        if(gridRows<1)
            mGridRows= 1
        else mGridRows = gridRows

    }

    /**
     * 设置表格列数
     */
    fun setGridColumns(gridColumns : Int){
        if (gridColumns <1) mGridColumns = 1 else mGridColumns =gridColumns
    }

    /**
     * 获取上方的Padding
     */
    fun getTopPadding() : Float{
        return mTopPadding.toFloat()
    }

    /**
     * 获取图表的宽度
     */
    fun getChartWidth() : Int{
        return mWidth
    }
    /**
     * 设置表格线宽度
     */
    fun setGridLineWidth(width : Float){
        mGridPaint.strokeWidth = width
    }
    /**
     * 设置表格线颜色
     */
    fun setGridLineColor(color : Int){
        mGridPaint.color = color
    }
    /**
     * 设置文字颜色
     */
    fun setTextColor(color : Int){
        mTextPaint.color = color
    }
    /**
     * 设置文字大小
     */
    fun setTextSize(textSize : Float){
        mTextPaint.textSize = textSize
    }



    /**
     * 绘制子区域的线
     */
    fun drawChildLine(canvas: Canvas, paint: Paint, startX: Float, startValue: Float, stopX: Float, stopValue: Float) {
        canvas.drawLine(startX,getChildY(startValue),stopX, getChildY(stopValue), paint)
    }





}