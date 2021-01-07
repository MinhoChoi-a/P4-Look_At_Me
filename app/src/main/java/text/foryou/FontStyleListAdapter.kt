package text.foryou

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import text.foryou.data.FontStyleEntity
import text.foryou.data.NoteEntity
import text.foryou.databinding.ListFontstyleItemBinding

class FontStyleListAdapter(private val fontStyleList: List<FontStyleEntity>, private val note: MutableLiveData<NoteEntity>)
    : RecyclerView.Adapter<FontStyleListAdapter.ViewHolder>(){

    private var checkedRBfontStyle: CompoundButton? = null
    private var resources: Resources? = null
    private var packageName: String = ""
    private var context: Context? = null

    inner class ViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView) {
        val binding = ListFontstyleItemBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_fontstyle_item, parent, false)
        resources = parent.context.resources
        packageName = parent.context.packageName
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val set = fontStyleList[position]
        //val back_id

        if(set.style.equals(note.value?.fontStyle)) {
            holder.binding.fontStyle.isChecked = true;
            checkedRBfontStyle = holder.binding.fontStyle
            holder.binding.backColorback.setBackgroundResource(R.drawable.select)
        }

        val checkedChangeListener = CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            checkedRBfontStyle?.apply { setChecked(!isChecked) }
            checkedRBfontStyle = compoundButton.apply { setChecked(isChecked) }
            if(isChecked) {
                holder.binding.backColorback.setBackgroundResource(R.drawable.select)
            }
            else {
                holder.binding.backColorback.setBackgroundResource(R.drawable.non_select)
            }
        }

        holder.binding.fontStyle.setOnCheckedChangeListener(checkedChangeListener)

        with(holder.binding) {

            var id = resources?.getIdentifier(set.style, "font", packageName)

            fontStyle.setText(set.name)
            fontStyle.setTag(set.style)
            fontStyle.typeface = ResourcesCompat.getFont(context!!, id!!)
            fontStyle.setBackgroundResource(id ?: R.drawable.back_transparent)

        }
    }

    fun getCheckedRBfontStyle(): CompoundButton? {
        return this.checkedRBfontStyle
    }

    override fun getItemCount() = fontStyleList.size

}