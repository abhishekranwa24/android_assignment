package com.example.appentus.Adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.appentus.Data.Dao.ImageEntity
import com.example.appentus.R
import com.example.appentus.databinding.ImageRowBinding

import javax.inject.Inject

class ImageAdapter
@Inject
constructor() : PagingDataAdapter<ImageEntity,ImageAdapter.ImageAdapterViewHolder>(DiffUtils) {

    inner class ImageAdapterViewHolder(private val binding: ImageRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageEntity) = with(binding) {
            progress.setVisibility(View.VISIBLE)
            // Geting the Image with help of Glide
            Glide.with(itemView.context)
                .load(item.download_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .downsample(DownsampleStrategy.AT_MOST)
                .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.setVisibility(View.GONE)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.setVisibility(View.GONE)
                    return false
                }
            })
            .into(image)
        }
    }

    object DiffUtils : DiffUtil.ItemCallback<ImageEntity>(){
        override fun areItemsTheSame(oldItem: ImageEntity, newItem: ImageEntity) =
             oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: ImageEntity, newItem: ImageEntity) =
             oldItem == newItem

    }

    override fun onBindViewHolder(holder: ImageAdapterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapterViewHolder {
       return ImageAdapterViewHolder(ImageRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

}