package id.ac.narotama.mo.data

import id.ac.narotama.mo.data.remote.APIEndpoint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkUtil {

    fun getRetrofit() = Retrofit
        .Builder()
        .baseUrl("http://80.211.184.148/pku/")
        .client(OkHttpClient
            .Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getApi() = getRetrofit().create(APIEndpoint::class.java)

}