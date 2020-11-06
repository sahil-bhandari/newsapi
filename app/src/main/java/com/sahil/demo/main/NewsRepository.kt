package com.sahil.demo.main

import Articles
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.sahil.demo.api.PostsDataSourceFactory

class NewsRepository {

    private val sourceFactory = PostsDataSourceFactory()

    fun getPosts(): LiveData<PagedList<Articles>> {
        return sourceFactory.toLiveData(
            pageSize = 10
        )
    }
}