// MyDatabaseHelper.kt

package com.example.task_management_app

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

internal class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = "CREATE TABLE $TABLE_NAME (" +

                "$COLUMN_Name TEXT, " +
                "$COLUMN_Prority TEXT, " +
                "$COLUMN_date INTEGER);"
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addTask(taskname: String?, taskprority: String?, taskdate: String) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_Name, taskname)
        cv.put(COLUMN_Prority, taskprority)
        cv.put(COLUMN_date, taskdate)

        // Get the maximum task ID from the database
        val query = "SELECT MAX($COLUMN_ID) FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        var maxId = 0
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                maxId = cursor.getInt(0) // Get the maximum task ID
            }
            cursor.close()
        }

        val taskId = maxId + 1 // Increment the maximum task ID by 1 to assign the new task ID

        cv.put(COLUMN_ID, taskId) // Assign the new task ID to the ContentValues

        val result = db.insert(TABLE_NAME, null, cv)
        if (result == -1L) {
            Log.e("MyDatabaseHelper", "Failed to add task")
        } else {
            Log.d("MyDatabaseHelper", "Task added successfully")
        }
    }


    fun readAllData(): Cursor? {
        val db = this.readableDatabase
        return db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }

    fun updateData(row_id: String, taskname: String?, taskprority: String?, taskdate: String?) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_Name, taskname)
        cv.put(COLUMN_Prority, taskprority)
        cv.put(COLUMN_date, taskdate)
        val result = db.update(TABLE_NAME, cv, "$COLUMN_ID=?", arrayOf(row_id)).toLong()
        if (result == -1L) {
            Log.e("MyDatabaseHelper", "Failed to update task")
        } else {
            Log.d("MyDatabaseHelper", "Task updated successfully")
        }
    }

    fun deleteOneRow(row_id: String) {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(row_id)).toLong()
        if (result == -1L) {
            Log.e("MyDatabaseHelper", "Failed to update task")
        } else {
            Log.d("MyDatabaseHelper", "Task delete successfully")
        }
    }

    fun deleteAllData() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
    }

    companion object {
        private const val DATABASE_NAME = "taskmgt.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "my_library"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_Name = "taskname"
        private const val COLUMN_Prority = "taskprority"
        private const val COLUMN_date = "taskdate"
    }
}
