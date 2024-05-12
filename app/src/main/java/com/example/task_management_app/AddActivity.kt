package com.example.task_management_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddActivity : AppCompatActivity() {
    private lateinit var nameInput: EditText
    private lateinit var priorityInput: EditText
    private lateinit var timeInput: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        nameInput = findViewById(R.id.title_input)
        priorityInput = findViewById(R.id.author_input)
        timeInput = findViewById(R.id.pages_input)
        addButton = findViewById(R.id.add_button)

        addButton.setOnClickListener {
            val myDB = MyDatabaseHelper(this@AddActivity)
            myDB.addTask(
                nameInput.text.toString().trim(),
                priorityInput.text.toString().trim(),
                timeInput.text.toString().trim()
            )

            // After adding, go back to MainActivity to see the changes
            val intent = Intent(this@AddActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
