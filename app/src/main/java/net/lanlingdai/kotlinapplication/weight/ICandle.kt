package net.lanlingdai.kotlinapplication.weight

interface ICandle{
    /**
     * 开盘价
     */
    fun getOpenPrice() : Float

    /**
     * 最高价
     */
    fun getHighPrice() : Float
    /**
     * 最低价
     */
    fun getLowPrice() : Float
    /**
     * 收盘价
     */
    fun getClosePrice() : Float
    /**
     * 五(月，日，时，分，5分等)均价
     */
    fun getMA5Price() : Float
    /**
     * 十(月，日，时，分，5分等)均价
     */
    fun getMA10Price() : Float
    /**
     * 二十(月，日，时，分，5分等)均价
     */
    fun getMA20Price() : Float

}