package ru.netology.nmedia

import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardPostBinding
import kotlin.math.floor

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            numberOfLikes.text = formatCount(post.likesCount)
            numberOfShare.text = formatCount(post.sharesCount)
            like.setImageResource(if (post.likedByMe) R.drawable.liked else R.drawable.like_2)

            like.setOnClickListener { onInteractionListener.onLike(post) }
            share.setOnClickListener { onInteractionListener.onShare(post) }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_options)
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.edit -> {
                                //логика для редактирования поста
                                onInteractionListener.onEdit(post)
                                true
                            }

                            R.id.remove -> {
                                //логика для удаления поста
                                onInteractionListener.onRemove(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
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
