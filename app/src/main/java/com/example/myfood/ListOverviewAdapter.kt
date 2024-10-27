package com.example.myfood

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ListOverviewAdapter(private val listFoodOverview: ArrayList<FoodOverview>) : RecyclerView.Adapter<ListOverviewAdapter.MyViewHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val photoOverview: ImageView = itemView.findViewById(R.id.img_item_photo_overview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_photo_overview, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFoodOverview.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (photoFoodOverview) = listFoodOverview[position]
        holder.photoOverview.setImageResource(photoFoodOverview)
        holder.itemView.setOnClickListener{
            onItemClickListener.onItemClicked(listFoodOverview[holder.adapterPosition])
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener{
        fun onItemClicked(data:FoodOverview)
    }

}