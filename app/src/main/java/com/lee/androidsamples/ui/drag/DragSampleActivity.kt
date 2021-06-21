package com.lee.androidsamples.ui.drag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lee.androidsamples.databinding.ActivityDragSampleBinding

class DragSampleActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityDragSampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDragSampleBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.dragLayout.setDragView(viewBinding.fab)
        viewBinding.fab.setOnClickListener {
            Toast.makeText(this, "Hello World!", Toast.LENGTH_SHORT).show()
        }
    }
}