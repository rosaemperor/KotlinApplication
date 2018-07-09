package net.lanlingdai.kotlinapplication.weight

import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.accessibility.AccessibilityRecordCompat.getMaxScrollX
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.OverScroller
import android.widget.RelativeLayout

abstract class  ScrollAndScaleView constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
    : RelativeLayout(context,attrs,defStyleAttr,defStyleRes) ,GestureDetector.OnGestureListener,
                           ScaleGestureDetector.OnScaleGestureListener{
    protected var mScrollX : Int = 0
    protected  var mDetector :GestureDetectorCompat
    protected  var mScaleDetector : ScaleGestureDetector
    private  var mScroller : OverScroller
     var isLongPress : Boolean = false
    protected var touch : Boolean = false
    protected var mMultipleTouch : Boolean = false
     var mScrollEnable : Boolean = true
     var mScaleEnable : Boolean = true
    protected var mScaleX  = 1
    protected var mScaleXMax = 2f
    protected var mScaleXMin  = 0.5f
    constructor(context: Context?) : this(context , null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr,0)

    init {
        setWillNotDraw(false)
        mDetector = GestureDetectorCompat(context, this)
        mScaleDetector = ScaleGestureDetector(context , this)
        mScroller = OverScroller(context)
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
             return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
            return false
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        if (! isTouch() && mScrollEnable){
            mScroller.fling(mScrollX,0,Math.round(velocityX/mScaleX),0,
                    Int.MIN_VALUE,Int.MAX_VALUE,0,0)
        }
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
          if (!isLongPress && !isMultipleTouch()){
              scrollBy(Math.round(distanceX),0)
              return true
          }
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
          isLongPress = true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
    }

    override fun onScale(detector: ScaleGestureDetector?): Boolean {
      if (!mScaleEnable){
          return false
      }
        var oldScale  = mScaleX
        mScaleX = detector!!.scaleFactor.toInt()
        if(mScaleX < mScaleXMin){
            mScaleX = mScaleXMin.toInt()
        }else if (mScaleX > mScaleXMax){
            mScaleX= mScaleXMax.toInt()
        }else{
            onScaleChanged(mScaleX,oldScale)
        }
        return true
    }

    private fun onScaleChanged(mScaleX: Int, oldScale: Int) {
        invalidate()

    }

    /**
     * 是否正在触摸中
     */
    fun isTouch() : Boolean{ return touch}

    override fun setScrollX(scrollX : Int){
        mScrollX = scrollX
        scrollTo(scrollX,0)
    }
    fun isMultipleTouch() : Boolean{
        return mMultipleTouch
    }

    override fun scrollBy(x: Int, y: Int) {
        scrollTo((mScrollX - Math.round((x / mScaleX).toDouble())).toInt(),0)    }

    override fun scrollTo(x: Int, y: Int) {
        if(!mScrollEnable){
            mScroller.forceFinished(true)
            return
        }
        var oldx = mScrollX
        mScrollX = x ;
        if(mScrollX < getMinScrollX()){
            mScrollX =getMinScrollX()
            onRightSide()
            mScroller.forceFinished(true)
        }else if(mScrollX > getMaxScrollX()){
            mScrollX = getMaxScrollX()
            onLeftSide()
            mScroller.forceFinished(true)
        }
        onScrollChanged(mScrollX,0,oldx,0)
        invalidate()
    }

    abstract fun onLeftSide()

    abstract fun onRightSide()

    abstract fun getMinScrollX(): Int

    abstract fun getMaxScrollX() : Int

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when(event.action and MotionEvent.ACTION_MASK){
                MotionEvent.ACTION_DOWN->
                        touch = true
                MotionEvent.ACTION_MOVE->
                        if (event.pointerCount == 1){
                            if (isLongPress){
                                onLongPress(event)
                            }
                        }
                MotionEvent.ACTION_POINTER_UP->
                        invalidate()
                MotionEvent.ACTION_UP->{
                    isLongPress = false
                    touch = false
                    invalidate()
                }
                MotionEvent.ACTION_CANCEL->{
                    isLongPress = false
                    touch = false
                    invalidate()
                }






        }
            mMultipleTouch = event.pointerCount>1
            mDetector.onTouchEvent(event)
            mScaleDetector.onTouchEvent(event)

        }
        return true


    }
    protected fun checkAndFixScrollX(){
        if (mScaleX < getMaxScrollX()){
            mScaleX = getMaxScrollX()
            mScroller.forceFinished(true)
        }else if(mScrollX > getMaxScrollX()){
            mScrollX = getMaxScrollX()
            mScroller.forceFinished(true)
        }
    }
}