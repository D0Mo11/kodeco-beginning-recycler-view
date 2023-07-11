package com.dragic.beggining_recyclerview_kodeco.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dragic.beggining_recyclerview_kodeco.R
import com.dragic.beggining_recyclerview_kodeco.app.inflate
import com.dragic.beggining_recyclerview_kodeco.databinding.ListItemCreatureBinding
import com.dragic.beggining_recyclerview_kodeco.databinding.ListItemCreatureCardBinding
import com.dragic.beggining_recyclerview_kodeco.databinding.ListItemCreatureWithFoodBinding
import com.dragic.beggining_recyclerview_kodeco.model.Creature

class CreatureCardAdapter(private val creatures: MutableList<Creature>, val onCreatureSelect: ((Int) -> Unit)) :
    ListAdapter<Creature, CreatureCardAdapter.ViewHolder>(CreatureItemDiffCallback()) {
    enum class Scrolldirection {
        UP, DOWN
    }

    var scrollDirection = Scrolldirection.DOWN

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatureCardAdapter.ViewHolder {
        val itemBinding = ListItemCreatureCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CreatureCardAdapter.ViewHolder, position: Int) {
        holder.bind(creatures[position])
    }

    override fun getItemCount(): Int = creatures.size

    inner class ViewHolder(private val itemBinding: ListItemCreatureCardBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private lateinit var creature: Creature

        init {
            itemBinding.root.setOnClickListener { onCreatureSelect.invoke(creature.id) }
        }

        fun bind(creature: Creature) {
            this.creature = creature
            val context = itemBinding.root.context
            val imageResource = context.resources.getIdentifier(creature.uri, null, context.packageName)
            itemBinding.creatureImage.setImageResource(imageResource)
            itemBinding.fullName.text = creature.fullName
            setBackgroundColors(context, imageResource)
            animateView(itemBinding.root)
        }

        private fun setBackgroundColors(context: Context, imageResource: Int) {
            val image = BitmapFactory.decodeResource(context.resources, imageResource)
            Palette.from(image).generate() { palette ->
                palette?.let {
                    val backgroundColor = it.getDominantColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    itemBinding.creatureCard.setCardBackgroundColor(backgroundColor)
                    itemBinding.nameHolder.setBackgroundColor(backgroundColor)
                    val textColor = if (isColorDark(backgroundColor)) Color.WHITE else Color.BLACK
                    itemBinding.fullName.setTextColor(textColor)
                }
            }
        }

        private fun isColorDark(color: Int): Boolean {
            val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
            return darkness >= 0.5
        }

        private fun animateView(viewToAnimate: View) {
            if (viewToAnimate.animation == null) {
                val animId = if (scrollDirection == Scrolldirection.DOWN) R.anim.slide_from_bottom else R.anim.slide_from_top
                val animation = AnimationUtils.loadAnimation(viewToAnimate.context, animId)
                viewToAnimate.animation = animation
            }
        }
    }
}