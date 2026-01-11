package com.example.groupassignment.data

import android.content.Context
import com.example.groupassignment.model.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class InternalFileRepository(private val context: Context) : ITaskRepository {

    private val gson = Gson()
    private val fileName = "tasks.json"

    // Helper to get file reference
    private fun getFile(): File = File(context.filesDir, fileName)

    override fun getTasks(): List<Task> {
        val file = getFile()
        if (!file.exists()) return emptyList()

        return try {
            val jsonString = file.readText()
            val type = object : TypeToken<List<Task>>() {}.type
            gson.fromJson(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override fun saveTask(task: Task) {
        val currentList = getTasks().toMutableList()
        currentList.add(task)
        saveListToFile(currentList)
    }

    override fun updateTask(task: Task) {
        val currentList = getTasks().toMutableList()
        // Find the task with the matching ID
        val index = currentList.indexOfFirst { it.id == task.id }
        if (index != -1) {
            currentList[index] = task
            saveListToFile(currentList)
        }
    }

    override fun deleteTask(task: Task) {
        val currentList = getTasks().toMutableList()
        currentList.removeAll { it.id == task.id }
        saveListToFile(currentList)
    }

    private fun saveListToFile(list: List<Task>) {
        val jsonString = gson.toJson(list)
        getFile().writeText(jsonString)
    }
}