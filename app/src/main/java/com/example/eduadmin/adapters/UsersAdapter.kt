package com.example.eduadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.eduadmin.R
import com.example.eduadmin.data.User
import com.example.eduadmin.databinding.RvUsersItemBinding

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UsersViewHHolder>(){

    inner class UsersViewHHolder(val binding : RvUsersItemBinding) : ViewHolder(binding.root){
    }

    private val diffUtil =object : DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem== newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersViewHHolder {
        return UsersViewHHolder(
            RvUsersItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: UsersViewHHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding.apply {
            tvName.text = item.name
            if(!item.image.isNullOrBlank()){
                Glide.with(holder.itemView).load(item.image).into(imageEdit)
            }
            else
            {
                Glide.with(holder.itemView).load(R.drawable.testing).into(imageEdit)
            }
        }
        holder.binding.button.setOnClickListener {
            onClick?.invoke(item)
        }


    }


    var onClick : ((User) -> Unit)? = null



}