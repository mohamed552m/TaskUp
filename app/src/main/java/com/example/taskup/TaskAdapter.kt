package com.example.taskup

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val taskList: MutableList<Task>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBoxTask: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val tvTaskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvTaskDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        val ivDeleteTask: ImageView = itemView.findViewById(R.id.ivDeleteTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        holder.tvTaskTitle.text = task.title
        holder.tvTaskDescription.text = task.description

        holder.tvTaskDescription.visibility =
            if (task.description.isBlank()) View.GONE else View.VISIBLE

        holder.checkBoxTask.setOnCheckedChangeListener(null)
        holder.checkBoxTask.isChecked = task.isCompleted

        applyTaskStyle(holder, task.isCompleted)

        holder.checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            applyTaskStyle(holder, isChecked)
        }

        holder.ivDeleteTask.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                onDeleteClick(currentPosition)
            }
        }
    }

    override fun getItemCount(): Int = taskList.size

    private fun applyTaskStyle(holder: TaskViewHolder, isCompleted: Boolean) {
        if (isCompleted) {
            holder.tvTaskTitle.paintFlags =
                holder.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            holder.tvTaskTitle.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.completed_text)
            )
        } else {
            holder.tvTaskTitle.paintFlags =
                holder.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            holder.tvTaskTitle.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.text_primary)
            )
        }
    }
}