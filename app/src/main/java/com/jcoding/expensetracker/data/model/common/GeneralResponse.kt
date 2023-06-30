package com.jcoding.expensetracker.data.model.common


class GeneralResponse<T> constructor(
    var status: Boolean,
    var message: String,
    var data: T? = null
) {
    fun isSuccess() = status
    fun requireData(): T = requireNotNull(data)
    override fun toString(): String {
        return "${this.javaClass.name}(status = $status, message = $message, data = $data)"
    }

    fun <T> copy(): GeneralResponse<T> {
        return GeneralResponse<T>(
            status = status,
            message = message
        )
    }
}
