package com.example.lookatme

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.widget.DrawableUtils
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lookatme.data.SetEntity
import com.example.lookatme.databinding.ListBackItemBinding
import kotlinx.coroutines.withContext


class SetListAdapter(private val setList: List<SetEntity>)
    : RecyclerView.Adapter<SetListAdapter.ViewHolder>(){

    private var checkedRB: CompoundButton? = null
    private var resources: Resources? = null
    private var packageName: String = ""

    inner class ViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView) {
        val binding = ListBackItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_back_item, parent, false)
        resources = parent.context.resources
        packageName = parent.context.packageName

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val set = setList[position]
        //val back_id

        holder.binding.backColor.setOnCheckedChangeListener(checkedChangeListener)

        if (holder.binding.backColor.isChecked)
            checkedRB = holder.binding.backColor

        with(holder.binding) {

            var id = resources?.getIdentifier(set.res, "drawable", packageName)

            backColor.setText(set.title)
            backColor.setTag(set.id)
            backColor.setBackgroundResource(id ?: R.drawable.back_transparent)
        }
    }

    private val checkedChangeListener = CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
        checkedRB?.apply { setChecked(!isChecked) }
        checkedRB = compoundButton.apply { setChecked(isChecked) }
    }

    fun getCheckedRB(): CompoundButton? {
        return this.checkedRB
    }

    override fun getItemCount() = setList.size

  }