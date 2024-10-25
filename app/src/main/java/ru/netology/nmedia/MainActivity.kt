package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import ru.netology.nmedia.databinding.ActivityMainBinding
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PostViewModel by viewModels() // Создаем ViewModel

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

        // Подписываемся на изменения в данных
        viewModel.post.observe(this, Observer { post ->
            updateUI(post)
        })

        // Добавляем обработчики кликов
        binding.like.setOnClickListener {
            viewModel.onLikeClicked() // Вызываем обработку клика на лайк
        }

        binding.share.setOnClickListener {
            viewModel.onShareClicked() // Вызываем обработку клика на share
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Обновляем UI при изменении данных
    fun updateUI(post: Post) {
        binding.like.setImageResource(if (post.likedByMe) R.drawable.liked else R.drawable.like_2)
        binding.numberOfLikes.text = formatCount(post.likesCount)
        binding.numberOfShare.text = formatCount(post.sharesCount)
    }

    // форматирование числа
    private fun formatCount(count: Int): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fM", floor(count / 100_000.0) / 10)
            count >= 10_000 -> String.format("%dK", count / 1_000)
            count >= 1_000 -> String.format("%.1fK", floor(count / 100.0) / 10)
            else -> count.toString()
        }
    }
}
