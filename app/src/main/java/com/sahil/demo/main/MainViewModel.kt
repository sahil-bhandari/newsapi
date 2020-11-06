package com.sahil.demo.main

import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val redditRepository = NewsRepository()

    val posts = redditRepository.getPosts()

}