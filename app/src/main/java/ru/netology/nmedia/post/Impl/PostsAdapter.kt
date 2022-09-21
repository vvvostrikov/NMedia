package ru.netology.nmedia.post.Impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.post.Post
import kotlin.math.round

internal class PostsAdapter(
    private val onLikeClicked: (Post) -> Unit,
    private val onShareClicked: (Post) -> Unit,
): ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: PostBinding
    ): RecyclerView.ViewHolder(binding.root){

        private lateinit var post : Post

        init {
            binding.likes.setOnClickListener {
                onLikeClicked(post)
            }
            binding.share.setOnClickListener {
                onShareClicked(post)
            }
        }

        fun bind(post : Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                textPost.text = post.content
                date.text = post.published
                countShare.text = formatCount(post.countShare)
                countLikes.text = formatCount(post.likes)
                likes.setImageResource(getLikeIconResId(post.likedByMe))
            }
        }
        @DrawableRes
        private fun getLikeIconResId(liked : Boolean) =
            if (liked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24

        private fun formatCount(count: UInt): String {
            return when (count) {
                in 0u..1099u -> "$count"
                in 1100u..10_000u -> "${round((count / 100u).toDouble()) / 10}K"
                in 10_001u..999_999u -> "${round((count / 1000u).toDouble())}K"
                else -> "${round((count / 100_000u).toDouble()) / 10}M"
            }
        }

    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>(){
        override fun areContentsTheSame(oldItem : Post, newItem : Post) =
            oldItem.id == newItem.id

        override fun areItemsTheSame(oldItem : Post, newItem : Post) =
            oldItem == newItem
    }
}