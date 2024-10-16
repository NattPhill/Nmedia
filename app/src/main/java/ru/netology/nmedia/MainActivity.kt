package ru.netology.nmedia

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.floor

data class Post(
    var isLiked: Boolean = false,
    var likesCount: Int = 16899,
    var sharesCount: Int = 0
)

class MainActivity : AppCompatActivity() {

    private var post = Post()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            println("клик на root")
        }

        binding.avatar.setOnClickListener {
            println("клик на avatar")
        }

        // Установка начальных значений
        updateUI(binding.like, binding.numberOfLikes, binding.numberOfShare)

        // Обработка клика на лайк
        binding.like.setOnClickListener {
            post.isLiked = !post.isLiked
            if (post.isLiked) {
                post.likesCount++
            } else {
                post.likesCount--
            }
            updateUI(binding.like, binding.numberOfLikes, binding.numberOfShare)
        }

        // Обработка клика на кнопку share
        binding.share.setOnClickListener {
            println("клик на share")
            post.sharesCount++
            updateUI(binding.like, binding.numberOfLikes, binding.numberOfShare)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // обновление числа лайков и смена иконки, числа шарингов
    private fun updateUI(likesButton: ImageButton, numberOfLikesTextView: TextView, numberOfSharesTextView: TextView) {
        if (post.isLiked) {
            likesButton.setImageResource(R.drawable.liked)
        } else {
            likesButton.setImageResource(R.drawable.like_2)
        }
        numberOfLikesTextView.text = formatCount(post.likesCount)
        numberOfSharesTextView.text = formatCount(post.sharesCount)
    }

    // форматирование числа
    private fun formatCount(count: Int): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fM", floor(count / 100_000.0) / 10)
            count >= 1_100 -> String.format("%.1fK", floor(count / 100.0) /10)
            count >= 1_000 -> String.format("%dK", count / 1_000)
            else -> count.toString()
        }
    }
}
