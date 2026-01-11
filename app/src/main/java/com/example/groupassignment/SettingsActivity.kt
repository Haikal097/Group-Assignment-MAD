package com.example.groupassignment

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.lifecycleScope
import com.example.groupassignment.viewmodel.SettingsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private val vm: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sw = findViewById<SwitchCompat>(R.id.switchDarkMode)
        val btnBackup = findViewById<Button>(R.id.btnBackup)

        // Read from DataStore -> update switch UI (without loops)
        lifecycleScope.launch {
            vm.darkMode.collectLatest { enabled ->
                if (sw.isChecked != enabled) {
                    sw.isChecked = enabled
                }
            }
        }

        // Switch toggled -> save to DataStore + apply theme instantly
        sw.setOnCheckedChangeListener { _, isChecked ->
            vm.setDarkMode(isChecked)
            ThemeController.applyDarkMode(isChecked)
        }

        // Role C will connect export logic later
        btnBackup.setOnClickListener {
            // Placeholder: Do nothing for now (Role C)
            // You can show a Toast if you want, but not required.
        }
    }
}
