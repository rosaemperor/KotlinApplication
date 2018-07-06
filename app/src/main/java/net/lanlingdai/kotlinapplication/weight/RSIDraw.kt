package net.lanlingdai.kotlinapplication.weight

import android.graphics.Canvas
import android.graphics.Paint

class RSIDraw : IChartDraw<IRSI>{
    var mRSI1Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var mRSI2Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var mRSI3Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun drawTranslated(lastPoint: IRSI, curPoint: IRSI, lastX: Float, curX: Float, canvas: Canvas, view: BaseKChartView, position: Int) {
        view.drawChildLine(canvas, mRSI1Paint , lastX , lastPoint.getRsi1() , curX,curPoint.getRsi1())
        view.drawChildLine(canvas , mRSI2Paint ,lastX , lastPoint.getRsi2() , curX , curPoint.getRsi2())
        view.drawChildLine(canvas , mRSI3Paint , lastX , lastPoint.getRsi3() ,curX ,curPoint.getRsi3())
    }

    override fun drawText(canvas: Canvas, view: BaseKChartView, position: Int, x: Float, y: Float) {
        var text = ""
        var targetX = x
        var point = view.getItem(position) as IRSI
        text = "RSI1:" + view.formatValue(point.getRsi1())
        canvas.drawText(text , targetX , y , mRSI1Paint)
        targetX += mRSI1Paint.measureText(text)
        text = "RSI2:" + view.formatValue(point.getRsi2())
        canvas.drawText(text , targetX ,y , mRSI2Paint)
        targetX += mRSI2Paint.measureText(text)
        text = "RSI3:" + view.formatValue(point.getRsi3())
        canvas.drawText(text , targetX ,y , mRSI3Paint)
    }

    override fun getMaxPoint(point: IRSI): Float {
        return Math.max(point.getRsi1() , Math.max(point.getRsi2() , point.getRsi3()))
    }

    override fun getMinPoint(point: IRSI): Float {
        return Math.min(point.getRsi1() , Math.min(point.getRsi2(), point.getRsi3()))
    }

    override fun getValueFormatter(): IValueFormatter {
        return ValueFormatter()
    }

    fun setRSI1Color(color : Int){
        mRSI1Paint.color = color
    }
    fun setRSI2Color (color: Int){
        mRSI2Paint.color = color
    }
    fun setRSI3Color (color: Int){
        mRSI3Paint.color =color
    }

    /**
     * 设置曲线宽度
     */
    fun setLineWidth(width : Float){
        mRSI1Paint.strokeWidth = width
        mRSI2Paint.strokeWidth = width
        mRSI3Paint.strokeWidth = width
    }
    /**
     * 设置文字大小
     */
    fun setTextSize(textSize : Float){
        mRSI1Paint.textSize = textSize
        mRSI2Paint.textSize = textSize
        mRSI3Paint.textSize = textSize
    }
}