package com.example.taskup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var btnAddTask: MaterialButton
    private lateinit var rvTasks: RecyclerView
    private lateinit var layoutEmptyState: LinearLayout
    private lateinit var bottomNavigation: BottomNavigationView

    private val taskList = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAddTask = findViewById(R.id.btnAddTask)
        rvTasks = findViewById(R.id.rvTasks)
        layoutEmptyState = findViewById(R.id.layoutEmptyState)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        setupRecyclerView()
        setupBottomNavigation()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(taskList) { position ->
            if (position != RecyclerView.NO_POSITION && position < taskList.size) {
                taskList.removeAt(position)
                TaskRepository.tasks.clear()
                TaskRepository.tasks.addAll(taskList)
                taskAdapter.notifyItemRemoved(position)
                updateEmptyState()
            }
        }

        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = taskAdapter
    }

    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.nav_home

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true

                R.id.nav_calendar -> {
                    startActivity(Intent(this, CalendarActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_add_task -> {
                    startActivity(Intent(this, AddTaskActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun setupClickListeners() {
        btnAddTask.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }

    private fun loadTasks() {
        taskList.clear()
        taskList.addAll(TaskRepository.tasks)
        taskAdapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (taskList.isEmpty()) {
            layoutEmptyState.visibility = View.VISIBLE
            rvTasks.visibility = View.GONE
        } else {
            layoutEmptyState.visibility = View.GONE
            rvTasks.visibility = View.VISIBLE
        }
    }
}