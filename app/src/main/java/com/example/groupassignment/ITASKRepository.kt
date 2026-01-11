package com.example.groupassignment

import kotlinx.coroutines.flow.Flow

interface ITaskRepository {
    // Role B Methods (Internal Storage)
    suspend fun getTasks(): List<Task>
    suspend fun saveTasks(tasks: List<Task>)
    suspend fun addTask(task: Task)

    // Role A will add their Settings/Dark Mode methods here later
    // Role C will use getTasks() to export data
}