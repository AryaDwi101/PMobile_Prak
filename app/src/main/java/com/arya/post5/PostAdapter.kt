package com.arya.post5

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arya.post5.databinding.ItemPostBinding

class PostAdapter(private val postList: List<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    interface OnPostOptionsClickListener {
        fun onEditClick(post: Post, position: Int)
    }
    private var optionsListener: OnPostOptionsClickListener? = null
    fun setOnPostOptionsClickListener(listener: OnPostOptionsClickListener) {
        this.optionsListener = listener
    }
    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.ivPostProfile.setImageResource(post.profileImageResId)
            binding.tvPostUsername.text = post.username
            when (post.postImage) {
                is Int -> binding.ivPostImage.setImageResource(post.postImage)
                is Uri -> binding.ivPostImage.setImageURI(post.postImage)
            }
            binding.tvPostCaption.text = post.caption
            binding.ivPostOptions.setOnClickListener {
                optionsListener?.onEditClick(post, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postList[position])
    }
}