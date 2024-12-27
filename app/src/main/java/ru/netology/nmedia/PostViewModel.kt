package ru.netology.nmedia

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

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
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
        edited.value?.let {
            repository.save(it)
            _data.value = repository.getAll()
        }
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
}