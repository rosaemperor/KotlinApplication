package net.lanlingdai.kotlinapplication.weight

import android.graphics.Canvas
import android.graphics.Paint

class KDJDraw : IChartDraw<IKDJ>{
    var mKPaint= Paint(Paint.ANTI_ALIAS_FLAG)
    var mDPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var mJPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    constructor(view : BaseKChartView){

    }
    override fun drawTranslated(lastPoint: IKDJ, curPoint: IKDJ, lastX: Float, curX: Float, canvas: Canvas, view: BaseKChartView, position: Int) {
        view.drawChildLine(canvas , mKPaint , lastX ,curPoint.getK() , curX ,curPoint.getK())
        view.drawChildLine(canvas , mDPaint , lastX ,curPoint.getD() , curX ,curPoint.getD())
        view.drawChildLine(canvas , mJPaint , lastX ,curPoint.getJ() ,curX ,curPoint.getJ())
    }

    override fun drawText(canvas: Canvas, view: BaseKChartView, position: Int, x: Float, y: Float) {
        var text =""
        var targetX = x
        var point= view.getItem(position) as IKDJ
        text = "K:" +view.formatValue(point.getK())
        canvas.drawText(text , targetX, y ,mKPaint)
        targetX += mKPaint.measureText(text)
        text = "D:"+view.formatValue(point.getD())
        canvas.drawText(text ,targetX ,y ,mDPaint)
        targetX += mDPaint.measureText(text)
        text = "J:" + view.formatValue(point.getJ())
        canvas.drawText(text , targetX ,y , mJPaint)
    }

    override fun getMaxPoint(point: IKDJ): Float {
        return Math.max(point.getD() , Math.max(point.getJ() , point.getK()))
    }

    override fun getMinPoint(point: IKDJ): Float {
        return Math.min(point.getD() , Math.min(point.getJ() , point.getK()))    }

    override fun getValueFormatter(): IValueFormatter {
        return ValueFormatter()
    }
    /**
     *设置K线的颜色
     */
     fun setKColor(color: Int ){
        mKPaint.color = color
    }
    /**
     *  设置D线的颜色
     */
    fun setDColor(color: Int){
        mDPaint.color = color
    }
    /**
     *  设置J线的颜色
     */
    fun setJColor(color: Int){
        mJPaint.color = color
    }
    /**
     * 设置曲线的宽度
     */
    fun setLineWidth(width : Float){
        mJPaint.strokeWidth = width
        mDPaint.strokeWidth = width
        mKPaint.strokeWidth = width
    }
    /**
     * 设置字体的大小
     */
    fun setTextSize(textSize : Float){
        mJPaint.textSize = textSize
        mDPaint.textSize = textSize
        mKPaint.textSize = textSize
    }
}