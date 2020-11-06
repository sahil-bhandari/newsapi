package com.sahil.demo.api

import Articles
import NewsPost
import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.google.gson.Gson
import com.sahil.demo.BuildConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostsDataSource: PageKeyedDataSource<String, Articles>() {

    private val Api: APIInterface = Retrofit.Builder()
        .baseUrl(BuildConfig.BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(APIInterface::class.java)

    var page=1;


    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Articles>) {

        Api.getTop("Bearer "+ BuildConfig.APIKEY,"in","general",10,1).enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>,response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val data = Gson().fromJson(response.body()?.string(), NewsPost::class.java)
                    Log.d("onResponse", "onResponse: "+data)
                    callback.onResult(data.articles, page.toString(), (page+1).toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Articles>) {

        if(page<3){
            page+=page
            Api.getTopAfter("Bearer "+ BuildConfig.APIKEY,"in","general",params.requestedLoadSize,params.key.toInt()).enqueue(object: Callback<ResponseBody> {

                override fun onResponse(call: Call<ResponseBody>,response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val data = Gson().fromJson(response.body()?.string(), NewsPost::class.java)
                        Log.d("onResponse", "onResponse: "+data)
                        callback.onResult(data.articles, page.toString())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

    }


    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Articles>) {
        Log.d("TAG", "loadBefore: $params")
    }

}