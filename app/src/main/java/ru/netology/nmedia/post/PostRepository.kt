package ru.netology.nmedia.post

import androidx.lifecycle.LiveData

interface PostRepository{
    val data: LiveData<List<Post>>

    fun like(postId: Long)
    fun share(postId: Long)
    fun delete(postId : Long)
    fun save(post : Post)

    companion object{
        const val NEW_POST_ID = 0L
    }
}
