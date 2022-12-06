package com.notes.ui.fragment.notes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.notes.databinding.ItemNoteBinding
import com.notes.model.NoteModel


/**
 * @author Fedotov Yakov
 */
class NotesAdapter :
    ListAdapter<NoteModel, NotesAdapter.NotesViewHolder>(NoteDiffCallback) {

    var onItemClickListener: ((NoteModel) -> Unit)? = null
    var onItemSelectedListener: ((NoteModel, Int) -> Unit)? = null

    private var editingMode = false
    private var countSelected = 0

    var list: List<NoteModel> = emptyList()
        private set

    override fun submitList(list: List<NoteModel>?) {
        val update = editingMode
        editingMode = false
        countSelected = 0
        submitList(list) {
            if (update) {
                notifyItemRangeChanged(0, itemCount)
            }
        }
        this.list = list ?: emptyList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        getItem(position).also(holder::bind)
    }


    inner class NotesViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val note = getItem(adapterPosition)
                when {
                    !editingMode -> onItemClickListener?.invoke(getItem(adapterPosition))
                    !note.isCheck -> {
                        note.isCheck = true
                        countSelected++
                        onItemSelectedListener?.invoke(note, countSelected)
                        notifyItemChanged(adapterPosition)
                    }
                    else -> {
                        if (--countSelected <= 0) {
                            unSelectAll()
                        } else {
                            note.isCheck = false
                            notifyItemChanged(adapterPosition)
                        }
                        onItemSelectedListener?.invoke(note, countSelected)
                    }
                }
            }
            binding.root.setOnLongClickListener { view ->
                if (!editingMode) {
                    val note = getItem(adapterPosition)
                    editingMode = true
                    if (!note.isCheck) {
                        countSelected++
                        onItemSelectedListener?.invoke(getItem(adapterPosition), countSelected)
                        note.isCheck = true
                    }
                    notifyItemRangeChanged(0, itemCount)
                }
                true
            }
        }

        fun bind(item: NoteModel) {
            binding.apply {
                name.text = item.name
                date.text = item.date
                text.text = item.text
                check.isVisible = editingMode
                check.isSelected = item.isCheck
            }
        }
    }

    fun selectAll() {
        repeat(itemCount) {
            getItem(it).isCheck = true
        }
        editingMode = true
        countSelected = itemCount
        notifyItemRangeChanged(0, itemCount)
        onItemSelectedListener?.invoke(getItem(itemCount - 1), countSelected)
    }

    fun unSelectAll() {
        repeat(itemCount) {
            getItem(it).isCheck = false
        }
        editingMode = false
        countSelected = 0
        notifyItemRangeChanged(0, itemCount)
    }

    object NoteDiffCallback : DiffUtil.ItemCallback<NoteModel>() {
        override fun areItemsTheSame(
            oldItem: NoteModel,
            newItem: NoteModel
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: NoteModel,
            newItem: NoteModel
        ) = oldItem == newItem
    }
}