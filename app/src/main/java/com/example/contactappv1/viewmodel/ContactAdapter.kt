package com.example.contactappv1.viewmodel

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contactappv1.R
import com.example.contactappv1.databinding.ContactItemBinding
import com.example.contactappv1.model.DataUser

class ContactAdapter(
    private val users: MutableList<DataUser>,
    val itemClick: (Int) -> Unit
    ): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val user = users[position]
        holder.onHolder(user)
    }

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(private val binding: ContactItemBinding): RecyclerView.ViewHolder(binding.root){
        fun onHolder(user: DataUser){
            Glide.with(binding.root.context)
                .load(user.srcAvatar)
                .fallback(R.drawable.avatar_default)
                .into(binding.sivAvatar)
            binding.tvName.text = user.name
            itemView.setOnClickListener {
                itemClick(user.id)
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newUsers: List<DataUser>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }
}