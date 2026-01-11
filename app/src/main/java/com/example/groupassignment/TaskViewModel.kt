package com.example.groupassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupassignment.data.ITaskRepository
import com.example.groupassignment.model.Task
import java.util.UUID

class TaskViewModel(private val repository: ITaskRepository) : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    fun loadTasks() {
        _tasks.value = repository.getTasks()
    }

    fun addTask(title: String, subtitle: String) {
        // FIX: We generate the ID and default isCompleted here so you don't get "No value passed" errors
        val newTask = Task(
            id = UUID.randomUUID().toString(),
            title = title,
            subtitle = subtitle,
            isCompleted = false
        )
        repository.saveTask(newTask)
        loadTasks()
    }

    fun toggleTaskCompletion(task: Task) {
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        repository.updateTask(updatedTask)
        loadTasks()
    }
}