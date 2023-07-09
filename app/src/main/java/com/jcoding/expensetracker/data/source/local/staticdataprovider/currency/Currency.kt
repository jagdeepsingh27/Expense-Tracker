package com.jcoding.expensetracker.data.source.local.staticdataprovider.currency

enum class Currency(val id: Int, val symbol: String) {
    INDIAN_RUPEE(1, symbol = "₹"),
    DOLLAR(2, symbol = "\$"),
    EURO(3, symbol = "€"),
    POUND(4, symbol = "£"),
    OTHERS(5, symbol = "");
    companion object{
        /** this function return the item of input type */
        fun getItemById(type: Int): Currency {
            return when (type) {
                1 -> INDIAN_RUPEE
                2 -> DOLLAR
                3 -> EURO
                4 -> POUND
                5 -> OTHERS
                else -> throw RuntimeException("invalid category type")
            }
        }
    }
}
