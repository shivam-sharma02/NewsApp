package com.example.newsapp.interfaces

import com.example.newsapp.models.Article

interface ClickListener {
    fun onClick(article: Article)
}