package com.jcoding.expensetracker.ui.currency.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jcoding.expensetracker.R
import com.jcoding.expensetracker.databinding.CurrencyListAdapterItemViewHolderBinding


class CurrencyListAdapter() : ListAdapter<CurrencyListAdapterItem,
        CurrencyListAdapter.ItemViewHolder>(CurrencyListAdapterItemDiffCallback) {
    companion object {
        private val FULL_LIST_ITEM_PAYLOAD_DIFF = CurrencyLisItemPayloadDiff(
            isSelectionChanged = true
        )

        val EMPTY_LIST_ITEM_PAYLOAD_DIFF = CurrencyLisItemPayloadDiff(
            isSelectionChanged = false
        )
    }

    interface Callback {
        fun onItemClick(item: CurrencyListAdapterItem)
    }

    private var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ItemViewHolder {
        return ItemViewHolder(
            CurrencyListAdapterItemViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(getItem(position), FULL_LIST_ITEM_PAYLOAD_DIFF)
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val diff = (
                payloads.filterIsInstance<CurrencyLisItemPayloadDiff>()
                    .takeIf { it.isNotEmpty() }
                    ?: listOf(FULL_LIST_ITEM_PAYLOAD_DIFF)
                ).fold(EMPTY_LIST_ITEM_PAYLOAD_DIFF, CurrencyLisItemPayloadDiff::plus)

        holder.onBind(getItem(position), diff)
    }


    fun setItemClickListener(onClick: (item: CurrencyListAdapterItem) -> Unit) {
        this.callback = object : Callback {
            override fun onItemClick(item: CurrencyListAdapterItem) {
                onClick.invoke(item)
            }
        }
    }


    inner class ItemViewHolder(private val binding: CurrencyListAdapterItemViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: CurrencyListAdapterItem

        init {
            binding.itemTextView.setOnClickListener {
                callback?.onItemClick(item)
            }
        }

        fun onBind(_item: CurrencyListAdapterItem, payloadDiff: CurrencyLisItemPayloadDiff) {
            this.item = _item
            binding.itemTextView.text = item.symbol

            if (payloadDiff.isSelectionChanged) {
                if (item.isSelected) {
                    binding.itemTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(binding.root.context, R.drawable.ic_check),
                        null
                    )
                } else {
                    binding.itemTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        null, null, null, null
                    )
                }
            }

        }
    }
}