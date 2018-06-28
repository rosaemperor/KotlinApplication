package net.lanlingdai.kotlinapplication.interfaces

import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap

interface HttpHelp{
    @POST("api/v1/auth?terminal="+"ANDROID")
fun getUserName (@Body userName : String ) : Call<Object>

    @POST("api/v1/users")
    abstract fun reginst(@Body user: String): Call<Object>
}

