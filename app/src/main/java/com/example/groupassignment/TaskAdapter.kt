package com.example.groupassignment

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groupassignment.model.Task

class TaskAdapter(
    // The error "Argument type mismatch" happened because MainActivity expected this signature:
    private val onTaskClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var tasks: List<Task> = emptyList()

    // This fixes "Unresolved reference 'setTasks'"
    fun setTasks(newTasks: List<Task>) {
        this.tasks = newTasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tvSubtitle)
        private val cbTask: CheckBox = itemView.findViewById(R.id.cbTask)

        fun bind(task: Task) {
            tvTitle.text = task.title
            tvSubtitle.text = task.subtitle

            // Prevent infinite loops when recycling views
            cbTask.setOnCheckedChangeListener(null)
            cbTask.isChecked = task.isCompleted

            // Strikethrough if completed
            if (task.isCompleted) {
                tvTitle.paintFlags = tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvTitle.paintFlags = tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            // Handle Clicks
            cbTask.setOnCheckedChangeListener { _, _ ->
                onTaskClick(task)
            }
        }
    }
}