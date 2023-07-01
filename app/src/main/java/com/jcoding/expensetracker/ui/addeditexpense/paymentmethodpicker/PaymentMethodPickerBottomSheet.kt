package com.jcoding.expensetracker.ui.addeditexpense.paymentmethodpicker

import com.jcoding.expensetracker.R
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpensePaymentMethod
import com.jcoding.expensetracker.ui.common.chooserbottomsheet.ChooserBottomSheet
import com.jcoding.expensetracker.ui.common.chooserbottomsheet.listadapter.ChooserOption

class PaymentMethodPickerBottomSheet() : ChooserBottomSheet() {
    private val chooserOptionList = ArrayList<ChooserOption>()


    fun addPaymentMethodList(list : List<ExpensePaymentMethod>){
        list.forEach {
            chooserOptionList.add(
                ChooserOption(
                    id = it.type.toString(),
                    titleResId = it.nameResId
                )
            )
        }
        setOptionList(chooserOptionList)
    }


    fun addPaymentMethodSelectedListener(onSelect : (item: ExpensePaymentMethod) -> Unit){
        super.setCallback(){
            onSelect.invoke(ExpensePaymentMethod.getItemByType(it.id.toInt()))
        }
    }



    override fun getTitle(): String {
        return     getString(R.string.labelPaymentMethodType)
    }
}