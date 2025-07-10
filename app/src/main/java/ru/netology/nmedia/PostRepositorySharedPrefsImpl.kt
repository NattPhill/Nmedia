package ru.netology.nmedia

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PostRepositorySharedPrefsImpl(context: Context) : PostRepository {

    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private var posts = emptyList<Post>()

    private val data = MutableLiveData<List<Post>>()

    private var nextId = 1L

    init {
        prefs.getString(KEY_POSTS, null)?.let {
            posts = gson.fromJson(it, type) as List<Post>
            nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
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

    companion object {
        private const val KEY_POSTS = "posts"
        private val gson = Gson()
        private val type = TypeToken.getParameterized(List::class.java, Post::class.java)
    }
}
