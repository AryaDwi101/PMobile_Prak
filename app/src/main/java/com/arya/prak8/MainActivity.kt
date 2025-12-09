package com.arya.prak8

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.prak8.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tasksRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tasksRef = FirebaseDatabase.getInstance().getReference("tasks")
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        fetchData()
        binding.fabAddTask.setOnClickListener {
            AddTaskDialog(this, tasksRef).show()
        }
    }

    private fun fetchData() {
        tasksRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskList = mutableListOf<Task>()

                for (data in snapshot.children) {
                    val task = data.getValue(Task::class.java)
                    task?.id = data.key
                    task?.let { taskList.add(it) }
                }
                taskList.sortWith(compareBy<Task> { it.isCompleted }.thenBy { it.date })

                updateUI(taskList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(tasks: List<Task>) {
        if (tasks.isEmpty()) {
            binding.rvTasks.visibility = View.GONE
            binding.layoutEmpty.visibility = View.VISIBLE
        } else {
            binding.rvTasks.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE

            val adapter = TaskAdapter(tasks,
                onCheckChanged = { task -> updateTaskStatus(task) },
                onDeleteClicked = { task -> deleteTask(task) },
                onItemClicked = { task ->
                    AddTaskDialog(this, tasksRef, task).show()
                }
            )
            binding.rvTasks.adapter = adapter
        }
    }

    private fun updateTaskStatus(task: Task) {
        task.id?.let { id ->
            tasksRef.child(id).child("completed").setValue(task.isCompleted)
        }
    }

    private fun deleteTask(task: Task) {
        task.id?.let { id ->
            tasksRef.child(id).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this, "Tugas dihapus", Toast.LENGTH_SHORT).show()
                }
        }
    }
}