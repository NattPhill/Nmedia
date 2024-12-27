package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
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

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.onLikeClicked(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.onShareClicked(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                showEditPanel(binding, post.content)
            }

        })

        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)

        binding.save.setOnClickListener {
            with(binding.newContent) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Контент не может быть пустым",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.changeContent(text.toString())
                viewModel.save()
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                binding.editingPanel.visibility = View.GONE
            }
        }

        binding.cancelButton.setOnClickListener {
            binding.editingPanel.visibility = View.GONE
            viewModel.cancelEdit()
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.newContent) {
                focusAndShowKeyboard()
                setText(post.content)
            }
            binding.editingPanel.visibility = View.VISIBLE

            val cancelEditing: View = findViewById(R.id.editingPanel)
            val cancelButton: ImageButton = findViewById(R.id.cancelButton)

            cancelButton.setOnClickListener {
                binding.editingPanel.visibility = View.GONE     //скрыть окно
                binding.newContent.setText("") // Очищаем текстовое поле
                binding.newContent.clearFocus()
                AndroidUtils.hideKeyboard(cancelEditing)
                viewModel.cancelEdit()     //отмена редактирования
            }
        }

        // Подписываемся на изменения в списке постов
        viewModel.data.observe(this) { posts ->
            val new = adapter.currentList.size < posts.size
            adapter.submitList(posts) {
                if (new) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }
    }

    private fun showEditPanel(binding: ActivityMainBinding, postContent: String) {
        binding.editingPanel.visibility = View.VISIBLE
        binding.editionText.text = postContent
    }
}
