package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.data.viewModel.PostViewModel
import ru.netology.nmedia.databinding.PostBinding
import java.lang.RuntimeException


class PostEditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var idPost: Long? = null
    val viewModel: PostViewModel by viewModels<PostViewModel>()
    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idPost = it.getLong(INITIAL_POST_KEY)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        if (viewModel.currentPost.value == null)
            RuntimeException("Error receive post parameter")
        else post = viewModel.currentPost.value ?: post


        val binding = PostBinding.inflate(layoutInflater, container, false).also { binding ->
            with(binding) {

                bind(binding)


            }
        }
        val popupMenu by lazy {
            PopupMenu(this.context, binding.dropdownMenu).apply {
                inflate(R.menu.option_post)
                this.setOnMenuItemClickListener { menuItems ->
                    when (menuItems.itemId) {
                        R.id.remove -> {
                            viewModel.onDeleteClicked(post)
                            findNavController().popBackStack()
                            true
                        }
                        R.id.menu_edit -> {
                            viewModel.onEditClicked(post)
                            true
                        }
                        else -> false
                    }

                }
            }
        }
        binding.dropdownMenu.setOnClickListener { popupMenu.show() }
        binding.imageButtonFavorit.setOnClickListener {
            viewModel.onLikeClicked(post)

            bind(binding)
        }
        binding.imageButtonShare.setOnClickListener {
            viewModel.onShareClicked(post)
            bind(binding)
        }
        binding.video.setOnClickListener {
            viewModel.onPlayVideo(post.video)
            bind(binding)
        }
        binding.playButton.setOnClickListener {
            viewModel.onPlayVideo(post.video)
            bind(binding)
        }


        viewModel.sharePostContentModel.observe(viewLifecycleOwner) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }

        viewModel.playVideoFromPost.observe(viewLifecycleOwner) { videoUrl ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))

            val videoPlayIntent = Intent.createChooser(intent, getString(R.string.chooser_player))
            //if (videoPlayIntent.resolveActivity(packageManager) != null) {
            startActivity(videoPlayIntent)
            //}
        }
        viewModel.navigateToPostScreenEvent.observe(viewLifecycleOwner) { initialContent ->
            findNavController().navigate(
                R.id.action_postEditFragment_to_postContentFragment,
                PostContentFragment.createBundle(
                    initialContent,
                    PostContentFragment.REQUEST_KEY_SINGLE
                )
            )
        }
        setFragmentResultListener(requestKey = PostContentFragment.REQUEST_KEY_SINGLE) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY_SINGLE) return@setFragmentResultListener
            val postContent =
                bundle.getString(PostContentFragment.RESULT_KEY) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(postContent)
            bind(binding)

        }



        return binding.root
    }

    fun bind(binding: PostBinding) = with(binding) {
        // val post = viewModel.dataViewModel.value?.find { it.id == idPost }
        if (viewModel.currentPost.value == null) return@with

        this@PostEditFragment.post = viewModel.currentPost.value ?: post
        imageButtonFavorit.isChecked = post.liked
        imageButtonFavorit.text = getStringOfCount(post.likeCount)
        contentPost.text = post.content
        author.text = post.author
        published.text = post.published

        imageButtonShare.text = getStringOfCount(post.shareCount)
        imageEye.text = getStringOfCount(post.seenCount)

        videoGroup.visibility =
            if (post.video.isBlank()) View.GONE else View.VISIBLE


    }


    companion object {

        const val INITIAL_POST_KEY = "openSinglePost"

        fun createBundle(postId: Long) = Bundle(1).apply {
            putLong(INITIAL_POST_KEY, postId)
        }

    }
}