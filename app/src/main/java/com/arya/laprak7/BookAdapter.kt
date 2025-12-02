package com.arya.laprak7

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // Import Glide
import com.arya.laprak7.databinding.ItemBookBinding

class BookAdapter(
    private var list: List<Book>,
    private var onClick: (Book, String) -> Unit
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(var binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book, position: Int) {
            binding.apply {
                tvCardTitle.text = book.title
                tvCardPage.text = "${book.pages} Pages"
                tvCardRelease.text = "Released: ${book.releaseDate}"

                val imageUrl = BookDataHelper.getCoverUrl(position)

                Glide.with(itemView.context)
                    .load(imageUrl)
                    .into(imgBookCover)

                root.setOnClickListener {
                    onClick(book, imageUrl)
                }
            }
        }
    }
}