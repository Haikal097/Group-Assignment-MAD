package com.example.groupassignment.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.groupassignment.BackupRepository
import com.example.groupassignment.datastore.SettingsDataStore
import com.example.groupassignment.model.Task
import kotlinx.coroutines.Dispatchers // Fixes 'Unresolved reference Dispatchers'
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext // Fixes 'Unresolved reference withContext'

class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val ds = SettingsDataStore(app.applicationContext)

    val darkMode: StateFlow<Boolean> =
        ds.darkModeFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            ds.setDarkMode(enabled)
        }
    }

    // --- EXPORT LOGIC (Role C) ---
    private val _exportStatus = MutableLiveData<String>()
    val exportStatus: LiveData<String> = _exportStatus

    fun backupData(repository: BackupRepository) {
        viewModelScope.launch(Dispatchers.IO) {

            // Mock Data for Testing
            val dummyTasks = listOf(
                Task(id="1", title = "Backup Test 1", subtitle = "Tomorrow", isCompleted = false),
                Task(id="2", title = "Backup Test 2", subtitle = "Urgent", isCompleted = true)
            )

            // Fixes "Unresolved reference 'exportTasksToDownloads'"
            val result = repository.exportTasksToDownloads(dummyTasks)

            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _exportStatus.value = result.getOrNull()
                } else {
                    _exportStatus.value = "Error: ${result.exceptionOrNull()?.message}"
                }
            }
        }
    }

}