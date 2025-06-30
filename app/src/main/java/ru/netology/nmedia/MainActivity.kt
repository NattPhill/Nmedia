package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels() // Создаем ViewModel
        val newPostLauncher = registerForActivityResult(NewPostContract) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.onLikeClicked(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }

                val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))

                startActivity(shareIntent)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                showEditPanel(binding, post.content)
            }

        })

        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)



        binding.save.setOnClickListener {
            newPostLauncher.launch()
        }


//        binding.cancelButton.setOnClickListener {
//            binding.editingPanel.visibility = View.GONE
//            viewModel.cancelEdit()
//        }
//////////////////////////////////
//        viewModel.edited.observe(this) { post ->
//            if (post.id == 0L) {
//                return@observe
//            }
//
//            binding.editingPanel.visibility = View.VISIBLE
//
//            val cancelEditing: View = binding.editingPanel
//            val cancelButton: ImageButton = binding.cancelButton
//                /////////////////////////
//            cancelButton.setOnClickListener {
//                binding.editingPanel.visibility = View.GONE     //скрыть окно
//                AndroidUtils.hideKeyboard(cancelEditing)
//                viewModel.cancelEdit()     //отмена редактирования
//            }
//        }
/////////////////////////////////////////////
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
