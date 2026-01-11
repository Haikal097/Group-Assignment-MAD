package com.example.groupassignment.data

import com.example.groupassignment.model.Task

interface ITaskRepository {
    fun getTasks(): List<Task>
    fun saveTask(task: Task)
    fun updateTask(task: Task)
    fun deleteTask(task: Task)
}