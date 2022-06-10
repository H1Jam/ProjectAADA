package com.hjam.aada.utils
import android.util.Log
import com.hjam.aada.BuildConfig

class Logger {
    companion object{
        fun debug(tag:String, message : String){
            if(BuildConfig.DEBUG){
                Log.d(tag,message)
            }
        }
        fun error(tag:String, message : String){
            if(BuildConfig.DEBUG){
                Log.e(tag,message)
            }
        }
    }
}