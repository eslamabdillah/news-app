package com.example.newsapp.ui.home.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.api.modul.newsResponse.News
import com.example.newsapp.api.modul.newsResponse.Source
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.ui.ViewError
import com.example.newsapp.ui.showMessage
import com.google.android.material.tabs.TabLayout

class NewsFragment : Fragment() {
    lateinit var viewBinding: FragmentNewsBinding
    lateinit var viewModel: NewsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentNewsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initView()
        viewModel.getNewsSources()
    }

    val adapter = NewsAdapter()
    private fun initView() {
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
        viewBinding.recycler.adapter = adapter
    }

    private fun initObservers() {
//        viewModel.shouldShowLoading.observe(viewLifecycleOwner, object : Observer<Boolean> {
//            override fun onChanged(isvisivle: Boolean) {
//                viewBinding.progressBar.isVisible = isvisivle
//            }
//        })

        viewModel.sourcesLiveData.observe(viewLifecycleOwner, object : Observer<List<Source?>?> {
            override fun onChanged(sources: List<Source?>?) {
                bindTabs(sources)
            }

        })

        viewModel.newsLiveData.observe(viewLifecycleOwner, object : Observer<List<News?>?> {
            override fun onChanged(artical: List<News?>?) {
                adapter.bindNews(artical)


            }

        })

        viewModel.errorLiveData.observe(viewLifecycleOwner, object : Observer<ViewError> {
            override fun onChanged(viewerror: ViewError) {
                handleError(viewerror)
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
                    viewModel.getNews(source.id)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    val source = tab?.tag as Source
                    viewModel.getNews(source.id)
                }

            }
        )
        viewBinding.tabLayout.getTabAt(0)?.select()


    }


    fun handleError(viewError: ViewError) {
        showMessage(message = viewError.message ?: viewError.throwable?.message
        ?: "something wromg sorry",
            posActionName = "try again",
            postAction = { dialogInterface, i ->
                dialogInterface.dismiss()
                viewError.onTryAgainClickListener?.onTryAgainClick()
            },
            negActionName = "cancel",
            negAction = { dialogInterface, i ->
                dialogInterface.dismiss()
            }
        )

    }


}


