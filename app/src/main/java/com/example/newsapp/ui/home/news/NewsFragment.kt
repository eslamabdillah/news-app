package com.example.newsapp.ui.home.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.modul.newsResponse.NewsResponse
import com.example.newsapp.api.modul.newsResponse.Source
import com.example.newsapp.api.modul.sourcesRespose.SourcesResponse
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.ui.showMessage
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {
    lateinit var viewBinding: FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentNewsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getNewsSources()
    }

    val adapter = NewsAdapter()
    private fun initView() {
        viewBinding.recycler.adapter = adapter
    }

    private fun getNewsSources() {
        //not use excute
        viewBinding.progressBar.isVisible = true

        ApiManager.getApi()
            .getSources()
            .enqueue(object : Callback<SourcesResponse> {
                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    viewBinding.progressBar.isVisible = false
                    if (response.isSuccessful) {
                        //show tabs in fragment

                        var sourcesList = response.body()?.sources
                        Log.d("sourcelist", sourcesList.toString())
                        bindTabs(sourcesList)


                    } else {
                        val errorBodyJsonString = response.errorBody()?.string()
                        val response =
                            Gson().fromJson(errorBodyJsonString, SourcesResponse::class.java)
                        handleError(response.message) {
                            getNewsSources()
                        }


                    }


                }

                override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                    viewBinding.progressBar.isVisible = true
                    handleError(t, {
                        getNewsSources()
                    })


                }

            })
    }

    private fun bindTabs(sourcesList: List<Source?>?) {
        if (sourcesList == null) return
        sourcesList.forEach { source ->
            val tab = viewBinding.tabLayout.newTab()
            tab.text = source?.name
            tab.tag = source
            Log.d("onesource", source?.id.toString())
            viewBinding.tabLayout.addTab(tab)
        }




        viewBinding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val source = tab?.tag as Source
                    getNews(source.id)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    val source = tab?.tag as Source
                    getNews(source.id)
                }

            }
        )
        viewBinding.tabLayout.getTabAt(0)?.select()


    }

    private fun getNews(sourceId: String?) {
        viewBinding.progressBar.isVisible = true
        ApiManager.getApi().getArticals(sources = sourceId ?: "")
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {

                    viewBinding.progressBar.isVisible = false
                    if (response.isSuccessful) {
                        //show news

                        adapter.bindNews(response.body()?.articles)
                        return
                    }
                    //convert error body to respose to get from it message
                    val responseJsonError = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(responseJsonError, NewsResponse::class.java)
                    handleError(message = errorResponse.message) {
                        getNews(sourceId)
                    }

                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    viewBinding.progressBar.isVisible = false
                    handleError(t, {
                        getNews(sourceId)
                    })


                }

            })

    }


    fun interface OnTryAgainClickListener {
        fun onTryAgainClick()

    }

    fun handleError(t: Throwable, onClick: OnTryAgainClickListener) {
        showMessage(message = t.message.toString(),
            posActionName = "try again",
            postAction = { dialogInterface, i ->
                dialogInterface.dismiss()
                onClick.onTryAgainClick()
            },
            negActionName = "cancel",
            negAction = { dialogInterface, i ->
                dialogInterface.dismiss()
            }
        )

    }

    fun handleError(message: String?, onClick: OnTryAgainClickListener) {
        showMessage(message ?: "something is wrong",
            posActionName = "try again",
            postAction = { dialogInterface, i ->
                dialogInterface.dismiss()
                onClick.onTryAgainClick()
            },
            negActionName = "cancel",
            negAction = { dialogInterface, i ->
                dialogInterface.dismiss()
            }
        )
    }


}


