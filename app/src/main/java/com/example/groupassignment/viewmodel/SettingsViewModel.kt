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


}