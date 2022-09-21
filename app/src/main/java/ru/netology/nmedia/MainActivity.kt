package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.viewModel.PostViewModel
import kotlin.math.*
import androidx.activity.viewModels



class MainActivity : AppCompatActivity() {

    private val viewModel by  viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this){post ->
                binding.render(post)
                binding.likes.setImageResource(getLikeIconResId(post.likedByMe))
        }
        binding.likes.setOnClickListener() {
            viewModel.onLikeClicked()
        }
        binding.share.setOnClickListener {
            viewModel.onShareClicked()
        }
    }

    private fun formatCount(count : UInt) : String {
        return when (count) {
            in 0u .. 1099u -> count.toString()
            in 1100u .. 10_000u -> "${round((count / 100u).toDouble() / 10)}K"
            in 10_001u .. 999_999u -> "${round((count / 1000u).toDouble()) / 10}K"
            else -> "${round((count / 100000u).toDouble()) / 10}M"
        }
    }


    private fun ActivityMainBinding.render(post : Post) {
        authorName.text = post.author
        textPost.text = post.content
        date.text = post.published
        countShare.text = formatCount(post.countShare)
        countLikes.text = formatCount(post.likes)
    }

    @DrawableRes
    private fun getLikeIconResId(liked : Boolean) =
        if (liked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
}