package com.example.task_management_app

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var add_button: FloatingActionButton
    private lateinit var empty_imageview: ImageView
    private lateinit var no_data: TextView
    private lateinit var myDB: MyDatabaseHelper
    private lateinit var task_id: ArrayList<String>
    private lateinit var task_name: ArrayList<String>
    private lateinit var task_priority: ArrayList<String>
    private lateinit var task_date: ArrayList<String>
    private lateinit var customAdapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        add_button = findViewById(R.id.add_button)
        empty_imageview = findViewById(R.id.empty_imageview)
        no_data = findViewById(R.id.no_data)

        add_button.setOnClickListener {
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            startActivityForResult(intent, 1)
        }

        myDB = MyDatabaseHelper(this@MainActivity)
        task_id = ArrayList()
        task_name = ArrayList()
        task_priority = ArrayList()
        task_date = ArrayList()

        storeDataInArrays()

        customAdapter = CustomAdapter(
            this@MainActivity, this, task_id, task_name, task_priority,
            task_date
        )

        recyclerView.adapter = customAdapter
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            recreate()
        }
    }

    private fun storeDataInArrays() {
        val cursor: Cursor? = myDB.readAllData()
        cursor?.let {
            if (it.count == 0) {
                empty_imageview.visibility = View.VISIBLE
                no_data.visibility = View.VISIBLE
            } else {
                while (it.moveToNext()) {
                    task_id.add(it.getString(0))
                    task_name.add(it.getString(1))
                    task_priority.add(it.getString(2))
                    task_date.add(it.getString(3))
                }
                empty_imageview.visibility = View.GONE
                no_data.visibility = View.GONE
            }
        }
        cursor?.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all) {
            confirmDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete All?")
        builder.setMessage("Are you sure you want to delete all Data?")
        builder.setPositiveButton(
            "Yes"
        ) { _, _ ->
            myDB.deleteAllData()
            // Refresh Activity
            recreate()
        }
        builder.setNegativeButton(
            "No"
        ) { _, _ -> }
        builder.create().show()
    }
}
