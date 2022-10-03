package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.activity.PostContentActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.impl.PostsAdapter

class MainActivity : AppCompatActivity() {


    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        run {
//            val preferences = getPreferences(Context.MODE_PRIVATE)
//            preferences.edit {
//                putString("key", "value")
//            }
//        }
//
//        run {
//            val preferences = getPreferences(Context.MODE_PRIVATE)
//            val value = preferences.getString("key", "no value") ?: return@run
//            Snackbar.make(binding.root, value, Snackbar.LENGTH_INDEFINITE).show()
//        }

        val adapter = PostsAdapter(viewModel)
        binding.postsRecycleView.adapter = adapter
//        binding.group.visibility = View.GONE

        viewModel.data.observe(this) { post ->
            adapter.submitList(post)
        }

//        binding.cancelButton.setOnClickListener {
//            binding.group.visibility = View.GONE
//            binding.addTextContent.clearFocus()
//            binding.addTextContent.hideKeyboard()
//            binding.addTextContent.setText("")
//            viewModel.currentPost.value = null
//        }

//        binding.addTextContent.doAfterTextChanged {
//            if (binding.addTextContent.text.isNotBlank())
//                binding.group.visibility = View.VISIBLE
//        }

        binding.addPost.setOnClickListener {
            viewModel.onAddClicked()
        }

        viewModel.playVideo.observe(this) { videoUrl ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
            val videoPlayIntent = Intent.createChooser(intent, getString(R.string.chooser_playVideo_post))
            if (videoPlayIntent.resolveActivity(packageManager) != null)
                startActivity(videoPlayIntent)
        }

        viewModel.sharePostEvent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }

        val postContentActivityResultContract = PostContentActivity.ResultContract
        val postContentActivityLauncher = registerForActivityResult(postContentActivityResultContract){postContent->
            if(postContent==null){
                viewModel.currentPost.value?.content
                return@registerForActivityResult
            }
            viewModel.onSaveButtonClicked(postContent)
        }
        viewModel.navigateToPostContentScreenEvent.observe(this) {
            postContentActivityLauncher.launch(it)
        }
    }
}












