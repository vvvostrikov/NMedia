package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository
import x.y.z.SingleLiveEvent
import kotlin.properties.Delegates

class FilePostRepository(
    private val application: Application
) : PostRepository {
    private val prefs = application.getSharedPreferences("repo", Context.MODE_PRIVATE)
    override val data: MutableLiveData<List<Post>>
        get() = singltonData.data

    override val sharePostContent = SingleLiveEvent<String>()
    override val currentPost: MutableLiveData<Post?>
        get() = singltonData.currentPost

    // override val currentSinglePost = SingltonData.currentPost
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    val singltonData by lazy { SingltonData }

    //для примера, в текущей версии не нужно
    var nextId: Long by Delegates.observable(
        prefs.getLong(NEXTID_PREFS_KEY, 0L)
    ) { _, _, newValue ->
        prefs.edit { putLong(NEXTID_PREFS_KEY, newValue) }
    }

    private var posts
        get() = checkNotNull(data.value) { "Data value should not be null" }
        set(value) {
            application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
                it.write(gson.toJson(value))
            }
            data.value = value
            currentPost.value = value.find { it.id == currentPost.value?.id }
        }

    init {
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postsFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use {
                gson.fromJson(it, type)
            }
        } else emptyList<Post>()


        singltonData.data = MutableLiveData(posts)
    }

    override fun like(id: Long) {

        posts = posts.map {
            if (it.id == id) it.copy(
                liked = !it.liked,
                likeCount = it.likeCount + if (it.liked) -1 else 1
            ) else it
        }


    }

    override fun share(id: Long) {

        posts = posts.map {
            if (it.id == id) it.copy(shareCount = it.shareCount + 1)
                .apply { sharePostContent.value = it.content } else it
        }

    }

    override fun delete(id: Long) {
        posts = posts.filter { it.id != id }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post: Post) {
        posts = posts.map { if (it.id == post.id) post else it }
    }

    private fun insert(post: Post) {
        posts =
            listOf(post.copy(id = if (posts.isEmpty()) 1 else posts.maxOf { it.id } + 1)) + posts
    }

    private companion object {
        const val POSTS_PREFS_KEY = "posts"
        const val NEXTID_PREFS_KEY = "nextId"
        const val FILE_NAME = "posts.json"
    }

    object SingltonData {
        val currentPost = MutableLiveData<Post?>(null)
        var data = MutableLiveData<List<Post>>(null)
    }
}
