package net.lanlingdai.kotlinapplication.weight

import android.content.Context

class ViewUtil {
    companion object {
        fun Dp2PX(context: Context? , dp : Float) : Int{
            var scale : Float = context!!.resources.displayMetrics.density
            return (dp* scale + 0.5f).toInt()
        }

    }
}