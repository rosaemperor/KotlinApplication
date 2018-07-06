package net.lanlingdai.kotlinapplication.weight

interface IVolume {
    /**
     * 开盘价
     */
    fun getOpenPrice() : Float
    /**
     * 收盘价
     */
    fun getClosePrice() : Float
    /**
     * 成交量
     */
    fun getVolume() :Float
    /**
     * 五分图（月，日，时，分，5分等）
     */
    fun getMA5Volume() :Float
    /**
     * 十分图（月，日，时，分，5分等）
     */
    fun getMA10Volume() : Float
}