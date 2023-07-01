package com.jcoding.expensetracker.ui.addeditexpense.expensecategorypicker

import com.jcoding.expensetracker.R
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpenseCategory
import com.jcoding.expensetracker.ui.common.chooserbottomsheet.ChooserBottomSheet
import com.jcoding.expensetracker.ui.common.chooserbottomsheet.listadapter.ChooserOption

class ExpenseCategoryPickerBottomSheet() : ChooserBottomSheet() {
    private val chooserOptionList = ArrayList<ChooserOption>()


    fun addCategoryList(list : List<ExpenseCategory>){
        list.forEach {
            chooserOptionList.add(
                ChooserOption(
                    id = it.type.toString(),
                    titleResId = it.nameStringResId,
                    iconResId = it.drawableResId
                )
            )
        }
        setOptionList(chooserOptionList)
    }


    fun addCategorySelectedListener(onSelect : (item: ExpenseCategory) -> Unit){
        super.setCallback(){
            onSelect.invoke(ExpenseCategory.getItemByType(it.id.toInt()))
        }
    }



    override fun getTitle(): String {
        return     getString(R.string.labelCategory)
    }
}