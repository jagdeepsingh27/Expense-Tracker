package com.jcoding.expensetracker.ui.common.chooserbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jcoding.expensetracker.databinding.ChooserBottomSheetLayoutBinding
import com.jcoding.expensetracker.ui.common.chooserbottomsheet.listadapter.ChooserOption
import com.jcoding.expensetracker.ui.common.chooserbottomsheet.listadapter.ChooserOptionListAdapter
import com.jcoding.expensetracker.util.VerticalSpaceRecyclerItemDecoration

abstract class ChooserBottomSheet : BottomSheetDialogFragment() {
    private var _binding: ChooserBottomSheetLayoutBinding? = null
    private val optionListAdapter by lazy { ChooserOptionListAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ChooserBottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding().root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set title
        binding().titleTextView.text = getTitle()
        //init the adapter
        initAdapter()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    // This property is only valid between onCreateView and onDestroyView.
    private fun binding() = _binding!!


    /* set adapter and linear layout manager to the recyclerview*/
    private fun initAdapter() {
        binding().listRecyclerView.addItemDecoration(
            VerticalSpaceRecyclerItemDecoration(4)
        )
        binding().listRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding().listRecyclerView.adapter = optionListAdapter
    }

    protected fun setOptionList(list: List<ChooserOption>) {
        optionListAdapter.submitList(list)
    }


    protected fun setCallback(onOptionClick: (item: ChooserOption) -> Unit) {
        optionListAdapter.setCallback(object : ChooserOptionListAdapter.Callback {
            override fun onItemClick(item: ChooserOption) {
                onOptionClick.invoke(item)
            }
        })
    }

    abstract fun getTitle(): String
}