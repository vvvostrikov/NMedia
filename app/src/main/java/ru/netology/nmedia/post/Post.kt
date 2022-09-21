package ru.netology.nmedia.post

data class Post(val id: Long,
                val author: String,
                val content: String,
                val published: String,
                val likes: UInt,
                val countShare: UInt,
                val likedByMe: Boolean = false)