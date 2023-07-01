package com.jcoding.expensetracker.ui.common.chooserbottomsheet.listadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jcoding.expensetracker.databinding.ChooserBottomSheetOptionLayoutBinding


class ChooserOptionListAdapter() :
    ListAdapter<ChooserOption, ChooserOptionListAdapter.ItemViewHolder>(
        ChooserOptionAdapterItemDiffCallback
    ) {

    interface Callback {
        fun onItemClick(item: ChooserOption)
    }

    private var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ItemViewHolder {
        return ItemViewHolder(
            ChooserBottomSheetOptionLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    fun setCallback(_callback: Callback) {
        this.callback = _callback
    }

    inner class ItemViewHolder(private val binding: ChooserBottomSheetOptionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: ChooserOption

        init {
            binding.root.setOnClickListener {
                callback?.onItemClick(item)
            }
        }

        fun onBind(_item: ChooserOption) {
            this.item = _item
            binding.optionLabelTextView.text = item.getTitle(binding.root.context)
            if(item.iconResId != null){
                binding.iconImageView.apply {
                    setImageResource(requireNotNull(item.iconResId))
                    visibility = View.VISIBLE
                }
            }else{
                binding.iconImageView.visibility = View.GONE
            }
        }
    }
}