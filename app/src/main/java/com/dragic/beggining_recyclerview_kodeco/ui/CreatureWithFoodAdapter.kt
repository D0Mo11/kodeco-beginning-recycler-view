package com.dragic.beggining_recyclerview_kodeco.ui

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.dragic.beggining_recyclerview_kodeco.databinding.ListItemCreatureWithFoodBinding
import com.dragic.beggining_recyclerview_kodeco.model.CreatureStore
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dragic.beggining_recyclerview_kodeco.model.Creature

class CreatureWithFoodAdapter(private val creatures: MutableList<Creature>, val onCreatureSelect: ((Int) -> Unit)) :
    ListAdapter<Creature, CreatureWithFoodAdapter.ViewHolder>(CreatureItemDiffCallback()) {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatureWithFoodAdapter.ViewHolder {
        val itemBinding = ListItemCreatureWithFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        itemBinding.foodRecyclerView.setRecycledViewPool(viewPool)
        LinearSnapHelper().attachToRecyclerView(itemBinding.foodRecyclerView)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CreatureWithFoodAdapter.ViewHolder, position: Int) {
        holder.bind(creatures[position])
    }

    override fun getItemCount(): Int = creatures.size

    inner class ViewHolder(private val itemBinding: ListItemCreatureWithFoodBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private lateinit var creature: Creature
        private val adapter = FoodAdapter(mutableListOf())

        init {
            itemBinding.root.setOnClickListener { onCreatureSelect.invoke(creature.id) }
        }

        fun bind(creature: Creature) {
            this.creature = creature
            val context = itemBinding.root.context
            itemBinding.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null, context.packageName))
            setupFoods()
        }

        private fun setupFoods() {
            itemBinding.foodRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            itemBinding.foodRecyclerView.adapter = adapter

            val foods = CreatureStore.getCreatureFoods(creature)
            adapter.addFoods(foods)
        }
    }
}