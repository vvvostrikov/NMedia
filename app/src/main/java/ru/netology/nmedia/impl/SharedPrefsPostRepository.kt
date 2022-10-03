package ru.netology.nmedia.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.Post
import ru.netology.nmedia.PostRepository
import kotlin.properties.Delegates


class SharedPrefsPostRepository(
    application: Application
) : PostRepository {

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )
//    private var nextId = GENERATED_POST_AMOUNT.toLong()
    private var nextId: Long by Delegates.observable(
        prefs.getLong(NEXT_ID_PREFS_KEY, 0L)
    ) {_, _, newValue ->
        prefs.edit { putLong(NEXT_ID_PREFS_KEY, newValue) }
    }

    private var posts
        get() = checkNotNull(data.value) { "Data value should not be null" }
        set(value) {
            prefs.edit {
                val serializedPosts =Json.encodeToString(value)
                putString(POST_PREFS_KEY, serializedPosts)
            }
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>
//        List(GENERATED_POST_AMOUNT) { index ->
//            Post(
//                id = index + 1L,
//                author = "Netology",
//                content = "Some whe are over the rainbow... $index",
//                published = "21.$index.2022",
//                likes = 999,
//                likedByMe = false,
//                countShare = 0,
//                video = "https://www.youtube.com/watch?v=sQB1cPS2MzI"
//            )
//        }
//    )

    init {
        val serializedPosts = prefs.getString(POST_PREFS_KEY, null)
        val posts: List<Post> = if (serializedPosts != null) {
            Json.decodeFromString(serializedPosts)
        } else emptyList()
        data = MutableLiveData(posts)
    }

    override fun like(postId: Long) {
        posts = posts.map {
            if (it.id == postId) it.copy(
                likedByMe = !it.likedByMe,
                likes = it.likes + if (it.likedByMe) -1 else +1
            ) else it
        }
    }

    override fun share(postId: Long) {
        posts = posts.map {
            if (it.id == postId) it.copy(countShare = it.countShare + 100) else it
        }
    }

    override fun delete(postId: Long) {
        posts = posts.filter { it.id != postId }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        posts = listOf(post.copy(id = ++nextId)) + posts
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private companion object {
        const val GENERATED_POST_AMOUNT = 10
        const val POST_PREFS_KEY = "posts"
        const val NEXT_ID_PREFS_KEY = "NextId"
    }
}







