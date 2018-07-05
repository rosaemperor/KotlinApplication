package net.lanlingdai.kotlinapplication.weight

class ValueFormatter : IValueFormatter{
    override fun format(value: Float): String {
        return String.format("5.2f",value)
    }
}