package com.seook.api_example.rv

import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seook.api_example.App
import com.seook.api_example.R
import com.seook.api_example.model.Photo
import org.w3c.dom.Text

class PhotoItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    //뷰들을 가져온다.
    private val photoImageView = itemView.findViewById<ImageView>(R.id.photoImage)
    private val photoCreatedAtText = itemView.findViewById<TextView>(R.id.createdAttext)
    private val photoLikesCountText = itemView.findViewById<TextView>(R.id.likesCountText)

    //data와 뷰를 묶는다.
    fun bindWithView(photoItem: Photo){

        photoCreatedAtText.text = photoItem.createdAt
        photoLikesCountText.text = photoItem.likesCount.toString()

        Glide.with(App.instance)
            .load(photoItem.thumbnail)
            .placeholder(R.drawable.ic_baseline_add_24)
            .into(photoImageView)



    }
}