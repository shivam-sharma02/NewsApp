package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.models.Article

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
     RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)





    suspend fun searchNews(searchQueery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQueery, pageNumber)

    suspend fun upsert (article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}