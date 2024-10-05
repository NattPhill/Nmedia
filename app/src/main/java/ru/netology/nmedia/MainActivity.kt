package ru.netology.nmedia

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var isLiked = false
    private var likesCount = 999
    private var sharesCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Инициализация кнопок и текстовых полей через findViewById
        val likesButton: ImageButton = findViewById(R.id.likes)
        val shareButton: ImageButton = findViewById(R.id.share)
        val numberOfLikesTextView: TextView = findViewById(R.id.numberOfLikes)
        val numberOfSharesTextView: TextView = findViewById(R.id.numberOfShare)

        // Установка начальных значений
        updateLikes(likesButton, numberOfLikesTextView)
        updateShares(numberOfSharesTextView)

        // Обработка клика на лайк
        likesButton.setOnClickListener {
            isLiked = !isLiked
            if (isLiked) {
                likesCount++
            } else {
                likesCount--
            }
            updateLikes(likesButton, numberOfLikesTextView)
        }

        // Обработка клика на кнопку share
        shareButton.setOnClickListener {
            sharesCount++
            updateShares(numberOfSharesTextView)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // обновление числа лайков и смена иконки
    private fun updateLikes(likesButton: ImageButton, numberOfLikesTextView: TextView) {
        if (isLiked) {
            likesButton.setImageResource(R.drawable.liked)
        } else {
            likesButton.setImageResource(R.drawable.like_2)
        }
        numberOfLikesTextView.text = formatCount(likesCount)
    }

    // обновлениe числа шарингов
    private fun updateShares(numberOfSharesTextView: TextView) {
        numberOfSharesTextView.text = formatCount(sharesCount)
    }

    // форматирование числа
    private fun formatCount(count: Int): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
            count >= 10_000 -> String.format("%dK", count / 1_000)
            count >= 1_100 -> String.format("%.1fK", count / 1_000.0)
            count >= 1_000 -> String.format("%dK", count / 1_000)
            else -> count.toString()
        }
    }
}
