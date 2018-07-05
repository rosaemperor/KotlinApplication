package net.lanlingdai.kotlinapplication.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import net.lanlingdai.kotlinapplication.R

class MACDDraw : IChartDraw<IMACD>{


    var view : BaseKChartView
    var context : Context
    var mRedPaint  = Paint()
    var mGreenPaint = Paint()
    var mDIFPaint = Paint()
    var mDEAPaint = Paint()
    var mMACDPaint = Paint()
    var targetX :Float = 0f
    var mMACDWidth = 0f

    override fun getMaxPoint(point: IMACD): Float {
        return Math.max(point.getMacd() , Math.max(point.getDea() , point.getDif()))
    }

    override fun getMinPoint(point: IMACD): Float {
        return Math.min(point.getMacd(),Math.min(point.getDea() , point.getDif()))
    }

    override fun drawTranslated(lastPoint: IMACD, curPoint: IMACD, lastX: Float, curX: Float, canvas: Canvas, view: BaseKChartView, position: Int) {
        drawMACD(canvas,view,curX,curPoint.getMacd())
        view.drawChildLine(canvas,mDIFPaint,lastX,lastPoint.getDea(),curX,curPoint.getDea())
        view.drawChildLine(canvas,mDEAPaint,lastX,lastPoint.getDif(),curX, curPoint.getDif())
    }
    /**
     * 画macd
     * @param canvas
     * @param x
     * @param macd
     */
    private fun drawMACD(canvas: Canvas, view: BaseKChartView, x: Float, macd: Float) {
        var macdy = view.getChildY(macd)
        var r = mMACDWidth / 2
        var zeroy = view.getChildY(0f)
        if(macd > 0){
            canvas.drawRect(x - r , macdy , x+r , zeroy , mRedPaint)
        }else {
            canvas.drawRect(x - r, zeroy , x+r , macdy ,mGreenPaint)
        }
    }

    override fun drawText(canvas: Canvas, view: BaseKChartView, position: Int, x: Float, y: Float) {
        var text = ""
        targetX = x
        var point :IMACD = view.getItem(position) as IMACD
        text = "DIF:" + view.formatValue(point.getDif()) + ""
        canvas.drawText(text, x ,y ,mDEAPaint)
        targetX += mDIFPaint.measureText(text)
        text = "DEA:" +view.formatValue(point.getDea()) +""
        canvas.drawText(text , targetX , y ,mDIFPaint)
        targetX  += mDEAPaint.measureText(text)
        text = "MACD:" + view.formatValue(point.getMacd())+""
        canvas.drawText(text , targetX , y , mMACDPaint)

    }

    override fun getValueFormatter(): IValueFormatter {
        return ValueFormatter()
    }
    constructor(baseView : BaseKChartView){
        this.view = baseView
        context = view.context
        mRedPaint.setColor(ContextCompat.getColor(context,R.color.chart_red))
        mGreenPaint.setColor(ContextCompat.getColor(context,R.color.chart_green))
    }
    init {
    }
    /**
     * 设置DIF颜色
     */
    fun setDIFColor(color : Int){
        this.mDIFPaint.color = color
    }
    /**
     * 设置DEA颜色
     */
    fun setDEAColor(color: Int){
        this.mDEAPaint.color = color
    }
    /**
     * 设置MACD颜色
     */
    fun setMACDColor(color: Int){
        this.mMACDPaint.color = color
    }
    /**
     * 设置MACD的宽度
     */
    fun setMACDWidth(MACDWidth : Float){
        this.mMACDWidth = MACDWidth
    }
    /**
     * 设置曲线的宽度
     */
    fun setLineWidth(width : Float){
        mDEAPaint.strokeWidth = width
        mDIFPaint.strokeWidth = width
        mMACDPaint.strokeWidth = width
    }
    /**
     * 设置文字大小
     */
    fun setTextSize(textSize : Float){
        mDEAPaint.textSize =textSize
        mDIFPaint.textSize = textSize
        mMACDPaint.textSize = textSize
    }

}