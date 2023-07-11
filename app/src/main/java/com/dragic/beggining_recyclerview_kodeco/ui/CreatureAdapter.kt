package com.dragic.beggining_recyclerview_kodeco.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dragic.beggining_recyclerview_kodeco.R
import com.dragic.beggining_recyclerview_kodeco.databinding.ListItemCreatureBinding
import com.dragic.beggining_recyclerview_kodeco.databinding.ListItemPlanetHeaderBinding
import com.dragic.beggining_recyclerview_kodeco.model.CompositeItem
import com.dragic.beggining_recyclerview_kodeco.model.Creature
import java.lang.IllegalArgumentException

class CreatureItemDiffCallback : DiffUtil.ItemCallback<Creature>() {
    override fun areItemsTheSame(oldItem: Creature, newItem: Creature): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Creature, newItem: Creature): Boolean = oldItem.id == newItem.id
}

class CreatureAdapter(private val compositeItems: MutableList<CompositeItem>, val onCreatureSelect: ((Int) -> Unit)) :
    ListAdapter<Creature, RecyclerView.ViewHolder>(CreatureItemDiffCallback()) {

    enum class ViewType {
        HEADER, CREATURE,
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.HEADER.ordinal -> HeaderViewHolder(ListItemPlanetHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ViewType.CREATURE.ordinal -> CreatureViewHolder(ListItemCreatureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bindHeader(compositeItems[position])
            is CreatureViewHolder -> holder.bindCreature(compositeItems[position])
        }
    }

    override fun getItemCount(): Int = compositeItems.size

    override fun getItemViewType(position: Int): Int {
        return if (compositeItems[position].isHeader) {
            ViewType.HEADER.ordinal
        } else {
            ViewType.CREATURE.ordinal
        }
    }

    fun addCreatures(creatures: List<CompositeItem>) {
        this.compositeItems.clear()
        this.compositeItems.addAll(creatures)
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(private val itemBinding: ListItemPlanetHeaderBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bindHeader(compositeItem: CompositeItem) {
            itemBinding.headerName.text = compositeItem.header.name
        }
    }

    inner class CreatureViewHolder(private val itemBinding: ListItemCreatureBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private lateinit var creature: Creature

        init {
            itemBinding.root.setOnClickListener { onCreatureSelect.invoke(creature.id) }
        }

        fun bindCreature(compositeItem: CompositeItem) {
            creature = compositeItem.creature
            val context = itemBinding.root.context
            itemBinding.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null, context.packageName))
            itemBinding.fullName.text = creature.fullName
            itemBinding.nickname.text = creature.nickname
            animateView(itemBinding.root)
        }

        private fun animateView(viewToAnimate: View) {
            if (viewToAnimate.animation == null) {
                val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.scale_xy)
                viewToAnimate.animation = animation
            }
        }
    }
}
