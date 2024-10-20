package ru.netology.nmedia

data class Post(
    var isLiked: Boolean = false,
    var likesCount: Int = 9999,
    var sharesCount: Int = 0
)