package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import x.y.z.SingleLiveEvent

interface PostRepository {
    val data: LiveData< List<Post>>
    val sharePostContent:SingleLiveEvent<String>
    val currentPost:MutableLiveData<Post?>
   // val currentSinglePost:MutableLiveData<Post?>
    fun like(id:Long)
    fun share(id:Long)
    fun delete(id: Long)
    fun save(post: Post)
    companion object{
        const val NEW_POST_ID = 0L
    }
}