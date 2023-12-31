/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.dragic.beggining_recyclerview_kodeco.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.dragic.beggining_recyclerview_kodeco.R
import com.dragic.beggining_recyclerview_kodeco.databinding.ActivityCreatureBinding
import com.dragic.beggining_recyclerview_kodeco.model.Favorites
import com.dragic.beggining_recyclerview_kodeco.model.Creature
import com.dragic.beggining_recyclerview_kodeco.model.CreatureStore

class CreatureActivity : AppCompatActivity() {

  private lateinit var creature: Creature
  private lateinit var binding: ActivityCreatureBinding

  companion object {
    private const val EXTRA_CREATURE_ID = "EXTRA_CREATURE_ID"

    fun newIntent(context: Context, creatureId: Int): Intent {
      val intent = Intent(context, CreatureActivity::class.java)
      intent.putExtra(EXTRA_CREATURE_ID, creatureId)
      return intent
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityCreatureBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupCreature()
    setupTitle()
    setupViews()
    setupFavoriteButton()
  }

  private fun setupCreature() {
    val creatureById = CreatureStore.getCreatureById(intent.getIntExtra(EXTRA_CREATURE_ID, 1))
    if (creatureById == null) {
      Toast.makeText(this, getString(R.string.invalid_creature), Toast.LENGTH_SHORT).show()
      finish()
    } else {
      creature = creatureById
    }
  }

  private fun setupTitle() {
    title = String.format(getString(R.string.detail_title_format), creature.nickname)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  private fun setupViews() {
    binding.headerImage.setImageResource(resources.getIdentifier(creature.uri, null, packageName))
    binding.fullName.text = creature.fullName
    binding.planet.text = creature.planet
  }

  private fun setupFavoriteButton() {
    setupFavoriteButtonImage(creature)
    setupFavoriteButtonClickListener(creature)
  }

  private fun setupFavoriteButtonImage(creature: Creature) {
    if (creature.isFavorite) {
      binding.favoriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_black_24dp))
    } else {
      binding.favoriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_black_24dp))
    }
  }

  private fun setupFavoriteButtonClickListener(creature: Creature) {
    binding.favoriteButton.setOnClickListener { _ ->
      if (creature.isFavorite) {
        binding.favoriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_black_24dp))
        Favorites.removeFavorite(creature, this)
      } else {
        binding.favoriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_black_24dp))
        Favorites.addFavorite(creature, this)
      }
    }
  }
}
