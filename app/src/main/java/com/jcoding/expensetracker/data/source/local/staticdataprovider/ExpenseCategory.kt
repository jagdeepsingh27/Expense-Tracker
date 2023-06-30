package com.jcoding.expensetracker.data.source.local.staticdataprovider

import com.jcoding.expensetracker.R

/***
 * This enum class contains the categories for the expense
 * Each Category have the type , name resource string id and drawable resource id
 * Here "0" Type value denotes the others
 */
//todo replace with database in next version
enum class ExpenseCategory(val type: Int, val nameStringResId: Int, val drawableResId: Int) {
    Education(1, R.string.expenseCateEducation, R.drawable.ic_cate_education),
    Food(2, R.string.expenseCateFood, R.drawable.ic_cate_food),
    Health(3, R.string.expenseCateHealth, R.drawable.ic_cate_health),
    HouseRent(4, R.string.expenseCateHouseRent, R.drawable.ic_cate_house_rent),
    MovieTicket(5, R.string.expenseCateMovieTicket, R.drawable.ic_cate_movie_ticket),
    Shopping(6, R.string.expenseCateShopping, R.drawable.ic_cate_shopping),
    Travel(7, R.string.expenseCateTravel, R.drawable.ic_cate_travel),
    Rent(8, R.string.expenseCateRent, R.drawable.ic_cate_payments),
    Others(0, R.string.expenseCateOthers, R.drawable.ic_cate_others);

    companion object {
        /** this function return the category of input type */
        fun getItemByType(type: Int): ExpenseCategory {
            return when (type) {
                0 -> Others
                1 -> Education
                2 -> Food
                3 -> Health
                4 -> HouseRent
                5 -> MovieTicket
                6 -> Shopping
                7 -> Travel
                8 -> Rent
                else -> throw RuntimeException("invalid category type")
            }
        }

        fun getList(): ArrayList<ExpenseCategory> {
            return ArrayList<ExpenseCategory>().apply {
                add(Education)
                add(Food)
                add(Health)
                add(HouseRent)
                add(MovieTicket)
                add(Shopping)
                add(Travel)
                add(Rent)
                add(Others)
            }
        }
    }
}