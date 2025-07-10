package ru.netology.nmedia

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PostRepositoryFilesImpl(private val context: Context) : PostRepository {

    private var posts = emptyList<Post>()
        set(value) {
            field = value
            data.value = posts
            sinc()
        }

    private val data = MutableLiveData<List<Post>>()

    private var nextId = 1L

    init {
        val file = context.filesDir.resolve(FILE_NAME)
        if (file.exists()) {
            context.openFileInput(FILE_NAME).bufferedReader().use {
                posts = gson.fromJson(it, type) as List<Post>
                nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
            }
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

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
    }

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likesCount = if (it.likedByMe) it.likesCount - 1 else it.likesCount + 1
            )
        }
    }

    override fun shareById(id: Long) {
        posts = posts.map { post ->
            if (post.id != id) post else post.copy(sharesCount = post.sharesCount + 1)
        }
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }.toMutableList()
    }

    private fun sinc() {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    companion object {
        private const val FILE_NAME = "posts.json"
        private val gson = Gson()
        private val type = TypeToken.getParameterized(List::class.java, Post::class.java)
    }
}
