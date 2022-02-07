package com.example.namequizapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for recycler view
 */
class EntryAdapter (private val entries: List<QuizEntry>, private val listener: View.OnClickListener) : RecyclerView.Adapter<EntryAdapter.ViewHolder>()
{
    /**
     * Provides a direct reference to each of the views within a data item
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val ivEntry : ImageView = itemView.findViewById(R.id.ivEntry)
        val btnSlett : Button = itemView.findViewById(R.id.btnSlett)
    }

    /**
     * Inflates layout from XML and returns view holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val entryView = inflater.inflate(R.layout.row_entry, parent, false)
        return ViewHolder(entryView)
    }

    /**
     * Populates data to item through view holder
     */
    override fun onBindViewHolder(viewHolder: EntryAdapter.ViewHolder, position: Int) {
        val entry: QuizEntry = entries[position]

        viewHolder.tvName.text = entry.name
        viewHolder.ivEntry.setImageBitmap(entry.image)
        viewHolder.btnSlett.setOnClickListener(listener)
        viewHolder.btnSlett.tag = entry
    }

    /**
     * Returns total number of items in list
     */
    override fun getItemCount(): Int {
        return entries.size
    }
}