package net.lanlingdai.kotlinapplication.viewmodels

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.ObservableField
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import io.reactivex.Observable
import net.lanlingdai.kotlinapplication.application.KotlinApplication
import net.lanlingdai.kotlinapplication.base.BaseViewModel
import net.lanlingdai.kotlinapplication.dao.StudentRespository
import net.lanlingdai.kotlinapplication.entity.Student
import net.lanlingdai.kotlinapplication.utils.DialogUtils
import java.util.*
import kotlin.math.log


class MainViewModel  :BaseViewModel(){
    lateinit var liveData : MutableLiveData<String>
    lateinit var respository : StudentRespository

    var num =0
    var steps = 0
    var numbers  = intArrayOf(90,20,78, 34, 12, 64, 5, 4, 62, 99, 98, 54, 56, 17, 18, 23, 34, 15, 35,101,120)
    fun clickHelloword(view : View){
        DialogUtils.showExitDialog(view.context)
        for (i in 0..numbers.size){
            for (n in i until numbers.size){
                if (numbers[i]<numbers[n]){
                    num=numbers[i]
                    numbers[i]=numbers[n]
                    numbers[n]= num

                }
            }
        }
        Log.d("numbers",""+Arrays.toString(numbers))
                Log.d("shellnumbers",""+Arrays.toString(shellSort(numbers)))

    }

    private fun shellSort(numbers: IntArray): IntArray {
        Log.d("numbersSize",""+numbers.size)
         steps = numbers.size
        var step = steps/2+steps%2
        for(u in steps downTo 1 step step){
            Log.d("number",""+steps+  "U:"+u)
            for (i in 0  .. numbers.size/2){
                if(numbers[i]>numbers[i+numbers.size/2]){
                    num= numbers[i]
                    numbers[i]=  numbers[i+numbers.size/2]
                    numbers[i+numbers.size/2]=num
                }
            }
            step = step/2
        }


        return numbers
    }

    override fun onResume(owner: LifecycleOwner) {

        super.onResume(owner)
    }
    fun insertStudent(view : View){
        respository = StudentRespository(view.context.applicationContext as KotlinApplication)
        var student : Student = Student()
        student.ID = 1
        var thread = Thread(){
            kotlin.run {
//                respository.insertOrUpdate(student)
                var students = respository.getStudents()

                Log.d("student",""+students.get(0).name)
            }
        }
        thread.start()
        liveData.value= "value"



    }

    override fun onCreate(owner: LifecycleOwner) {
        liveData = MutableLiveData()
        liveData.value = "livedata测试"
        var text = ObservableField<String>()

        var observer = Observer<String> {
            t: String? ->
            Log.d("TAG", "TAG:$t")
        }
        liveData.observe(owner,observer)
    }

    fun useRxJava(view : View){
        Toast.makeText(view.context,"useRxJava",Toast.LENGTH_LONG).show()
        var testText = ObservableField<String>()
        testText.set("test")
        var gson = Gson()
        var string = gson.toJson(testText)
        Log.d("TAG","$string")
    }




}