package ru.netology.nmedia.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository
import x.y.z.SingleLiveEvent

class InMemoryPostRepository : PostRepository {
    override val data = MutableLiveData(Post.demoDataPost())
    override val sharePostContent = SingleLiveEvent<String>()
    override val currentPost = MutableLiveData<Post?>(null)
   // override val currentSinglePost: MutableLiveData<Post?>
    //    get() = TODO("Not yet implemented")

    private val posts get() = checkNotNull(data.value) { "Data value should not be null" }

    override fun like(id: Long) {

        data.value = posts.map {
            if (it.id == id) it.copy(
                liked = !it.liked,
                likeCount = it.likeCount + if (it.liked) -1 else 1
            ) else it
        }


    }

    override fun share(id: Long) {

        data.value = posts.map { if (it.id == id) it.copy(shareCount = it.shareCount + 1).apply { sharePostContent.value = it.content } else it }

    }

    override fun delete(id: Long) {
        data.value = posts.filter { it.id != id }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post: Post) {
        data.value = posts.map { if (it.id == post.id) post else it }
    }

    private fun insert(post: Post) {
        data.value = listOf(post.copy(id = posts.maxOf { it.id } + 1)) + posts
    }

}