package com.example.newsapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.ui.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ArticleFragment: Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    lateinit var webView: WebView
    lateinit var floatingAction: FloatingActionButton
    val args: ArticleFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewmodel

        webView = view.findViewById(R.id.webView)
        floatingAction = view.findViewById(R.id.fab)

        val article = args.article
        Log.e("ARTICLE", article?.url + "    ******* ")

        webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        floatingAction.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }
    }
}