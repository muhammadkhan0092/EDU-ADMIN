package com.example.eduadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.eduadmin.R
import com.example.eduadmin.databinding.RvMessagesBinding
import com.example.eduadmin.data.MessageModel

class SupportAdapter : RecyclerView.Adapter<SupportAdapter.SupportViewHolder>(){

    inner class SupportViewHolder(val binding : RvMessagesBinding) : ViewHolder(binding.root){
    }

    private val diffUtil =object : DiffUtil.ItemCallback<MessageModel>(){
        override fun areItemsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
            return oldItem=== newItem
        }

        override fun areContentsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SupportViewHolder {
        return SupportViewHolder(
            RvMessagesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SupportViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding.apply {
           tvName.text = item.userId
            textView2.text = item.messages.last().content
            if(!item.image.isNullOrBlank()){
                Glide.with(holder.itemView).load(item.image).into(imageEdit)
            }
            else
            {
                Glide.with(holder.itemView).load(R.drawable.testing).into(imageEdit)
            }
        }
        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }


    }


    var onClick : ((MessageModel) -> Unit)? = null



}