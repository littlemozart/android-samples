package com.lee.androidsamples

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lee.androidsamples.databinding.ActivityMainBinding
import com.lee.androidsamples.ui.drag.DragSampleActivity

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private val list by lazy { arrayListOf("Drag") }
    private val adapter by lazy { MainListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.contentList.adapter = adapter
        adapter.submitList(list)
        adapter.onClick {
            when (it) {
                0 -> {
                    startActivity(Intent(this, DragSampleActivity::class.java))
                }
                1 -> {

                }
                // ...
            }
        }
    }

    class MainListAdapter : ListAdapter<String, MainItemViewHolder>(diff) {
        companion object {
            val diff = object : DiffUtil.ItemCallback<String>() {
                override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem === newItem
                }

                override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem == newItem
                }
            }
        }

        private var adapterOnClick: ((position: Int) -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainItemViewHolder {
            return MainItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
            )
        }

        override fun onBindViewHolder(holder: MainItemViewHolder, position: Int) {
            holder.textView.text = getItem(position)
            holder.textView.setOnClickListener {
                adapterOnClick?.invoke(position)
            }
        }

        fun onClick(listener: ((position: Int) -> Unit)) {
            adapterOnClick = listener
        }
    }

    class MainItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView: TextView = v.findViewById(android.R.id.text1)
    }
}