package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    private val _data = MutableLiveData(repository.getAll())
    val data: LiveData<List<Post>> = _data

    fun onLikeClicked(id: Long) {
        repository.likeById(id)
        _data.value = repository.getAll()
    }

    fun onShareClicked(id: Long) {
        repository.shareById(id)
        _data.value = repository.getAll()
    }
}