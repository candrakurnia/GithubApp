package com.project.githubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.githubapp.databinding.ItemRecycleViewBinding
import com.project.githubapp.model.User

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRecycleViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .circleCrop()
                    .into(imgItemPhoto)
                tvUsername.text = user.login

            }
        }

    }

    private val list = ArrayList<User>()

    fun setList(users: ArrayList<User>) {
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRecycleViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(list[position])
        viewHolder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(list[viewHolder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = list.size


}