package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val post: LiveData<Post> = repository.get()

    fun onLikeClicked() {
        repository.like()
    }

    fun onShareClicked() {
        repository.share()
    }
}
