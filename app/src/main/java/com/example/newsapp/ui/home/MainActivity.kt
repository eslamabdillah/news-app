package com.example.newsapp.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.ui.home.news.NewsFragment

class MainActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, NewsFragment())
            .commit()

    }
}