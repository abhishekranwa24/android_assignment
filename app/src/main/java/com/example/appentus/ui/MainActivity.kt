package com.example.appentus.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appentus.Adapter.ImageAdapter
import com.example.appentus.Adapter.ImageLoadStateAdapter
import com.example.appentus.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    @Inject
    lateinit var imageAdapter: ImageAdapter
    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intiRecyclerView()
        setupList()



    }

    @ExperimentalPagingApi
    private  fun onstart(){
        lifecycleScope.launchWhenStarted {
            mainViewModel.getAllImages().collectLatest { response->
                binding.apply {
                    // after getting data hide swipe refresh bar and data
                    recyclerview.isVisible = true
                    swipeRefreshLayout.isRefreshing =false
                }
                imageAdapter.submitData(response)
            }
        }
    }

    private fun setupList() {
        imageAdapter = ImageAdapter()
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = imageAdapter.withLoadStateHeaderAndFooter(
                header = ImageLoadStateAdapter { imageAdapter.retry() },
                footer = ImageLoadStateAdapter { imageAdapter.retry() }
            )
            setHasFixedSize(true)
        }
    }
    @ExperimentalPagingApi
    private fun intiRecyclerView() {
        binding.apply {
            // Swipe Refersh Bar for Refreshing on App Start
            swipeRefreshLayout.setOnRefreshListener { onstart()}
            recyclerview.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(this@MainActivity,2)
                adapter = imageAdapter
            }
        }
    }
}