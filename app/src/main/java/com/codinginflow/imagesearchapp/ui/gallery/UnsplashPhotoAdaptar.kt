package com.codinginflow.imagesearchapp.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.data.UnsplashPhoto
import com.codinginflow.imagesearchapp.databinding.ItemUnsplashPhotoBinding

class UnsplashPhotoAdaptar(private val listener: ItemClickListener) : PagingDataAdapter<UnsplashPhoto,UnsplashPhotoAdaptar.PhotoViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemUnsplashPhotoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null){
            holder.bind(currentItem)
        }

    }

    inner class PhotoViewHolder(
        private val binding: ItemUnsplashPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener{
                val position = bindingAdapterPosition
                val item = getItem(position)
                if (item != null) {
                    listener.onItemClickListener(item)
                }

            }
        }

        fun bind(currentItem : UnsplashPhoto){
            binding.apply{
                //We use Glide to set the image to imageView
                //with needs a fragment reference for lifecycle mangagement but we can also pass view to use its lifecycle, Here itemView is whole row
                Glide.with(itemView)
                    .load(currentItem.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)
                usernameTextView.text = currentItem.user.username
            }

        }

    }
    class DiffCallback : DiffUtil.ItemCallback<UnsplashPhoto>(){
        override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean =
            oldItem == newItem
    }

    interface ItemClickListener{
        fun onItemClickListener(unsplashPhoto: UnsplashPhoto)
    }
}