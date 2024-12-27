package ru.netology.nmedia

import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemoryImpl : PostRepository {
    private var posts = listOf(
        Post(
            id = 2,
            author = "Нетология",
            content = "Знаний хватит на всех.",
            published = "18 сентября в 10:12",
            likedByMe = false
        ),
        Post(
            id = 1,
            author = "Нетология",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с простых курсов, а теперь это целый университет интернет-профессий будущего.",
            published = "21 мая в 18:36",
            likedByMe = false
        ),
    )

    private val data = MutableLiveData(posts)
    private var nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1

    override fun getAll(): List<Post> = posts

    //создаем новый пост
    override fun save(post: Post) {
        if (post.id == 0L) {
            val newPost = post.copy(
                id = nextId++,
                author = "Me",
                likedByMe = false,
                published = "now"
            )
            posts = listOf(newPost) + posts
        } else {
            posts = posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }
        data.value = posts
    }

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likesCount = if (it.likedByMe) it.likesCount - 1 else it.likesCount + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map { post ->
            if (post.id != id) post else post.copy(sharesCount = post.sharesCount + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }.toMutableList()
        data.value = posts
    }
}
