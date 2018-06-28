package net.lanlingdai.kotlinapplication.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.lanlingdai.kotlinapplication.http.RetrofitUtils
import net.lanlingdai.kotlinapplication.interfaces.HttpHelp

abstract class BaseFragment : Fragment(){
    lateinit var httpHelp : HttpHelp

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getLifecycleObserver().setLifecycle(lifecycle)
        httpHelp = RetrofitUtils.instance.help
        return super.onCreateView(inflater, container, savedInstanceState)
    }
   abstract fun getLifecycleObserver() : BaseViewModel
}