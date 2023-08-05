package com.example.contactlistappproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.contactlistappproject.databinding.ItemContactBinding
import com.example.contactlistappproject.db.ContactEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactAdapter @Inject constructor(): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflateItem= ItemContactBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ContactViewHolder(inflateItem)
    }
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ContactViewHolder(
        private val binding: ItemContactBinding
        ): RecyclerView.ViewHolder(binding.root){

            fun bind(item : ContactEntity){
                binding.apply {
                    tvName.text = item.name
                    tvNumber.text = item.number
                }
            }

    }


    private val differCallback = object : DiffUtil.ItemCallback<ContactEntity>(){
        override fun areItemsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)
}