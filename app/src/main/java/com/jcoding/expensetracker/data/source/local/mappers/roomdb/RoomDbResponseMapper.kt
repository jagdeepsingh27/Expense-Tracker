package com.jcoding.expensetracker.data.source.local.mappers.roomdb

import com.jcoding.expensetracker.data.model.common.GeneralResponse

interface RoomDbResponseMapper {
    fun <T> processResponse(input: T): GeneralResponse<T>
    fun  processDeleteItemResponse(input: Int): GeneralResponse<Unit>
    fun  processUpdateItemResponse(input: Int): GeneralResponse<Unit>
}