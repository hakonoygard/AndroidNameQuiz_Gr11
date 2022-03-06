package com.example.namequizapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.namequizapp.data.QuizEntryModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.namequizapp.databinding.RowEntryBinding
import com.example.namequizapp.utils.ImageUtils.convertToBitmap

class EntryAdapterNew(
    private val onItemClicked: (QuizEntryModel) -> Unit
) : ListAdapter<QuizEntryModel, EntryAdapterNew.EntryAdapterNewViewHolder>(DiffCallback){


    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<QuizEntryModel>() {
            override fun areItemsTheSame(
                oldItem: QuizEntryModel,
                newItem: QuizEntryModel
            ): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(
                oldItem: QuizEntryModel,
                newItem: QuizEntryModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryAdapterNewViewHolder {

        return EntryAdapterNewViewHolder(
            RowEntryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EntryAdapterNewViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClicked)
    }

    class EntryAdapterNewViewHolder(
        private var binding: RowEntryBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(quizEntry: QuizEntryModel, onItemClicked: (QuizEntryModel) -> Unit) {
            binding.tvName.text = quizEntry.name
            binding.ivEntry.setImageBitmap(quizEntry.image.convertToBitmap())
            binding.btnSlett.setOnClickListener{
                onItemClicked(quizEntry)
            }
        }
    }
}