package com.app.bima.githubuser.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.bima.githubuser.ui.DetailUserActivity
import com.app.bima.githubuser.data.remote.response.ItemsItem
import com.app.bima.githubuser.databinding.ItemUserCardBinding
import com.app.bima.githubuser.utility.loadImage

class UserCardAdapter(private val listUser:ArrayList<ItemsItem>)
    :RecyclerView.Adapter<UserCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvUserName.text = listUser[position].login
        holder.binding.ivUserPhoto.loadImage(listUser[position].avatar_url)

        holder.itemView.setOnClickListener {
            val detailIntent = Intent(holder.itemView.context, DetailUserActivity::class.java)
            detailIntent.putExtra(DetailUserActivity.USERNAME, listUser[position].login)
            holder.itemView.context.startActivity(detailIntent)
        }

    }

    override fun getItemCount(): Int {
       return listUser.size
    }

   inner class ViewHolder(val binding:ItemUserCardBinding):RecyclerView.ViewHolder(binding.root)

}