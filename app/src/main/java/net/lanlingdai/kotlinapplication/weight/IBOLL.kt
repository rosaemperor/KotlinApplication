package net.lanlingdai.kotlinapplication.weight

interface IBOLL {
    /**
     * 上轨线
     */
    fun getUp() : Float

    /**
     * 中轨线
     */
    fun getMb() : Float

    /**
     * 下轨线
     */
    fun GetDn() : Float
}