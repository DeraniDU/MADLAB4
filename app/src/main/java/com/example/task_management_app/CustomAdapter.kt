package com.example.task_management_app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class CustomAdapter internal constructor(
    private val activity: Activity,
    private val context: Context,
    private val task_id: ArrayList<String>,
    private val task_name: ArrayList<String>,
    private val task_priority: ArrayList<String>,
    private val task_date: ArrayList<String>
) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.my_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.task_id_txt?.text = task_id.getOrNull(position)
        holder.task_name_txt?.text = task_name.getOrNull(position)
        holder.task_priority_txt?.text = task_priority.getOrNull(position)
        holder.task_date_txt?.text = task_date.getOrNull(position)
        //Recyclerview onClickListener
        holder.mainLayout?.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("id", task_id.getOrNull(position))
            intent.putExtra("taskname", task_name.getOrNull(position))
            intent.putExtra("taskpriority", task_priority.getOrNull(position))
            intent.putExtra("taskdate", task_date.getOrNull(position))
            activity.startActivityForResult(intent, 1)
        }
    }

    override fun getItemCount(): Int {
        return task_id.size
    }

    internal inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var task_id_txt: TextView? = itemView.findViewById(R.id.task_id_txt)
        var task_name_txt: TextView? = itemView.findViewById(R.id.task_name_txt)
        var task_priority_txt: TextView? = itemView.findViewById(R.id.task_priority_txt)
        var task_date_txt: TextView? = itemView.findViewById(R.id.task_date_txt)
        var mainLayout: LinearLayout? = itemView.findViewById(R.id.mainLayout)

        init {
            //Animate Recyclerview
            val translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim)
            mainLayout?.animation = translate_anim
        }
    }
}
