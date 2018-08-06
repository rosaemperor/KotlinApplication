package net.lanlingdai.kotlinapplication.base

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel

abstract class BaseViewModel  : ViewModel() , DefaultLifecycleObserver{

    fun setLifecycle(lifecycle : Lifecycle){
        lifecycle.addObserver(this)
    }




}