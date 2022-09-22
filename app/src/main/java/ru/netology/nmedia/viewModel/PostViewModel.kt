package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.post.Impl.InMemoryPostRepository
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.post.PostRepository


class PostViewModel: ViewModel(), PostInteractionListener {
    private val repository: PostRepository = InMemoryPostRepository()
    val data by repository:: data

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
                likedByMe = false,
                likes = 0u,
                countShare = 0u
        )
        repository.save(post)
        currentPost.value = null
    }

    override fun onLikeClicked(post: Post) = repository.like(post.id)
    override fun onShareClicked(post: Post) = repository.share(post.id)
    override fun onRemoveClicked(post: Post) = repository.delete(post.id)
    override fun onEditClicked(post: Post) { currentPost.value = post }
}