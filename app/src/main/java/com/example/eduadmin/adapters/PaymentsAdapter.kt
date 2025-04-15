package com.example.eduadmin.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.eduadmin.R
import com.example.eduadmin.data.Course
import com.example.eduadmin.databinding.RvPaymentsBinding

class PaymentsAdapter : RecyclerView.Adapter<PaymentsAdapter.PaymentsViewHolder>(){

    inner class PaymentsViewHolder(val binding : RvPaymentsBinding) : ViewHolder(binding.root){
    }

    private val diffUtil =object : DiffUtil.ItemCallback<Course>(){
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.courseId=== newItem.courseId
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentsViewHolder {
        return PaymentsViewHolder(
            RvPaymentsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PaymentsViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding.apply {
           tvName.text = item.title
            Log.d("khan","adapting ${item.sold-item.paid}")
            textView2.text = "Rs " + item.price + "(${item.sold-item.paid})"
            Log.d("khan","image is ${item.image}")
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


    var onClick : ((Course) -> Unit)? = null



}