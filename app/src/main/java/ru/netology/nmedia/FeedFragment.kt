package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.data.viewModel.PostViewModel
import ru.netology.nmedia.databinding.ActivityMainBinding

class FeedFragment : Fragment() {

    val viewModel: PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.sharePostContentModel.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }

        viewModel.playVideoFromPost.observe(this) { videoUrl ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))

            val videoPlayIntent = Intent.createChooser(intent, getString(R.string.chooser_player))
            startActivity(videoPlayIntent)
        }
        setFragmentResultListener(requestKey = PostContentFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val postContent =
                bundle.getString(PostContentFragment.RESULT_KEY) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(postContent)
            viewModel.currentPost.value = null
        }



        viewModel.navigateToPostScreenEvent.observe(this) { initialContent ->
            findNavController().navigate(
                R.id.to_postContentFragment,
                PostContentFragment.createBundle(initialContent, PostContentFragment.REQUEST_KEY)
            )


        }
        viewModel.navigateToPostSingle.observe(this) { postToSingle ->
            viewModel.currentPost.value = postToSingle
            findNavController().navigate(
                R.id.postEditFragment,
                PostEditFragment.createBundle(postToSingle.id)
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ActivityMainBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = PostsAdapter(viewModel)

        binding.container.adapter = adapter
        viewModel.dataViewModel.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }
        binding.fab.setOnClickListener {
            viewModel.currentPost.value = null
            viewModel.onAddClicked()
        }
    }.root

    companion object {
        const val TAG = "FeedFragment"
    }
}

fun getStringOfCount(count: Int): String {

    return when (count) {
        in 0..999 -> count.toString()
        in 1000..10000 -> {
            val firstDigi = (count / 1000)
            val secondDigi = ((count % 1000) / 100)
            var secondPart = ".${secondDigi}K"

            if (secondDigi == 0) secondPart = "K"
            "$firstDigi$secondPart"
        }
        in 10001..999999 -> {
            "${(count / 1000)}K"
        }
        else -> {
            val firstDigi = (count / 1_000_000)
            val secondDigi = ((count % 1_000_000) / 100_000)
            var secondPart = ".${secondDigi}M"

            if (secondDigi == 0) secondPart = "M"
            "$firstDigi$secondPart"

        }
    }
}



