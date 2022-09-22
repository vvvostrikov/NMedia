package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel
import androidx.activity.viewModels
import ru.netology.nmedia.post.Impl.PostsAdapter


class MainActivity : AppCompatActivity() {

    private val viewModel by  viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel::onLikeClicked, viewModel::onShareClicked)
        binding.postsRecyclerView.adapter = adapter
            viewModel.data.observe(this) { posts ->
                adapter.submitList(posts)
            }
    }
}