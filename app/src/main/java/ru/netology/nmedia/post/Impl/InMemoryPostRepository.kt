package ru.netology.nmedia.post.Impl


import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.post.PostRepository

import java.util.*


class InMemoryPostRepository: PostRepository {

    override val data = MutableLiveData(
        Post(
                    id = 0L,
                    author = "Vlad",
                    content = "Events",
                    published = "17.09.22",
                    likes = 0U,
                    countShare = 0U
        )
    )

    init {
        GlobalScope.launch(Dispatchers.Default) {
            while (true){
                delay(1000)
                val currentPost = checkNotNull(data.value){
                    "Data value should not be null"
                }
                val newPost = currentPost.copy(
                        published = Date().toString()
                )
                data.postValue(newPost)
            }
        }
    }

    override fun like() {
        val currentPost = checkNotNull(data.value){
            "Data value should be null"
        }
        val likedPost = currentPost.copy(
                likedByMe = !currentPost.likedByMe
        )
        likedPost.likes = if (likedPost.likedByMe) ++likedPost.likes
        else (--likedPost.likes)
        data.value = likedPost
    }
    override fun share() {
        val currentPost = checkNotNull(data.value)
        val clickShare = currentPost.copy(countShare = ++currentPost.countShare)
        data.value = clickShare
    }
}