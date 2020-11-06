package com.sahil.demo.api

import Articles
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

class PostsDataSourceFactory: DataSource.Factory<String, Articles>() {

    private val sourceLiveData = MutableLiveData<PostsDataSource>()

    override fun create(): DataSource<String, Articles> {
        val source = PostsDataSource()
        sourceLiveData.postValue(source)
        return source
    }
}