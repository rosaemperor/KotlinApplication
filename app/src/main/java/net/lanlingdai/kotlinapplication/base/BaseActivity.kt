package net.lanlingdai.kotlinapplication.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import net.lanlingdai.kotlinapplication.application.KotlinApplication
import net.lanlingdai.kotlinapplication.http.RetrofitUtils
import net.lanlingdai.kotlinapplication.interfaces.HttpHelp

abstract class BaseActivity : AppCompatActivity(){
    lateinit var httpHelp : HttpHelp
    lateinit var  application : KotlinApplication
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        getLifecycleObserver().setLifecycle(lifecycle)
        httpHelp = RetrofitUtils.instance.help
        application = KotlinApplication.instance
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    abstract fun getLifecycleObserver() : BaseViewModel

    abstract fun initViewModel()
}