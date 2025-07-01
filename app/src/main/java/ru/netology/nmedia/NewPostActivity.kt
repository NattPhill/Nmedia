package ru.netology.nmedia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textToEdit = intent.getStringExtra("text")
        val postId = intent.getLongExtra("id", 0L)

        if(!textToEdit.isNullOrBlank()) {
            binding.edit.setText(textToEdit)
        }

        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                val resultIntent = Intent().apply {
                    putExtra("text", text)
                    putExtra("id", postId)
                }
                setResult(RESULT_OK, resultIntent)
            }
            finish()
        }
    }
}

object NewPostContract: ActivityResultContract<Pair<Long, String>?,  Pair<Long, String>?>() {
    override fun createIntent(context: Context, input: Pair <Long, String>?): Intent {
        return Intent(context, NewPostActivity::class.java).apply {
        putExtra("id", input?.first ?: 0L)
            putExtra("text", input?.second ?: "")
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Long, String>? {
        if (resultCode != Activity.RESULT_OK) return null
        val text = intent?.getStringExtra("text") ?: return null
        val id = intent.getLongExtra("id", 0L)
        return id to text
    }
}