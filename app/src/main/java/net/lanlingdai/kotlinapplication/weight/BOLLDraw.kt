package net.lanlingdai.kotlinapplication.weight

import android.graphics.Canvas
import android.graphics.Paint

/**
 * BOLL实现类
 */
class BOLLDraw : IChartDraw<IBOLL>{
    var mUpPaint  = Paint(Paint.ANTI_ALIAS_FLAG)
    var mMbPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var mDnPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    constructor(view: BaseKChartView){

    }
    init {

    }
    override fun drawTranslated(lastPoint: IBOLL, curPoint: IBOLL, lastX: Float, curX: Float, canvas: Canvas, view: BaseKChartView, position: Int) {
        view.drawChildLine(canvas , mUpPaint ,lastX , lastPoint.getUp() , curX ,curPoint.getUp())
        view.drawChildLine(canvas , mMbPaint , lastX , lastPoint.getMb() ,curX , curPoint.getMb())
        view.drawChildLine(canvas , mDnPaint , lastX ,lastPoint.getDn() , curX , curPoint.getDn())
    }

    override fun drawText(canvas: Canvas, view: BaseKChartView, position: Int, x: Float, y: Float) {
        var text =""
        var  targetX = x
        var point  =  view.getItem(position)as IBOLL
        text = "UP:" + view.formatValue(point.getUp())
        canvas.drawText(text,targetX, y , mUpPaint)
        targetX += mUpPaint.measureText(text)
        text = "MB:" + view.formatValue(point.getMb())
        canvas.drawText(text ,targetX , y ,mMbPaint)
        targetX  += mMbPaint.measureText(text)
        text = "DN:" + view.formatValue(point.getDn())
        canvas.drawText(text ,targetX, y ,mDnPaint)
    }

    override fun getMaxPoint(point: IBOLL): Float {
        if (Float.NaN != point.getUp()){
            return point.getMb()
        }
        return point.getUp()
    }

    override fun getMinPoint(point: IBOLL): Float {
        if(Float.NaN != point.getDn()){
            return point.getMb()
        }
        return point.getDn()
    }

    override fun getValueFormatter(): IValueFormatter {
        return ValueFormatter()
    }
    /**
     * 设置up颜色
     */
    fun setUpColor(color : Int){
        mUpPaint.color = color
    }
    /**
     * 设置MB颜色
     */
    fun setMbColor(color: Int ){
        mMbPaint.color = color
    }
    /**
     * 设置DN颜色
     */
    fun setDnColor(color: Int){
        mDnPaint.color = color
    }
    /**
     * 设置曲线宽度
     */
    fun setLineWidth(width : Float){
        mDnPaint.strokeWidth =width
        mMbPaint.strokeWidth = width
        mDnPaint.strokeWidth  = width
    }


}