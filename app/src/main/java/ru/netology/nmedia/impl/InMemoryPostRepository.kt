package ru.netology.nmedia.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.PostRepository


class InMemoryPostRepository : PostRepository {

    private var nextId = GENERATED_POST_AMOUNT.toLong()

    private val posts
        get() = checkNotNull(data.value) { "Data value should not be null" }

    override val data = MutableLiveData(
        List(GENERATED_POST_AMOUNT) { index ->
            Post(
                id = index + 1L,
                author = "Netology",
                content = "Some whe are over the rainbow... $index",
                published = "21.$index.2022",
                likes = 999,
                likedByMe = false,
                countShare = 0,
                video = "https://www.youtube.com/watch?v=sQB1cPS2MzI"
            )
        }
    )

    override fun like(postId: Long) {
        data.value = posts.map {
            if (it.id == postId) it.copy(
                likedByMe = !it.likedByMe,
                likes = it.likes + if (it.likedByMe) -1 else +1
            ) else it
        }
    }

    override fun share(postId: Long) {
        data.value = posts.map {
            if (it.id == postId) it.copy(countShare = it.countShare + 100) else it
        }
    }

    override fun delete(postId: Long) {
        data.value = posts.filter { it.id != postId }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        data.value = listOf(post.copy(id = ++nextId)) + posts
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private companion object {
        const val GENERATED_POST_AMOUNT = 10
    }
}







