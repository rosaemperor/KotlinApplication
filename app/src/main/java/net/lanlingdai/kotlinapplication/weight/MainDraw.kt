package net.lanlingdai.kotlinapplication.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import net.lanlingdai.kotlinapplication.R

class MainDraw : IChartDraw<ICandle>{
    override fun drawTranslated(lastPoint: ICandle, curPoint: ICandle, lastX: Float, curX: Float, canvas: Canvas, view: BaseKChartView, position: Int) {
        drawCandle(view,canvas,curX, curPoint.getHighPrice() ,curPoint.getLowPrice() ,curPoint.getOpenPrice(),curPoint.getClosePrice())
    }



    override fun drawText(canvas: Canvas, view: BaseKChartView, position: Int, x: Float, y: Float) {
        var point = view.getItem(position) as IKLine
        var text = "MA5："+view.formatValue(point.getMA5Price())
        canvas.drawText(text,x,y,ma5Paint)
         var targetX =x
        targetX += ma5Paint.measureText(text)
        text = "MA10:" +view.formatValue(point.getMA10Price())
        canvas.drawText(text, targetX, y ,ma10Paint)
        targetX +=ma10Paint.measureText(text)
        text = "MA20:" +view.formatValue(point.getMA20Price())
        canvas.drawText(text, targetX , y ,ma20Paint)
        if(view.isLongPress){
            drawSelector(view, canvas)
        }
    }

    override fun getMaxPoint(point: ICandle): Float {
        return Math.max(point.getHighPrice(),point.getMA20Price())
    }

    override fun getMinPoint(point: ICandle): Float {
        return Math.min(point.getMA20Price(), point.getLowPrice())
    }

    override fun getValueFormatter(): IValueFormatter {
        return ValueFormatter()
    }

    var mCandleWidth : Float = 0f
    var mCandleLineWidth : Float = 0f
    var mRedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var mGreenPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var ma5Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var ma10Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var ma20Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var mSelectorTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var mSelectorBackgroundPaint  = Paint(Paint.ANTI_ALIAS_FLAG)
    var context : Context
    var mCandleSolid = true
    constructor(view : BaseKChartView){
        context = view.context
        mRedPaint.color = ContextCompat.getColor(context , R.color.chart_red)
        mGreenPaint .color = ContextCompat.getColor(context , R.color.chart_green)
    }
    /**
     * 画Candle
     * @param canvas
     * @param x      x轴坐标
     * @param high   最高价
     * @param low    最低价
     * @param open   开盘价
     * @param close  收盘价
     */
     fun drawCandle(view: BaseKChartView, canvas: Canvas, x: Float, high: Float, low: Float, open: Float, close: Float) {
        var highPrice = high
        var lowPrice = low
        var openPrice = open
        var closePrice = close
        var r = mCandleWidth / 2
        var lineR = mCandleLineWidth /2
        if(open > close){
            if(mCandleSolid){
                canvas.drawRect(x-r , closePrice , x+r , openPrice , mRedPaint)
                canvas.drawRect(x - lineR , highPrice , x+lineR , lowPrice , mRedPaint)
            }else{
                mRedPaint.strokeWidth = mCandleLineWidth
                canvas.drawLine(x,highPrice, x ,closePrice ,mRedPaint)
                canvas.drawLine(x, openPrice , x, lowPrice , mRedPaint)
                canvas.drawLine(x-r+lineR ,openPrice , x-r +lineR ,closePrice , mRedPaint)
                canvas.drawLine(x+r-lineR , openPrice , x+r-lineR , closePrice ,mRedPaint)
                mRedPaint.strokeWidth = mCandleLineWidth * view.scaleX
                canvas.drawLine(x- r ,openPrice , x+r, open , mRedPaint)
                canvas.drawLine(x-r , closePrice , x+r ,closePrice , mRedPaint)

            }
        }else if (openPrice < closePrice){
            canvas.drawRect(x-r , openPrice, x+r ,closePrice , mGreenPaint)
            canvas.drawRect(x-lineR , high , x+lineR , lowPrice , mGreenPaint)

        }else{
            canvas.drawRect(x-r ,openPrice,x+r, close+1 , mRedPaint)
            canvas.drawRect(x-lineR, highPrice,x+lineR ,lowPrice ,mRedPaint)
        }
    }

    /**
     * draw选择器
     */
    fun drawSelector(view : BaseKChartView , canvas: Canvas){
        var metrics = mSelectorTextPaint.fontMetrics
        var textHeight = metrics.descent - metrics.ascent

        var index = view.mSelectedIndex
        var padding =ViewUtil.Dp2PX(context,5f)
        var marign = ViewUtil.Dp2PX(context, 5f)
        var width = 0f
        var left = 0f
        var top = marign+ view.getTopPadding()
        var height = padding * 8 + textHeight * 5
        var point = view.getItem(index) as ICandle
        var strings = ArrayList<String>()
        strings.add((view.formatDataTime(view.iAdapter.getDate(index))))
        strings.add("高："+point.getHighPrice())
        strings.add("低："+point.getLowPrice())
        strings.add("开："+point.getOpenPrice())
        strings.add("收："+point.getClosePrice())
        for(s  :String in strings){
            width =Math.max(width , mSelectorTextPaint.measureText(s))
        }
        width += padding*2

        var x = view.translateXtoX(view.getX(index))
        if( x > view.getChartWidth() / 2 ){
            left = marign.toFloat()
        }else{
            left = view.getChartWidth() - width - marign
        }

       var r = RectF(left,top, left+ width , top+height)
        canvas.drawRoundRect(r, padding.toFloat(), padding.toFloat() , mSelectorTextPaint)
        var  y = top + padding* 2 + (textHeight - metrics.bottom -metrics.top) / 2
        for (s:String in strings){
            canvas.drawText(s,left+padding , y ,mSelectorTextPaint)
            y += textHeight + padding
        }
    }
    /**
     * 设置蜡烛宽度
     */
    fun setCandleWidth(candleWidth : Float){
        mCandleWidth = candleWidth
    }
    /**
     * 设置蜡烛线宽度
     */
    fun setCandleLineWidth(candleLineWidth : Float){
        mCandleLineWidth = candleLineWidth
    }
    /**
     * Ma5颜色
     */
    fun setMa5Color(color : Int){
        ma5Paint.color = color
    }
    /**
     * 设置ma10颜色
     */
    fun setMa10Color(color : Int){
        ma10Paint.color = color
    }
    /**
     * 设置ma20颜色
     */
    fun setMa20Color( color: Int){
        ma20Paint.color = color
    }
    /**
     * 设置选择器文字颜色
     */
    fun setSelectorTextColor(color: Int){
        mSelectorTextPaint.color = color
    }
    /**
     * 设置文字大小
     */
    fun setSelecorTextSize(textSize : Float){
        mSelectorTextPaint.textSize = textSize
    }
    /**
     * 设置选择器背景
     */
    fun setSelectorBackgroundColro(color: Int){
        mSelectorBackgroundPaint.color= color
    }
    /**
     * 设置曲线宽度
     */
    fun setLineWidth(width : Float){
        ma20Paint.strokeWidth = width
        ma10Paint.strokeWidth = width
        ma5Paint.strokeWidth = width
    }
    /**
     * 设置文字大小
     */
     fun setTextSize(textSize: Float){
        ma5Paint.textSize= textSize
        ma10Paint.textSize = textSize
        ma20Paint.textSize =textSize
    }
    /**
     * 设置蜡烛是否空心
     */
    fun setCandleSolid(candleSolid : Boolean){
        mCandleSolid  = candleSolid
    }
}