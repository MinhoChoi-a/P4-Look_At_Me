package com.example.lookatme

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.lookatme.data.FontColorEntity
import com.example.lookatme.data.NoteEntity
import com.example.lookatme.databinding.ListFontcolorItemBinding

class FontColorListAdapter(private val fontColorList: List<FontColorEntity>, private val note: MutableLiveData<NoteEntity>)
    : RecyclerView.Adapter<FontColorListAdapter.ViewHolder>(){

    private var checkedRBfont: CompoundButton? = null
    private var resources: Resources? = null
    private var packageName: String = ""

    inner class ViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView) {
        val binding = ListFontcolorItemBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_fontcolor_item, parent, false)
        resources = parent.context.resources
        packageName = parent.context.packageName

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val set = fontColorList[position]
        //val back_id

        if(set.color.equals(note.value?.fontColor!!)) {
            holder.binding.fontColor.isChecked = true;
            holder.binding.backColorback.setBackgroundResource(R.drawable.select)
            checkedRBfont = holder.binding.fontColor
        }

        val checkedChangeListener = CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            checkedRBfont?.apply { setChecked(!isChecked) }
            checkedRBfont = compoundButton.apply { setChecked(isChecked) }
            if(isChecked) {
                holder.binding.backColorback.setBackgroundResource(R.drawable.select)
            }
            else {
                holder.binding.backColorback.setBackgroundResource(R.drawable.non_select)
            }
        }

        holder.binding.fontColor.setOnCheckedChangeListener(checkedChangeListener)

        with(holder.binding) {

            var id = resources?.getIdentifier(set.color, "color", packageName)

            fontColor.setText(set.name)
            fontColor.setTag(set.color)
            fontColor.setBackgroundResource(id ?: R.drawable.back_transparent)
            //fontColor.setTextColor(id ?: R.color.black)
        }
    }

    fun getCheckedRBfont(): CompoundButton? {
        return this.checkedRBfont
    }

    override fun getItemCount() = fontColorList.size

}