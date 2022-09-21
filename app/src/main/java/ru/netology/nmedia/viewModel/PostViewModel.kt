package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.post.Impl.InMemoryPostRepository
import ru.netology.nmedia.post.PostRepository

class PostViewModel: ViewModel() {
    private val repository: PostRepository = InMemoryPostRepository()
    val data by repository:: data

    fun onLikeClicked() = repository.like()
    fun onShareClicked() = repository.share()
}