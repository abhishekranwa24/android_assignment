package com.example.appentus.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appentus.databinding.ItemLoadingStateBinding

class ImageLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ImageLoadStateAdapter.ImageLoadStateViewHolder>() {
    // LoadState to present in the adapter.
    // Changing this property will immediately notify the Adapter to change the item it's presenting.
    inner class ImageLoadStateViewHolder(
            private val binding: ItemLoadingStateBinding,
            private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.textViewError.text = "No Internet Connection"
            }
            // error Handling for the Bottom Progress Bar and Button
            binding.progressbar.visible(loadState is LoadState.Loading)
            binding.buttonRetry.visible(loadState is LoadState.Error)
            binding.textViewError.visible(loadState is LoadState.Error)
            binding.buttonRetry.setOnClickListener {
                retry()
            }

            binding.progressbar.visibility = View.VISIBLE
        }
    }

    override fun onBindViewHolder(holder: ImageLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = ImageLoadStateViewHolder(
        ItemLoadingStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        retry
    )
    fun View.visible(isVisible: Boolean) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}