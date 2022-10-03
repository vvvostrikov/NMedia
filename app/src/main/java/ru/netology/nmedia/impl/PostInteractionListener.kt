package ru.netology.nmedia.impl

import ru.netology.nmedia.Post

interface PostInteractionListener {

    fun onLikeClicked(post: Post)
    fun onShareClicked(post: Post)
    fun onRemoveClicked(post: Post)
    fun onEditClicked(post: Post)
    fun onPlayVideo(uri: String)
}