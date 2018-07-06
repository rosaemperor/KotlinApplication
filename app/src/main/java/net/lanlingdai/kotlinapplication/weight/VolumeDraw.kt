package net.lanlingdai.kotlinapplication.weight

import android.graphics.Canvas
import android.graphics.Paint

class VolumeDraw : IChartDraw<IVolume>{
    var mRedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var mGreenPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var ma5Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var ma10Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var pillarWidth = 0
    constructor(view : BaseKChartView){

    }
    override fun drawTranslated(lastPoint: IVolume, curPoint: IVolume, lastX: Float, curX: Float, canvas: Canvas, view: BaseKChartView, position: Int) {
        drawHistogram(canvas ,curPoint , lastPoint, curX , view , position)
    }

    private fun drawHistogram(canvas: Canvas, curPoint: IVolume, lastPoint: IVolume, curX: Float, view: BaseKChartView, position: Int) {
        var r = pillarWidth /2
        var top  = view.getChildY(curPoint.getVolume())
        var bottom = view.mChildRect.bottom
        if(curPoint.getClosePrice() >- curPoint.getOpenPrice()){
            canvas.drawRect(curX -r ,top , curX+ r, bottom.toFloat() ,mRedPaint)
        }else{
            canvas.drawRect(curX - r ,top , curX+r ,bottom.toFloat() , mGreenPaint)
        }
    }

    override fun drawText(canvas: Canvas, view: BaseKChartView, position: Int, x: Float, y: Float) {
        var point = view.getItem(position) as IVolume
        var text = ""
        var targetX = x
        text = "VOL:"+view.formatValue(point.getVolume())
        canvas.drawText(text ,targetX ,y , view.mTextPaint)
        targetX += view.mTextPaint.measureText(text)
        text = "MA5:"+ view.formatValue(point.getMA5Volume())
        canvas.drawText(text , targetX ,y, ma5Paint)
        targetX += ma5Paint.measureText(text)
        text = "MA10:" +view.formatValue(point.getMA10Volume())
        canvas.drawText(text , targetX ,y ,ma10Paint)
    }

    override fun getMaxPoint(point: IVolume): Float {
        return Math.max(point.getVolume() , Math.max(point.getMA5Volume() , point.getMA10Volume()))
    }

    override fun getMinPoint(point: IVolume): Float {
        return Math.min(point.getVolume(), Math.min(point.getMA5Volume(), point.getMA10Volume()))
    }

    override fun getValueFormatter(): IValueFormatter {
        return ValueFormatter()
    }
    /**
     *  设置MA5线的颜色
     */
    fun setMa5Color(color :Int){
        ma5Paint.color= color
    }
    /**
     *  设置MA10线的颜色
     */
    fun setMa10Color(color: Int){
        ma10Paint.color =color
        ma5Paint.color =color
    }
    /**
     * 设置字体大小
     */
    fun setTextSize(textSize : Float){
        ma5Paint.textSize = textSize
        ma10Paint.textSize = textSize
    }
}