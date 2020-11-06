package com.sahil.demo.api

import NewsPost
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface APIInterface {

    @GET("v2/top-headlines")
    fun getTop(
        @Header("Authorization") auth: String ,
        @Query("country") country:String ,
        @Query("category") category:String ,
        @Query("pageSize") pageSize:Int ,
        @Query("page") page:Int ): Call<ResponseBody>

    @GET("v2/top-headlines")
    fun getTopAfter(
        @Header("Authorization") auth: String ,
        @Query("country") country:String ,
        @Query("category") category:String  ,
        @Query("pageSize") pageSize:Int,
        @Query("page") page:Int): Call<ResponseBody>

}