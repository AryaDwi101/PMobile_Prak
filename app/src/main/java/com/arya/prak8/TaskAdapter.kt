package com.arya.prak8

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arya.prak8.databinding.ItemTaskBinding

class TaskAdapter(
    private val tasks: List<Task>,
    private val onCheckChanged: (Task) -> Unit,
    private val onDeleteClicked: (Task) -> Unit,
    private val onItemClicked: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.tvTitle.text = task.title
            binding.tvDescription.text = task.description
            binding.tvDate.text = task.date
            binding.cbTask.setOnCheckedChangeListener(null)
            binding.cbTask.isChecked = task.isCompleted

            updateVisuals(task.isCompleted)

            binding.cbTask.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                updateVisuals(isChecked)
                onCheckChanged(task)
            }
            binding.btnDelete.setOnClickListener {
                onDeleteClicked(task)
            }
            binding.textContainer.setOnClickListener {
                onItemClicked(task)
            }
        }

        private fun updateVisuals(isCompleted: Boolean) {
            if (isCompleted) {
                binding.tvTitle.paintFlags = binding.tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.tvTitle.setTextColor(Color.GRAY)
                binding.tvDescription.setTextColor(Color.LTGRAY)
                binding.tvDate.alpha = 0.5f
            } else {
                binding.tvTitle.paintFlags = binding.tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.tvTitle.setTextColor(Color.BLACK)
                binding.tvDescription.setTextColor(Color.parseColor("#757575"))
                binding.tvDate.alpha = 1.0f
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount() = tasks.size
}