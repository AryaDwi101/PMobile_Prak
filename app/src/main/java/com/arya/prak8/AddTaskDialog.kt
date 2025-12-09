package com.arya.prak8

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.arya.prak8.databinding.DialogTaskBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskDialog(
    private val context: Context,
    private val tasksRef: DatabaseReference,
    private val task: Task? = null
) {

    fun show() {
        val binding = DialogTaskBinding.inflate(LayoutInflater.from(context))

        if (task != null) {
            binding.tvTitleDialog.text = "Edit Tugas"
            binding.etTitle.setText(task.title)
            binding.etDescription.setText(task.description)
            binding.etDate.setText(task.date)
        }

        binding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, day)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    binding.etDate.setText(dateFormat.format(selectedDate.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setPositiveButton("Simpan") { dialog, _ ->
                val title = binding.etTitle.text.toString()
                val desc = binding.etDescription.text.toString()
                val date = binding.etDate.text.toString()

                if (title.isEmpty() || date.isEmpty()) {
                    Toast.makeText(context, "Judul dan Tanggal wajib diisi!", Toast.LENGTH_SHORT).show()
                } else {
                    saveOrUpdateTask(title, desc, date)
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun saveOrUpdateTask(title: String, desc: String, date: String) {
        val idKey = task?.id ?: tasksRef.push().key
        val status = task?.isCompleted ?: false
        val taskData = Task(idKey, title, desc, date, status)

        idKey?.let {
            tasksRef.child(it).setValue(taskData)
                .addOnSuccessListener {
                    val message = if (task == null) "Berhasil ditambah!" else "Berhasil diedit!"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
        }
    }
}