package com.dragic.beggining_recyclerview_kodeco.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dragic.beggining_recyclerview_kodeco.R
import com.dragic.beggining_recyclerview_kodeco.app.inflate
import com.dragic.beggining_recyclerview_kodeco.databinding.ListItemFoodBinding
import com.dragic.beggining_recyclerview_kodeco.model.Food

class FoodItemDiffCallback : DiffUtil.ItemCallback<Food>() {
    override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean = oldItem.id == newItem.id
}

class FoodAdapter(private val foods: MutableList<Food>) : ListAdapter<Food, FoodAdapter.ViewHolder>(FoodItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_food))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(foods[position])
    }

    override fun getItemCount(): Int = foods.size


    fun addFoods(foods: List<Food>) {
        this.foods.clear()
        this.foods.addAll(foods)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ListItemFoodBinding = ListItemFoodBinding.bind(itemView)
        private lateinit var food: Food

        fun bind(food: Food) {
            this.food = food
            val context = itemView.context
            binding.foodImage.setImageResource(context.resources.getIdentifier(food.thumbnail, null, context.packageName))
        }
    }

}