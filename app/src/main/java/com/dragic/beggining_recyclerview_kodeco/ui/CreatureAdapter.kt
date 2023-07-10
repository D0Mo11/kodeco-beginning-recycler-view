package com.dragic.beggining_recyclerview_kodeco.ui

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dragic.beggining_recyclerview_kodeco.R
import com.dragic.beggining_recyclerview_kodeco.app.inflate
import com.dragic.beggining_recyclerview_kodeco.databinding.ListItemCreatureBinding
import com.dragic.beggining_recyclerview_kodeco.model.Creature

class CreatureItemDiffCallback : DiffUtil.ItemCallback<Creature>() {
    override fun areItemsTheSame(oldItem: Creature, newItem: Creature): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Creature, newItem: Creature): Boolean = oldItem.id == newItem.id
}

class CreatureAdapter(private val creatures: MutableList<Creature>, val onCreatureSelect: ((Int) -> Unit)) :
    ListAdapter<Creature, CreatureAdapter.ViewHolder>(CreatureItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatureAdapter.ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_creature))
    }

    override fun onBindViewHolder(holder: CreatureAdapter.ViewHolder, position: Int) {
        holder.bind(creatures[position])
    }

    override fun getItemCount(): Int = creatures.size

    fun addCreatures(creatures: List<Creature>) {
        this.creatures.clear()
        this.creatures.addAll(creatures)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ListItemCreatureBinding = ListItemCreatureBinding.bind(itemView)
        private lateinit var creature: Creature

        init {
            itemView.setOnClickListener { onCreatureSelect.invoke(creature.id) }
        }

        fun bind(creature: Creature) {
            this.creature = creature
            val context = itemView.context
            binding.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null, context.packageName))
            binding.fullName.text = creature.fullName
            binding.nickname.text = creature.nickname
            animateView(binding.root)
        }

        private fun animateView(viewToAnimate: View) {
            if (viewToAnimate.animation == null) {
                val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.scale_xy)
                viewToAnimate.animation = animation
            }
        }
    }
}