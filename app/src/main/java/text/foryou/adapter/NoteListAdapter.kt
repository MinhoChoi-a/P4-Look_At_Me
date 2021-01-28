package text.foryou.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import text.foryou.R
import text.foryou.data.model.NoteEntity
import text.foryou.databinding.ListItemBinding


class NoteListAdapter(private val noteList: List<NoteEntity>, private val listener: ListItemListener)
    : RecyclerView.Adapter<NoteListAdapter.ViewHolder>(){

    val selectedNotes = arrayListOf<NoteEntity>()

    inner class ViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView) {
        val binding = ListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = noteList[position]

        with(holder.binding) {
            noteText.text = note.text


            noteText.setOnClickListener {
                listener.editNote(note.id)
            }

            clickButtonToPlay.setOnClickListener {
                listener.playNote(note.id)
            }

            clickToEdit.setOnClickListener {
                listener.editNote(note.id)
            }
        }
    }

    override fun getItemCount() = noteList.size


    interface ListItemListener {
        fun editNote(noteId: Int)
        fun playNote(noteId: Int)
        fun onItemSelectionChanged()

    }
}