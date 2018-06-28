package net.lanlingdai.kotlinapplication.viewmodels

import android.util.Log
import android.view.View
import android.widget.Toast
import net.lanlingdai.kotlinapplication.base.BaseViewModel
import java.util.*

class MainViewModel :BaseViewModel(){
    var num =0
    var steps = 0
    var numbers  = intArrayOf(90,20,78, 34, 12, 64, 5, 4, 62, 99, 98, 54, 56, 17, 18, 23, 34, 15, 35,101,120)
    fun clickHelloword(view : View){
        Toast.makeText(view.context,"toast",Toast.LENGTH_LONG).show()
        for (i in 0..numbers.size){
            for (n in i..numbers.size-1){
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
}