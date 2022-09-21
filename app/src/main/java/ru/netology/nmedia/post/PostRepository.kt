package ru.netology.nmedia.post

import androidx.lifecycle.LiveData

interface PostRepository{
    val data: LiveData<List<Post>>
    fun like(postId: Long)
    fun share(postId: Long)
}
