package net.lanlingdai.kotlinapplication

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import net.lanlingdai.kotlinapplication.databinding.ActivityMainBinding
import net.lanlingdai.kotlinapplication.utils.DialogUtils
import net.lanlingdai.kotlinapplication.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() , View.OnClickListener {
    lateinit var view :View
    lateinit var viewModel : MainViewModel
    var firstClickTime : Long = 0
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewmodel = viewModel
        // Example of a call to a native method
        view  = View(this)
         Log.d("view",""+view.id)
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun stringMyJNI() :String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun onClick(v: View?) {
        when(v?.let { v.id }){
            R.id.sample_text->{
               DialogUtils.showExitDialog(this)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

//            if(event!!.action == KeyEvent.KEYCODE_BACK){
//                DialogUtils.showExitDialog(this)
//                return true
//            }else{
//                return super.onKeyDown(keyCode, event)
//            }


    }

}
