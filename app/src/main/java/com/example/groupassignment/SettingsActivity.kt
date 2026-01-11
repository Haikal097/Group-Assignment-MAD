package com.example.groupassignment

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.lifecycleScope
import com.example.groupassignment.viewmodel.SettingsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private val vm: SettingsViewModel by viewModels()
//    private lateinit var backupRepository: BackupRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

//        backupRepository = BackupRepository(this)

        // --- FIX: Using the IDs from YOUR XML ---
        val sw = findViewById<SwitchCompat>(R.id.switchDarkMode)
//        val btnBackup = findViewById<Button>(R.id.btnBackup)

        // Dark Mode Logic
        lifecycleScope.launch {
            vm.darkMode.collectLatest { enabled ->
                if (sw != null && sw.isChecked != enabled) {
                    sw.isChecked = enabled
                }
            }
        }

        sw?.setOnCheckedChangeListener { _, isChecked ->
            vm.setDarkMode(isChecked)
            ThemeController.applyDarkMode(isChecked)
        }


    }
}