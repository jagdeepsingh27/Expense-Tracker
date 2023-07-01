package com.jcoding.expensetracker.ui.home.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jcoding.expensetracker.databinding.ExpenseListAdapterItemViewHolderBinding


class ExpenseListAdapter() : ListAdapter<ExpenseListAdapterItem, ExpenseListAdapter.ItemViewHolder>(
    ExpenseListAdapterItemDiffCallback
) {

    companion object {
        private val FULL_EXPENSE_LIST_ITEM_PAYLOAD_DIFF = ExpenseListPayloadDiff(
            titleChanged = true,
            amountChanged = true,
            descriptionChanged = true,
            dateChanged = true,
            categoryChanged = true,
            paymentMethodChanged = true,
            currencySymbolChanged = true
        )

        val EMPTY_EXPENSE_LIST_ITEM_PAYLOAD_DIFF = ExpenseListPayloadDiff(
            titleChanged = false,
            amountChanged = false,
            descriptionChanged = false,
            dateChanged = false,
            categoryChanged = false,
            paymentMethodChanged = false,
            currencySymbolChanged = false
        )
    }

    interface Callback {
        fun onItemClick(item: ExpenseListAdapterItem)
    }

    private var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ItemViewHolder {
        return ItemViewHolder(
            ExpenseListAdapterItemViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(getItem(position), FULL_EXPENSE_LIST_ITEM_PAYLOAD_DIFF)
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val diff = (
                payloads.filterIsInstance<ExpenseListPayloadDiff>()
                    .takeIf { it.isNotEmpty() }
                    ?: listOf(FULL_EXPENSE_LIST_ITEM_PAYLOAD_DIFF)
                ).fold(EMPTY_EXPENSE_LIST_ITEM_PAYLOAD_DIFF, ExpenseListPayloadDiff::plus)

        holder.onBind(getItem(position), diff)
    }


    fun setItemClickListener(onClick: (item: ExpenseListAdapterItem) -> Unit) {
        this.callback = object : Callback {
            override fun onItemClick(item: ExpenseListAdapterItem) {
                onClick.invoke(item)
            }
        }
    }


    inner class ItemViewHolder(private val binding: ExpenseListAdapterItemViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: ExpenseListAdapterItem

        init {
            binding.root.setOnClickListener {
                callback?.onItemClick(item)
            }
        }

        fun onBind(_item: ExpenseListAdapterItem, payloadDiff: ExpenseListPayloadDiff) {
            this.item = _item
            binding.expenseItemView.setContent(item, payloadDiff)
        }
    }
}