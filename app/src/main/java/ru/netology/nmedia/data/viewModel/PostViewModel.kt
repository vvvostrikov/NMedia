package ru.netology.nmedia.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.Post
import ru.netology.nmedia.PostContentFragment
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import x.y.z.SingleLiveEvent

class PostViewModel(application: Application) : AndroidViewModel(application),
    PostInteractionListener {
    private val repository: PostRepository = FilePostRepository(application)
    val dataViewModel by repository::data

    val sharePostContentModel by repository::sharePostContent
    val navigateToPostScreenEvent = SingleLiveEvent<String>()
    val navigateToPostSingle = SingleLiveEvent<Post>()
    val playVideoFromPost = SingleLiveEvent<String>()

    // val currentSinglePost by repository::currentSinglePost
    val currentPost by repository::currentPost
    override fun onLikeClicked(post: Post) = repository.like(post.id)
    override fun onShareClicked(post: Post) = repository.share(post.id)
    override fun onDeleteClicked(post: Post) = repository.delete(post.id)
    override fun onEditClicked(post: Post) {
        currentPost.value = post
        navigateToPostScreenEvent.value = post.content

        //navigateToPostScreenEvent.call()
    }

    override fun onPlayVideo(url: String) {
        playVideoFromPost.value = url
    }

    override fun onNavigateClicked(post: Post) {
        navigateToPostSingle.value = post
    }


    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return

        val editedPost = currentPost.value?.copy(content = content) ?: Post(
            id = PostRepository.NEW_POST_ID,
            content = content,
            published = "Today", author = "Me"
        )

        repository.save(editedPost)
        // currentPost.value = null
    }


    fun onAddClicked() {
        navigateToPostScreenEvent.call()
    }
}