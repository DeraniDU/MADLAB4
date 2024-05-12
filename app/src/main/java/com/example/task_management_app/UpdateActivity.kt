// UpdateActivity.kt

package com.example.task_management_app

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateActivity : AppCompatActivity() {
    private lateinit var tasknameInput: EditText
    private lateinit var taskpriorityInput: EditText
    private lateinit var taskdateInput: EditText
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private var id: String? = null
    private lateinit var myDB: MyDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        tasknameInput = findViewById(R.id.taskname_input)
        taskpriorityInput = findViewById(R.id.taskpriority_input)
        taskdateInput = findViewById(R.id.taskdate_input)
        updateButton = findViewById(R.id.update_button)
        deleteButton = findViewById(R.id.delete_button)

        myDB = MyDatabaseHelper(this)

        getIntentData()

        updateButton.setOnClickListener {
            val taskname = tasknameInput.text.toString().trim()
            val taskpriority = taskpriorityInput.text.toString().trim()
            val taskdate = taskdateInput.text.toString().trim()

            if (taskname.isNotEmpty() && taskpriority.isNotEmpty() && taskdate.isNotEmpty()) {
                myDB.updateData(id!!, taskname, taskpriority, taskdate)
                Toast.makeText(this, "Task updated successfully!", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK, Intent())
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            confirmDialog()
        }
    }

    private fun getIntentData() {
        if (intent.hasExtra("id") && intent.hasExtra("taskname") &&
            intent.hasExtra("taskpriority") && intent.hasExtra("taskdate")
        ) {
            id = intent.getStringExtra("id")
            val taskname = intent.getStringExtra("taskname")!!
            val taskpriority = intent.getStringExtra("taskpriority")!!
            val taskdate = intent.getStringExtra("taskdate")!!

            tasknameInput.setText(taskname)
            taskpriorityInput.setText(taskpriority)
            taskdateInput.setText(taskdate)
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun confirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Task")
        builder.setMessage("Are you sure you want to delete this task?")
        builder.setPositiveButton("Yes") { _, _ ->
            if (id != null) {
                myDB.deleteOneRow(id!!)
                Toast.makeText(this, "Task deleted successfully!", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK, Intent())
                finish()
            } else {
                Toast.makeText(this, "Unable to delete task.", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}
