package com.aksoyh.mvvmwithkotlincoroutine.ui.main.fragments

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.aksoyh.mvvmwithkotlincoroutine.R
import com.aksoyh.mvvmwithkotlincoroutine.data.UserList

import com.aksoyh.mvvmwithkotlincoroutine.ui.main.fragments.dummy.DummyContent.DummyItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_item.view.*

class MyItemRecyclerViewAdapter : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val differCallback = object : DiffUtil.ItemCallback<UserList>() {
        override fun areItemsTheSame(oldItem: UserList, newItem: UserList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserList, newItem: UserList): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = differ.currentList[position]
        holder.itemView.apply {
            val imageUrl = user.picture
            Glide.with(context).load(imageUrl).into(user_photo)
            user_name.text = "${user.firstName} ${user.lastName}"
            user_email.text = user.email
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(user.id)
        }
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }
}