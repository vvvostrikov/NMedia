package ru.netology.nmedia

import SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.impl.InMemoryPostRepository
import ru.netology.nmedia.impl.PostInteractionListener

class PostViewModel : ViewModel(), PostInteractionListener {
    private val repository: PostRepository = InMemoryPostRepository()
    val data by repository::data

    val playVideo = SingleLiveEvent<String>()
    val sharePostEvent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<String>()
    val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return

        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "to Day",
            video = "https://www.youtube.com/watch?v=ERhAQ1QXRFA&ab_channel=DigitalDelirium"
        )
        repository.save(post)
        currentPost.value = null
    }

    override fun onLikeClicked(post: Post) = repository.like(post.id)
    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        sharePostEvent.value = post.content
    }

    override fun onRemoveClicked(post: Post) = repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEvent.value = post.content
    }

    override fun onPlayVideo(url: String) {
        playVideo.value = url
    }

    fun onAddClicked() {
        navigateToPostContentScreenEvent.call()
    }
}
