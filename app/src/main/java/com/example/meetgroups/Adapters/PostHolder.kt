package com.example.meetgroups.Adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.meetgroups.DataModels.Post
import kotlinx.android.synthetic.main.post_card_item.view.*

class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(info: Post){
        with(info){
            itemView.post_owner.text = owner
            itemView.post_description.text = descr
            itemView.post_date.text = date
        }
    }

}