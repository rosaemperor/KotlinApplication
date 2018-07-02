package net.lanlingdai.kotlinapplication.weight

interface IMACD {
    /**
     * DEAValue
     */
    fun getDea() : Float
    /**
     * DIF值
     */
    fun getDif() : Float
    /**
     * MACD值
     */
    fun getMacd() : Float
}