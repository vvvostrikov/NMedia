package ru.netology.nmedia.post

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: UInt = 0u,
    var likedByMe: Boolean = false
)
