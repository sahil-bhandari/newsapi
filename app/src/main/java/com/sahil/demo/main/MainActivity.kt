package com.sahil.demo.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.sahil.demo.R
import com.sahil.demo.utility.misc
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator

class MainActivity: AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var posts_recycler:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: MainViewModel by viewModels()
        this.viewModel = viewModel

        if (misc.isOnline()){
            observePosts()
        } else{
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show()
        }

    }

    private fun observePosts() {
        posts_recycler=findViewById(R.id.posts_recycler)
        val adapter = PostsAdapter()
        posts_recycler.adapter = adapter
        posts_recycler.itemAnimator= SlideInDownAnimator().apply {
            addDuration = 700
            removeDuration = 100
            moveDuration = 700
            changeDuration = 700
        }
        viewModel.posts.observe(this, Observer {
            adapter.submitList(it) {}
        })
    }

//    override fun onClick(Item: Articles?) {
//        val intent = Intent(this@MainActivity, WebViewActivity::class.java)
//        val bundle = Bundle()
//        bundle.putString("link", Item!!.url)
//        intent.putExtras(bundle)
//        startActivity(intent)
//    }


}
