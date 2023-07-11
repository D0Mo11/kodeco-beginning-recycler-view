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

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dragic.beggining_recyclerview_kodeco.R
import com.dragic.beggining_recyclerview_kodeco.databinding.FragmentAllBinding
import com.dragic.beggining_recyclerview_kodeco.model.CreatureStore


class AllFragment : Fragment() {

    private lateinit var layoutManager: GridLayoutManager
    private lateinit var listItemDecoration: ItemDecoration
    private lateinit var gridItemDecoration: ItemDecoration
    private lateinit var listMenuItem: MenuItem
    private lateinit var gridMenuItem: MenuItem
    private var gridState = GridState.GRID
    private var _binding: FragmentAllBinding? = null

    private enum class GridState {
        LIST, GRID
    }

    private val binding get() = _binding!!
    private val adapter by lazy {
        CreatureCardAdapter(
            CreatureStore.getCreature().toMutableList()
        ) { startActivity(context?.let { context -> CreatureActivity.newIntent(context, it) }) }
    }

    companion object {
        fun newInstance(): AllFragment {
            return AllFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_all, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        listMenuItem = menu.findItem(R.id.action_span1)
        gridMenuItem = menu.findItem(R.id.action_span2)
        when (gridState) {
            GridState.LIST -> {
                listMenuItem.isEnabled = false
                gridMenuItem.isEnabled = true
            }

            GridState.GRID -> {
                listMenuItem.isEnabled = true
                gridMenuItem.isEnabled = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_span1 -> {
                gridState = GridState.LIST
                updateRecyclerView(1, listItemDecoration, gridItemDecoration)
                return true
            }

            R.id.action_span2 -> {
                gridState = GridState.GRID
                updateRecyclerView(2, gridItemDecoration, listItemDecoration)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return (adapter.spanSizeAtPosition(position))
            }
        }
        binding.creatureRecyclerView.layoutManager = layoutManager
        binding.creatureRecyclerView.adapter = adapter
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.creature_card_grid_layout_margin)
        listItemDecoration = SpacingItemDecoration(1, spacingInPixels)
        gridItemDecoration = SpacingItemDecoration(2, spacingInPixels)
        binding.creatureRecyclerView.addItemDecoration(gridItemDecoration)
        binding.creatureRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                adapter.scrollDirection = if (dy > 0) {
                    CreatureCardAdapter.ScrollDirection.DOWN
                } else {
                    CreatureCardAdapter.ScrollDirection.UP
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateRecyclerView(spanCount: Int, addItemDecoration: RecyclerView.ItemDecoration, removeItemDecoration: ItemDecoration) {
        layoutManager.spanCount = spanCount
        adapter.jupiterSpanSize = spanCount
        binding.creatureRecyclerView.removeItemDecoration(removeItemDecoration)
        binding.creatureRecyclerView.addItemDecoration(addItemDecoration)
    }
}
