package ru.netology.nmedia.post.Impl

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.post.PostRepository
import java.util.*

class InMemoryPostRepository: PostRepository {

    private val posts get() = checkNotNull(data.value){
        "Data value should not be null"
    }

    override val data = MutableLiveData(
            List(1000) {index->
                Post(
                        id = index + 1L,
                        author = "Netology",
                        content = "Пост № $index",
                        published = "20.09.22",
                        likes = 0U,
                        countShare = 0U,
                        likedByMe = false
                )
            }
    )

    /*init {
        GlobalScope.launch {
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
    }*/

    override fun like(postId: Long) {
        data.value = posts.map {
            if (it.id == postId) it.copy(
                    likedByMe = !it.likedByMe,
                    likes = if (it.likedByMe) --it.likes  else ++it.likes
            ) else it
        }
    }

    override fun share(postId: Long) {
        data.value = posts.map {
            if (it.id == postId) it.copy(countShare = it.countShare + 1u) else it
        }
    }
}