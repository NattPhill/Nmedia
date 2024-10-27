package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels() // Создаем ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(
            onLikeClicked = { post -> viewModel.onLikeClicked(post.id) },
            onShareClicked = { post -> viewModel.onShareClicked(post.id) }
        )

        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)

        // Подписываемся на изменения в списке постов
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }
}
