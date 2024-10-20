package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {
    private val _post = MutableLiveData(Post()) // Внутренние данные
    val post: LiveData<Post> = _post // Данные для View (чтобы экран мог слушать изменения)

    fun onLikeClicked() {
        _post.value?.let { post ->
            post.isLiked = !post.isLiked
            if (post.isLiked) {
                post.likesCount++
            } else {
                post.likesCount--
            }
            _post.value = post // Обновляем данные
        }
    }

    fun onShareClicked() {
        _post.value?.let { post ->
            post.sharesCount++
            _post.value = post // Обновляем данные
        }
    }
}
