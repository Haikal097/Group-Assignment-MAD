package com.example.groupassignment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onCheckedChanged: (Task, Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskVH>() {

    inner class TaskVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbDone: CheckBox = itemView.findViewById(R.id.cbDone)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvSubtitle: TextView = itemView.findViewById(R.id.tvTaskSubtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskVH(v)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskVH, position: Int) {
        val task = tasks[position]

        holder.tvTitle.text = task.title
        holder.tvSubtitle.text = task.subtitle

        holder.cbDone.setOnCheckedChangeListener(null)
        holder.cbDone.isChecked = task.isCompleted

        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChanged(task, isChecked)
            notifyItemChanged(position)
        }
    }

    fun updateData(newList: List<Task>) {
        tasks.clear()
        tasks.addAll(newList)
        notifyDataSetChanged()
    }
}
