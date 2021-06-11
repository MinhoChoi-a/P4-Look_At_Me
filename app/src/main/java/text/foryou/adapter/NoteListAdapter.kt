package text.foryou.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import text.foryou.R
import text.foryou.data.model.NoteEntity
import text.foryou.databinding.ListItemBinding

//(constructor: noteList, listener)
//extends RecyclerView
class NoteListAdapter(private val noteList: List<NoteEntity>, private val listener: ListItemListener)
    : RecyclerView.Adapter<NoteListAdapter.ViewHolder>(){

    //inner class: it will do coupling between NoteListAdapter each item of List for the RecyclerView
    //ViewHolder describes an item view
    inner class ViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView) {
        val binding = ListItemBinding.bind(itemView)
    }

    //this methods will be called when RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    //display the data at the specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = noteList[position]

        //with each item of RecyclerView
        //can call the each view item by using data binding library
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

    //must have this method to use RecyclerView interface to know the total number of items in data set
    override fun getItemCount() = noteList.size

    //to customize the action listener of the Recycler View
    interface ListItemListener {
        fun editNote(noteId: Int)
        fun playNote(noteId: Int)
    }
}