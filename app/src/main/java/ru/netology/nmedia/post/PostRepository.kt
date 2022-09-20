package ru.netology.nmedia.post

import androidx.lifecycle.LiveData

interface PostRepository{
    val data: LiveData<Post>
    fun like()
    fun share()
}
