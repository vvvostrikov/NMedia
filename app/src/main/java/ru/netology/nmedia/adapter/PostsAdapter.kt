package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.post.Post
import kotlin.math.round

internal class PostsAdapter(
    private val interactionListener : PostInteractionListener
): ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater,parent,false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: PostBinding,
        listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.option).apply {
                inflate(R.menu.option_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.likes.setOnClickListener {
                listener.onLikeClicked(post)
            }
            binding.share.setOnClickListener {
                listener.onShareClicked(post)
            }
            binding.option.setOnClickListener { popupMenu.show() }
        }

        fun bind(post : Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                textPost.text = post.content
                date.text = post.published
                share.text = formatCount(post.countShare)
                likes.isChecked = post.likedByMe
                likes.text = formatCount(post.likes)
            }
        }

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