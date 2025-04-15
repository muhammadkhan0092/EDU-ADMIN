package com.example.eduadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.eduadmin.R
import com.example.eduadmin.data.Course
import com.example.eduadmin.databinding.RvCoursesBinding

class CoursesAdapter : RecyclerView.Adapter<CoursesAdapter.CoursesViewHolder>(){

    inner class CoursesViewHolder(val binding : RvCoursesBinding) : ViewHolder(binding.root){
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
    ): CoursesViewHolder {
        return CoursesViewHolder(
            RvCoursesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding.apply {
           tvName.text = item.title
            if(!item.image.isNullOrBlank()){
                Glide.with(holder.itemView).load(item.image).into(imageEdit)
            }
            else
            {
                Glide.with(holder.itemView).load(R.drawable.testing).into(imageEdit)
            }
        }
        holder.binding.imageView.setOnClickListener {
            onDel?.invoke(item)
        }


    }


    var onDel : ((Course) -> Unit)? = null



}