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

    private var nextId = GENERATED_POSTS_AMOUNT.toLong()

    override val data = MutableLiveData(
            List(GENERATED_POSTS_AMOUNT) {index->
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

    override fun delete(postId : Long) {
        data.value = posts.filterNot {it.id == postId}
    }

    override fun save(post : Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post : Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private fun insert(post : Post) {
        data.value = listOf(
                post.copy(id = ++nextId)
        ) + posts
    }

    private companion object{
        const val GENERATED_POSTS_AMOUNT = 1000
    }
}