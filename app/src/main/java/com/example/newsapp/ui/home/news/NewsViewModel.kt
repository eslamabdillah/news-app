package com.example.newsapp.ui.home.news

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.modul.newsResponse.News
import com.example.newsapp.api.modul.newsResponse.NewsResponse
import com.example.newsapp.api.modul.newsResponse.Source
import com.example.newsapp.api.modul.sourcesRespose.SourcesResponse
import com.example.newsapp.ui.OnTryAgainClickListener
import com.example.newsapp.ui.ViewError
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {
    val shouldShowLoading = MutableLiveData<Boolean>()
    val sourcesLiveData = MutableLiveData<List<Source?>?>()
    val newsLiveData = MutableLiveData<List<News?>?>()
    val errorLiveData = MutableLiveData<ViewError>()

    fun getNewsSources() {
        //not use execute
        //viewBinding.progressBar.isVisible = true
        shouldShowLoading.postValue(true)

        ApiManager.getApi()
            .getSources()
            .enqueue(object : Callback<SourcesResponse> {
                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    //viewBinding.progressBar.isVisible = false
                    shouldShowLoading.postValue(false)

                    if (response.isSuccessful) {
                        //show tabs in fragment

                        sourcesLiveData.postValue(response.body()?.sources)
                        Log.d("sourcelist", sourcesLiveData.toString())
                        //bindTabs(sourcesList)


                    } else {
                        val errorBodyJsonString = response.errorBody()?.string()
                        val response =
                            Gson().fromJson(errorBodyJsonString, SourcesResponse::class.java)

                        errorLiveData.postValue(ViewError(message = response.message,
                            onTryAgainClickListener = OnTryAgainClickListener {
                                getNewsSources()
                            }

                        ))


//                        handleError(response.message) {
//                            getNewsSources()
//                        }


                    }


                }

                override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
//                    viewBinding.progressBar.isVisible = true
                    shouldShowLoading.postValue(false)
                    errorLiveData.postValue(ViewError(throwable = t,
                        onTryAgainClickListener = OnTryAgainClickListener {
                            getNewsSources()
                        }

                    ))
//                    handleError(t, {
//                        getNewsSources()
//                    })


                }

            })
    }

    fun getNews(sourceId: String?) {
        shouldShowLoading.postValue(true)
        ApiManager.getApi().getArticals(sources = sourceId ?: "")
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {

                    shouldShowLoading.postValue(false)
                    if (response.isSuccessful) {
                        //show news
                        newsLiveData.postValue(response.body()?.articles)
                        return
                    }
                    //convert error body to respose to get from it message
                    val responseJsonError = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(responseJsonError, NewsResponse::class.java)
                    errorLiveData.postValue(
                        ViewError(
                            message = errorResponse.message,
                            onTryAgainClickListener = {
                                getNews(sourceId)

                            })
                    )


                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    shouldShowLoading.postValue(false)
                    errorLiveData.postValue(ViewError(onTryAgainClickListener = object :
                        OnTryAgainClickListener {
                        override fun onTryAgainClick() {
                            getNews(sourceId)

                        }

                    }))

//


                }

            })

    }


}