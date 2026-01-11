package com.example.groupassignment

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.example.groupassignment.model.Task
import com.google.gson.Gson
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BackupRepository(private val context: Context) {

    fun exportTasksToDownloads(tasks: List<Task>): Result<String> {
        return try {
            // 1. Convert the list of tasks to JSON text
            val gson = Gson()
            val jsonString = gson.toJson(tasks)

            // 2. Create a unique filename with a timestamp
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val filename = "TaskMaster_Backup_$timestamp.json"

            // 3. Set up the file details for MediaStore
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                // This saves it to the "Downloads/TaskMaster" folder on the phone
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/TaskMaster")
            }

            // 4. Insert the file entry into the phone's storage
            val resolver = context.contentResolver
            val uri: Uri? = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

            // 5. Open the file and write the JSON data into it
            if (uri != null) {
                val outputStream: OutputStream? = resolver.openOutputStream(uri)
                outputStream?.use { stream ->
                    stream.write(jsonString.toByteArray())
                }
                Result.success("Success! Saved to Downloads/TaskMaster/$filename")
            } else {
                Result.failure(Exception("Could not create file in MediaStore"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}