package com.mlm4u.hamstudybuddy.data.remote

import com.mlm4u.hamstudybuddy.data.model.Root
import com.mlm4u.hamstudybuddy.data.model.VersionResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


const val BASE_URL = "https://api.mlm4u.eu"

private val logger = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val client = OkHttpClient.Builder()
    .addInterceptor(logger)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface QuestionRemoteApi {

    @GET("hamstudybuddy/fragenkatalog3b.json")
    suspend fun getQuestionsApi(): Root

    @GET("hamstudybuddy/version.json")
    suspend fun getVersionApi(): VersionResponse
}

object QuestionApi {
    val retrofitService: QuestionRemoteApi by lazy {
        retrofit.create(QuestionRemoteApi::class.java)
    }
}

