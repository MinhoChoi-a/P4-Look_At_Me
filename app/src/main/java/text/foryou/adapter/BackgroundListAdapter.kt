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

class BackgroundListAdapter(private val backgroundList: List<BackgroundEntity>, private val note: MutableLiveData<NoteEntity>)
    : RecyclerView.Adapter<BackgroundListAdapter.ViewHolder>(){

    private var checkedRB: CompoundButton? = null
    private var resources: Resources? = null
    private var packageName: String = ""
    private lateinit var context: Context

    inner class ViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView) {
        val binding = ListBackItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_back_item, parent, false)
        resources = parent.context.resources
        packageName = parent.context.packageName
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val set = backgroundList[position]
        //val back_id

        if(set.res.equals(note.value?.backRes!!)) {
            holder.binding.backColor.isChecked = true;
            holder.binding.backColorback.setBackgroundResource(R.drawable.select)
            checkedRB = holder.binding.backColor
        }

        val checkedChangeListener = CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            checkedRB?.apply { setChecked(!isChecked) }
            checkedRB = compoundButton.apply { setChecked(isChecked) }
            if(isChecked) {
                holder.binding.backColorback.setBackgroundResource(R.drawable.select)
            }
            else {
                holder.binding.backColorback.setBackgroundResource(R.drawable.non_select)
            }
        }

        holder.binding.backColor.setOnCheckedChangeListener(checkedChangeListener)

        with(holder.binding) {

            var id = resources?.getIdentifier(set.backImage, "drawable", packageName)

            backColor.setText(set.title)
            backColor.setTag(set.id)
            //backColor.setButtonDrawable(id ?: R.drawable.back_transparent)
            backColor.setBackgroundResource(id ?: R.drawable.back_transparent)

            if(set.type==3) {

                if(!set.res.equals("heart")) {
                    backColor.setTextColor(getColor(context, R.color.white))
                }

            }
        }
    }



    fun getCheckedRB(): CompoundButton? {
        return this.checkedRB
    }



    override fun getItemCount() = backgroundList.size

  }