package ru.netology.nmedia

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long = 0,
    val content: String = "",
    val published: String = "",
    val author: String = "",
    val liked: Boolean = false,
    val likeCount: Int = 0,
    val shareCount: Int = 0,
    val seenCount: Int = 0,
    val video: String = ""
) {
    companion object {
        fun demoDataPost(): List<Post> {
            return List(100) { index ->
                var url = ""
                if (index < 3) url = "https://www.youtube.com/watch?v=5Gt_mZRFCVs"

                Post(
                    id = index.toLong(),
                    content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                    published = "13.05.2022",
                    author = "$index Всемирная ассоциация любителей всего",
                    liked = false,
                    likeCount = 3119,
                    shareCount = index,
                    seenCount = 89,
                    video = url
                )
            }
        }
    }
}
