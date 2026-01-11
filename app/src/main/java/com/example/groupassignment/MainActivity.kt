package com.example.groupassignment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groupassignment.data.InternalFileRepository
import com.example.groupassignment.datastore.SettingsDataStore
import com.example.groupassignment.viewmodel.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Role B Variables
    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        // --- ROLE A: THEME LOGIC (Keep this at the top) ---
        // Apply theme BEFORE setContentView to avoid "flashbang" effect
        val ds = SettingsDataStore(applicationContext)
        lifecycleScope.launch {
            val enabled = ds.darkModeFlow.first()
            ThemeController.applyDarkMode(enabled)
        }
        // --------------------------------------------------

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- ROLE B: INIT VIEWMODEL & REPOSITORY ---
        val repository = InternalFileRepository(this)
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TaskViewModel(repository) as T
            }
        }
        viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]

        // --- ROLE B: SETUP RECYCLERVIEW ---
        val rvTasks = findViewById<RecyclerView>(R.id.rvTasks)
        rvTasks.layoutManager = LinearLayoutManager(this)

        // Initialize Adapter with click listener
        adapter = TaskAdapter { task ->
            viewModel.toggleTaskCompletion(task)
        }
        rvTasks.adapter = adapter

        // Observe Data from ViewModel
        viewModel.tasks.observe(this) { taskList ->
            adapter.setTasks(taskList)
        }
        viewModel.loadTasks() // Load initial data from file

        // --- NAVIGATION & UI BUTTONS ---

        // Settings Button (Role A/Navigation)
        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }

        // Add Task Button (Role B)
        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            showAddTaskDialog()
        }

        // Sort Spinner (UI Layout requirement)
        setupSpinner()
    }

    // --- HELPER FUNCTIONS ---

    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etDialogTitle)
        val etSubtitle = dialogView.findViewById<EditText>(R.id.etDialogSubtitle)

        AlertDialog.Builder(this)
            .setTitle("New Task")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = etTitle.text.toString()
                val subtitle = etSubtitle.text.toString()
                if (title.isNotEmpty()) {
                    viewModel.addTask(title, subtitle)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupSpinner() {
        val spinnerSort = findViewById<Spinner>(R.id.spinnerSort)
        val sortOptions = listOf("Sort by Date", "Sort by Title")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSort.adapter = spinnerAdapter
    }
}