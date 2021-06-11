package text.foryou.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import text.foryou.R
import text.foryou.data.model.FontColorEntity
import text.foryou.data.model.NoteEntity
import text.foryou.databinding.ListFontcolorItemBinding

//(constructor: fontColorList, note)
//extends RecyclerView
class FontColorListAdapter(private val fontColorList: List<FontColorEntity>, private val note: MutableLiveData<NoteEntity>)
    : RecyclerView.Adapter<FontColorListAdapter.ViewHolder>(){

    //used compoundButton to have a onclick effect
    private var checkedRBfont: CompoundButton? = null

    private var resources: Resources? = null
    private var packageName: String = ""

    //inner class: it will do coupling between NoteListAdapter each item of List for the RecyclerView
    //ViewHolder describes an item view
   inner class ViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView) {

       val binding = ListFontcolorItemBinding.bind(itemView)
    }

    //this methods will be called when RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_fontcolor_item, parent, false)

        //get the info of the current context => to use the app's stored resource
        resources = parent.context.resources
        packageName = parent.context.packageName

        return ViewHolder(view)
    }

    //display the data at the specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val set = fontColorList[position]

        //compare font color and check if it is selected one
        if(set.color.equals(note.value?.fontColor!!)) {

            //setting for the radio button
            holder.binding.fontColor.isChecked = true;
            holder.binding.backColorback.setBackgroundResource(R.drawable.select)

            //assign the value of checkedRBfont
            checkedRBfont = holder.binding.fontColor
        }

        //listener for the OnCheckedChanged
        val checkedChangeListener = CompoundButton.OnCheckedChangeListener {
                compoundButton,
                isChecked ->
                        checkedRBfont?.apply { setChecked(!isChecked) }
                        checkedRBfont = compoundButton.apply { setChecked(isChecked) }

            //set the background color based on the checked status
            if(isChecked) {
                holder.binding.backColorback.setBackgroundResource(R.drawable.select)
            }
            else {
                holder.binding.backColorback.setBackgroundResource(R.drawable.non_select)
            }
        }

        //binding checkedChangeListener to the fragment
        holder.binding.fontColor.setOnCheckedChangeListener(checkedChangeListener)

        //set data for each item on RecyclerView
        with(holder.binding) {

            //get the id of the color resource
            var id = resources?.getIdentifier(set.color, "color", packageName)

            fontColor.setText(set.name)
            fontColor.setTag(set.color)
            fontColor.setBackgroundResource(id ?: R.drawable.back_transparent)
        }
    }

    //get the information of checked item
    fun getCheckedRBfont(): CompoundButton? {
        return this.checkedRBfont
    }

    override fun getItemCount() = fontColorList.size

 }