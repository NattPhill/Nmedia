package ru.netology.nmedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFilesImpl(application)
    private val _data = MutableLiveData(repository.getAll())
    val data: LiveData<List<Post>> = _data

    val edited = MutableLiveData(empty)

    private var isEditing: Boolean = false
    private var editingPostId: Long? = null

    fun onLikeClicked(id: Long) {
        repository.likeById(id)
        _data.value = repository.getAll()
    }

    fun onShareClicked(id: Long) {
        repository.shareById(id)
        _data.value = repository.getAll()
    }

    fun removeById(id: Long) {
        repository.removeById(id)
        _data.value = repository.getAll()
    }

    fun save() {
        val post = edited.value ?: return
//        val updated = if (id == 0L) {
//            post.copy(id = 0L)
//        } else {
//            post.copy(id = id)
//        }
            repository.save(post)
            _data.value = repository.getAll()
            edited.value = empty  //очистка состояния редактирования
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun cancelEdit() {
        return
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim() // проверяем, вносились ли изменения
            if (it.content == text) {
                return
            }
            // если текст изменился, обновляем
            edited.value = it.copy(content = text)
        }
    }

    fun getById(id: Long): Post {
        return data.value?.find { it.id == id } ?: empty
    }

}