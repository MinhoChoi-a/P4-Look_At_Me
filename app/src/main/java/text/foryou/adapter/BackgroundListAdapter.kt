package text.foryou.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import text.foryou.R
import text.foryou.data.model.NoteEntity
import text.foryou.data.model.BackgroundEntity
import text.foryou.databinding.ListBackItemBinding

//(constructor: backgroundList, note)
//extends RecyclerView
class BackgroundListAdapter(private val backgroundList: List<BackgroundEntity>, private val note: MutableLiveData<NoteEntity>)
    : RecyclerView.Adapter<BackgroundListAdapter.ViewHolder>(){

    //used compoundButton to have a onclick effect
    private var checkedRB: CompoundButton? = null

    private var resources: Resources? = null
    private var packageName: String = ""
    private lateinit var context: Context

    //inner class: it will do coupling between NoteListAdapter each item of List for the RecyclerView
    //ViewHolder describes an item view
    inner class ViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView) {
        val binding = ListBackItemBinding.bind(itemView)
    }

    //this methods will be called when RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_back_item, parent, false)

        //get the info of the current context => to use the app's stored resource
        resources = parent.context.resources
        packageName = parent.context.packageName
        context = parent.context

        return ViewHolder(view)
    }

    //display the data at the specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val set = backgroundList[position]

        //compare background resource and check if it is selected one
        if(set.res.equals(note.value?.backRes!!)) {

            //setting for the radio button
            holder.binding.backColor.isChecked = true;
            holder.binding.backColorback.setBackgroundResource(R.drawable.select)

            //assign the value of checkedRB
            checkedRB = holder.binding.backColor
        }

        //listener for the OnCheckedChanged
        val checkedChangeListener = CompoundButton.OnCheckedChangeListener {
                compoundButton,

                //boolean
                isChecked ->
                        checkedRB?.apply { setChecked(!isChecked) }
                        checkedRB = compoundButton.apply { setChecked(isChecked) }

            //set the background color based on the checked status
            if(isChecked) {
                holder.binding.backColorback.setBackgroundResource(R.drawable.select)
            }
            else {
                holder.binding.backColorback.setBackgroundResource(R.drawable.non_select)
            }
        }

        //binding checkedChangeListener to the fragment
        holder.binding.backColor.setOnCheckedChangeListener(checkedChangeListener)

        //set data for each item on RecyclerView
        with(holder.binding) {

            //get the id of the drawable resource
            var id = resources?.getIdentifier(set.backImage, "drawable", packageName)

            backColor.setText(set.title)
            backColor.setTag(set.id)
            backColor.setBackgroundResource(id ?: R.drawable.back_transparent)

            if(set.type==3) {
                if(!set.res.equals("heart")) {
                    backColor.setTextColor(getColor(context, R.color.white))
                }
            }
        }
    }

    //get the information of checked item
    fun getCheckedRB(): CompoundButton? {
        return this.checkedRB
    }

    override fun getItemCount() = backgroundList.size
  }