package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.getStringOfCount

class PostsAdapter(

    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: PostBinding, listener: PostInteractionListener) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var post: Post
        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.dropdownMenu).apply {
                inflate(R.menu.option_post)
                this.setOnMenuItemClickListener { menuItems ->
                    when (menuItems.itemId) {
                        R.id.remove -> {
                            listener.onDeleteClicked(post)
                            true
                        }
                        R.id.menu_edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }

                }
            }
        }

        init {
            binding.imageButtonFavorit.setOnClickListener {
                listener.onLikeClicked(post)
            }
            binding.imageButtonShare.setOnClickListener {
                listener.onShareClicked(post)
            }
            binding.video.setOnClickListener {
                listener.onPlayVideo(post.video)
            }
            binding.playButton.setOnClickListener {
                listener.onPlayVideo(post.video)
            }
            binding.contentPost.setOnClickListener {
                listener.onNavigateClicked(post)
            }

        }

        fun bind(post: Post) = with(binding) {
            this@ViewHolder.post = post
            imageButtonFavorit.isChecked = post.liked
            imageButtonFavorit.text = getStringOfCount(post.likeCount)
            contentPost.text = post.content
            author.text = post.author
            published.text = post.published

            imageButtonShare.text = getStringOfCount(post.shareCount)
            imageEye.text = getStringOfCount(post.seenCount)
            dropdownMenu.setOnClickListener { popupMenu.show() }
            videoGroup.visibility = if (post.video.isBlank()) View.GONE else View.VISIBLE


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }
}