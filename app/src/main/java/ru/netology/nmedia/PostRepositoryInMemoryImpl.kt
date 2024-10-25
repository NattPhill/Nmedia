package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun share()
}

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с простых курсов, а теперь это целый университет интернет-профессий будущего.",
        published = "21 мая в 18:36",
        likedByMe = false
    )

    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        val likes = if (post.likedByMe) post.likesCount - 1 else post.likesCount + 1
        post = post.copy(likedByMe = !post.likedByMe, likesCount = likes)
        data.value = post
    }

    override fun share() {
        post = post.copy(sharesCount = post.sharesCount + 1)
        data.value = post
    }
}