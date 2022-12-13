package com.notes.db.firestore

/**
 * @author Fedotov Yakov
 */
enum class FirebaseFirestoreError(val code: Int, val msg: String) {
    OK(0, ""),
    CANCELLED(1, "Операция была отменена."),
    UNKNOWN(2, "Неизвестная ошибка"),
    DEADLINE_EXCEEDED(4, "Срок действия операции истек"),
    NOT_FOUND(5, "Запрошенный документ не был найден."),
    ALREADY_EXISTS(6, "Документ уже существует."),
    RESOURCE_EXHAUSTED(8, "Ресурс недоступен"),
    FAILED_PRECONDITION(9, "Ошибка операции"),
    ABORTED(10, "Операция была прервана"),
    UNAVAILABLE(14, "Временно недоступно"),
    DATA_LOSS(15, "Данные повреждены"),
    UNAUTHENTICATED(16, "Необходимо авторизоваться");

    companion object {
        fun fetchMsgByCode(code: Int?) =
            values().find { it.code == code }?.msg ?: UNKNOWN.msg
    }
}