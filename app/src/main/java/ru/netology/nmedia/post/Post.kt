package ru.netology.nmedia.post

data class Post(val id: Long,
                val author: String,
                val content: String,
                val published: String,
                var likes: UInt,
                var countShare: UInt,
                var likedByMe: Boolean = false)