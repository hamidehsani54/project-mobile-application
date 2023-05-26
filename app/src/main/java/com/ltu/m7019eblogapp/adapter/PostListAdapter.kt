package com.ltu.m7019eblogapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ltu.m7019eblogapp.databinding.PostPreviewCardBinding
import com.ltu.m7019eblogapp.model.Post
class PostListAdapter(private val postClickListener: PostClickListener) : ListAdapter<Post, PostListAdapter.ViewHolder>(PostListDiffCallback()) {
    // ViewHolder class that holds the view elements for each item in the RecyclerView
    class ViewHolder(private var binding: PostPreviewCardBinding) : RecyclerView.ViewHolder(binding.root) {
        // bind function to bind data to the view elements
        fun bind(post: Post, postClickListener: PostClickListener){
            // Assign the post object to the "post" variable in the layout
            binding.post = post
            // Assign the postClickListener to the "clickListener" variable in the layout
            binding.clickListener = postClickListener
            // Ensures that pending data bindings are executed immediately
            binding.executePendingBindings()
        }

        companion object {
            // Create a ViewHolder instance from the provided ViewGroup
            fun from(parent: ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PostPreviewCardBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    // Create and inflate the ViewHolder when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // Bind the data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), postClickListener)
    }
}

// DiffUtil.ItemCallback implementation for comparing items in the ListAdapter
class PostListDiffCallback : DiffUtil.ItemCallback<Post>(){

    // Check if two items represent the same object in the list
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    // Check if the contents of two items are the same
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}

// Custom click listener for the posts in the list
class PostClickListener(val clickListener: (post: Post) -> Unit) {
    // Function to handle the click event on a post
    fun onClick(post: Post) = clickListener(post)
}
