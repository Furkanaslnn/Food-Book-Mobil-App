package com.example.foodbook.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbook.databinding.FoodRecyclerRowBinding
import com.example.foodbook.model.Food
import com.example.foodbook.util.downloadImage
import com.example.foodbook.util.placeHolder
import com.example.foodbook.view.FoodListFragmentDirections

class FoodRecyclerAdapter(val foodList: ArrayList<Food>) :
    RecyclerView.Adapter<FoodRecyclerAdapter.FoodViewHolder>() {

    class FoodViewHolder(val binding: FoodRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding =
            FoodRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun foodListUpdate(newFoodList: List<Food>) {
        foodList.clear()
        foodList.addAll(newFoodList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.binding.name.text = foodList[position].name
        holder.binding.cal.text = foodList[position].cal
        holder.itemView.setOnClickListener {
            val action =
                FoodListFragmentDirections.actionFoodListFragmentToFoodDetailFragment(foodList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.imageView.downloadImage(
            foodList[position].image, placeHolder(holder.itemView.context)
        )
    }
}