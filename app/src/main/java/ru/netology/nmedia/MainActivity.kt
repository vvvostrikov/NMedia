package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.post.Post
import kotlin.math.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val post = Post(
            id = 0L,
            author = "Vlad",
            content = "Events",
            published = "17.09.22",
            likes = 1100U,
            countShare = 1100U
        )
        binding.render(post)
        binding.likes.setOnClickListener() {
            post.likedByMe = !post.likedByMe
            binding.likes.setImageResource(getLikeIconResId(post.likedByMe))
                if (post.likedByMe) {
                    binding.countLikes.text = formatCount(++post.likes)
                    R.drawable.ic_baseline_favorite_24
                } else {
                    binding.countLikes.text = formatCount(--post.likes)
                    R.drawable.ic_baseline_favorite_border_24
                }

        }
        binding.share.setOnClickListener {
            post.countShare += 1u
            binding.countShare.text = formatCount(post.countShare)
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