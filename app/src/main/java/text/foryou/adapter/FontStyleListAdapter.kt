package text.foryou.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import text.foryou.R
import text.foryou.data.model.FontStyleEntity
import text.foryou.data.model.NoteEntity
import text.foryou.databinding.ListFontstyleItemBinding

//(constructor: fontStyleList, note)
//extends RecyclerView
class FontStyleListAdapter(private val fontStyleList: List<FontStyleEntity>, private val note: MutableLiveData<NoteEntity>)
    : RecyclerView.Adapter<FontStyleListAdapter.ViewHolder>(){

    //used compoundButton to have a onclick effect
    private var checkedRBfontStyle: CompoundButton? = null

    private var resources: Resources? = null
    private var packageName: String = ""
    private var context: Context? = null

    //inner class: it will do coupling between NoteListAdapter each item of List for the RecyclerView
    //ViewHolder describes an item view
    inner class ViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView) {
        val binding = ListFontstyleItemBinding.bind(itemView)
    }

    //this methods will be called when RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_fontstyle_item, parent, false)

        //get the info of the current context => to use the app's stored resource
        resources = parent.context.resources
        packageName = parent.context.packageName
        context = parent.context

        return ViewHolder(view)
    }

    //display the data at the specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val set = fontStyleList[position]

        //compare font style and check if it is selected one
        if(set.style.equals(note.value?.fontStyle)) {

            //setting for the radio button
            holder.binding.fontStyle.isChecked = true;
            holder.binding.backColorback.setBackgroundResource(R.drawable.select)

            //assign the value of checkedRBfont
            checkedRBfontStyle = holder.binding.fontStyle
        }

        //listener for the OnCheckedChanged
        val checkedChangeListener = CompoundButton.OnCheckedChangeListener {
                compoundButton,
                isChecked ->
                        checkedRBfontStyle?.apply { setChecked(!isChecked) }
                        checkedRBfontStyle = compoundButton.apply { setChecked(isChecked) }

            //set the background color based on the checked status
            if(isChecked) {
                holder.binding.backColorback.setBackgroundResource(R.drawable.select)
            }
            else {
                holder.binding.backColorback.setBackgroundResource(R.drawable.non_select)
            }
        }

        //binding checkedChangeListener to the fragment
        holder.binding.fontStyle.setOnCheckedChangeListener(checkedChangeListener)

        //set data for each item on RecyclerView
        with(holder.binding) {

            //get the id of the color resource
            var id = resources?.getIdentifier(set.style, "font", packageName)

            fontStyle.setText(set.name)
            fontStyle.setTag(set.style)
            fontStyle.typeface = ResourcesCompat.getFont(context!!, id!!) //apply the font style
            fontStyle.setBackgroundResource(id ?: R.drawable.back_transparent)
        }
    }

    //get the information of checked item
    fun getCheckedRBfontStyle(): CompoundButton? {
        return this.checkedRBfontStyle
    }

    override fun getItemCount() = fontStyleList.size

}