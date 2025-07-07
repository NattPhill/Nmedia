package ru.netology.nmedia

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PostRepositorySharedPrefsImpl(context: Context) : PostRepository {

    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            data.value = posts
            sinc()
        }

    private val data = MutableLiveData(posts)

    private var nextId = 0L

    init {
       prefs.getString(KEY_POSTS, null)?.let {
           posts = gson.fromJson(it, type) as List<Post>
           nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
       }
    }

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
        prefs.edit {
            putString(KEY_POSTS, gson.toJson(posts))
        }
    }

    companion object {
        private const val KEY_POSTS = "posts"
        private val gson = Gson()
        private val type = TypeToken.getParameterized(List::class.java, Post::class.java)
    }
}
