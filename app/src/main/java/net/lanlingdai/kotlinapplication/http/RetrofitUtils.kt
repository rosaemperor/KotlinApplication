package net.lanlingdai.kotlinapplication.http

import android.util.Log
import net.lanlingdai.kotlinapplication.interfaces.HttpHelp
import okhttp3.Interceptor

import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitUtils private constructor() {
    lateinit var retrofit: Retrofit
    lateinit var help: HttpHelp
        private set
    private var client: OkHttpClient? = null
    private var loggingInterceptor: HttpLoggingInterceptor? = null

    init {
        init()
    }

    private fun init() {
        loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor!!.setLevel(HttpLoggingInterceptor.Level.BODY)
        client = OkHttpClient.Builder().addInterceptor(object : Interceptor{
            override fun intercept(chain: Interceptor.Chain?): Response {
                val response = chain!!.proceed(chain.request())
                Log.d("TAG", "" + response.body()!!.string())
                val responseBody = response.body()

                val request = chain.request().newBuilder()
                        .addHeader("Content-Type", "text/html; charset=UTF-8")
                        .build()
                Log.d("TAG", "" + request.url().toString())
                return chain.proceed(request)            }
        })
                .addInterceptor(loggingInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(3, TimeUnit.SECONDS)
                .build()


        retrofit = Retrofit.Builder()
                //                .baseUrl( "http://api.ih2ome.cn/")
                .baseUrl("http://10.0.6.47:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        help = retrofit.let { retrofit.create(HttpHelp::class.java) }

    }

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }

    companion object {
        val instance = RetrofitUtils()
    }
}