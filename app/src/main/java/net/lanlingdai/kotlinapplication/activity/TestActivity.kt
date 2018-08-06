package net.lanlingdai.kotlinapplication.activity

import net.lanlingdai.kotlinapplication.base.BaseActivity
import net.lanlingdai.kotlinapplication.base.BaseViewModel
import net.lanlingdai.kotlinapplication.viewmodels.TestViewModel

class TestActivity : BaseActivity(){
    private lateinit var viewModel : TestViewModel
    override fun initViewModel() {
        viewModel = TestViewModel()
        viewModel.setLifecycle(lifecycle)
    }


    override fun getLifecycleObserver(): BaseViewModel {
        return viewModel
    }


}