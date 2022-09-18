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
            likes = 0U

        )
        binding.render(post)
        binding.likes.setOnClickListener() {
            post.likedByMe = !post.likedByMe
            binding.likes.setImageResource(getLikeIconResId(post.likedByMe))
            let {
                if (post.likedByMe) {
                    binding.countLikes.text = formatLikes(post.likes + 1u)
                    R.drawable.ic_baseline_favorite_24
                } else {
                    binding.countLikes.text = formatLikes(post.likes - 1u + 1u)
                    R.drawable.ic_baseline_favorite_border_24
                }
            }

            var countShare = 0u

            binding.share.setOnClickListener{
                countShare += 1u
                binding.countShare.text = formatLikes(countShare)
            }
        }
    }


    private fun formatLikes(likes : UInt) : String {
        return when (likes) {
            in   0u .. 1099u -> likes.toString()
            in 1100u .. 10_000u -> "${round((likes / 100u).toDouble()) / 10}K"
            in 10_001u .. 999_999u-> "${round((likes / 1000u).toDouble()) / 10}K"
            else -> "${round((likes / 100000u).toDouble()) / 10}M"
        }
    }


    private fun ActivityMainBinding.render(post : Post) {
        authorName.text = post.author
        textPost.text = post.content
        date.text = post.published
        countLikes.text = post.likes.toString()
    }

    @DrawableRes
    private fun getLikeIconResId(liked : Boolean) =
        if (liked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24

}