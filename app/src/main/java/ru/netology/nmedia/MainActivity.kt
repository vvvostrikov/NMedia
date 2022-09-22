package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import ru.netology.nmedia.adapter.PostsAdapter


class MainActivity : AppCompatActivity() {

    private val viewModel by  viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel)
        binding.postsRecycleView.adapter = adapter
        binding.group.visibility = View.GONE

        viewModel.data.observe(this) { post ->
            adapter.submitList(post)
        }

        binding.cancelButton.setOnClickListener {
            binding.group.visibility = View.GONE
            binding.addTextContent.clearFocus()
            binding.addTextContent.hideKeyboard()
            binding.addTextContent.setText("")
            viewModel.currentPost.value = null
        }

        binding.addTextContent.doAfterTextChanged {
            if (binding.addTextContent.text.isNotBlank())
                binding.group.visibility = View.VISIBLE
        }

        binding.saveButton.setOnClickListener {
            val content = binding.addTextContent.text.toString()
            viewModel.onSaveButtonClicked(content)
            binding.addTextContent.clearFocus()
            binding.addTextContent.hideKeyboard()
            binding.group.visibility = View.GONE
        }

        viewModel.currentPost.observe(this) { currentPost ->
            binding.addTextContent.setText(currentPost?.content)
        }
    }
}