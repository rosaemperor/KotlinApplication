package net.lanlingdai.kotlinapplication.weight

interface KChartRefreshListener {
    /**
     * 加载更多
     */
    fun onLoadMoreBegin(chart : KChartView)
}