package com.example.newsapp.ui.fragments

//import NewsAdapter
import NewsAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.interfaces.ClickListener
import com.example.newsapp.models.Article
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.util.Resource
import com.example.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE

class BreakingNewsFragment: Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar

    private var TAG = "BreakinNewsFragment"



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewmodel

        recyclerView = view.findViewById(R.id.rvBreakingNews)
        progressBar = view.findViewById(R.id.paginationProgressBar)

        setupRecyclerView()

//        newsAdapter.setOnItemClickListener {
//
//        }

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages

                        if (isLastPage) {
                            recyclerView.setPadding(0,0,0,0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "an error occured : $message")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })


    }

//    private fun setupRecyclerView(){
//        newsAdapter = NewsAdapter(object : ClickListener{
//            override fun onClick(article: Article) {
//                val bundle = Bundle().apply {
//                    putSerializable("article", article)
//                }
//                findNavController().navigate(
//                    R.id.action_breakingNewsFragment2_to_articleFragment,
//                    bundle
//                )
//            }
//
//        })
//        recyclerView.apply {
//            adapter = newsAdapter
//            layoutManager = LinearLayoutManager(activity)
//        }
//    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount =  layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning  = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                viewModel.getBreakingNews("in")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }

    private fun hideProgressBar(){
        progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

}