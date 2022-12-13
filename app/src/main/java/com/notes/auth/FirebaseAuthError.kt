package com.notes.auth

/**
 * @author Fedotov Yakov
 */
enum class FirebaseAuthError(val code: String, val msg: String) {
    INVALID_CUSTOM_TOKEN(
        "ERROR_INVALID_CUSTOM_TOKEN",
        "Пользовательский формат токена неверен. Пожалуйста, ознакомьтесь с документацией."
    ),
    CUSTOM_TOKEN_MISMATCH(
        "ERROR_CUSTOM_TOKEN_MISMATCH",
        "Пользовательский токен соответствует другой аудитории."
    ),
    INVALID_CREDENTIAL(
        "ERROR_INVALID_CREDENTIAL",
        "Предоставленные учетные данные для авторизации неверно оформлены или срок их действия истек."
    ),
    INVALID_EMAIL("ERROR_INVALID_EMAIL", "Адрес электронной почты плохо отформатирован."),
    WRONG_PASSWORD("ERROR_WRONG_PASSWORD", "Пароль неверен или у пользователя нет пароля."),
    USER_MISMATCH(
        "ERROR_USER_MISMATCH",
        "Предоставленные учетные данные не соответствуют ранее вошедшему в систему пользователю."
    ),
    REQUIRES_RECENT_LOGIN(
        "ERROR_REQUIRES_RECENT_LOGIN",
        "Предоставленные учетные данные не соответствуют ранее вошедшему в систему пользователю."
    ),
    ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL(
        "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL",
        "Учетная запись уже существует с тем же адресом электронной почты, но другими учетными данными для входа. Войдите в систему, используя провайдера, связанного с этим адресом электронной почты."
    ),
    EMAIL_ALREADY_IN_USE(
        "ERROR_EMAIL_ALREADY_IN_USE",
        "Адрес электронной почты уже используется другой учетной записью."
    ),
    CREDENTIAL_ALREADY_IN_USE(
        "ERROR_CREDENTIAL_ALREADY_IN_USE",
        "Эти учетные данные уже связаны с другой учетной записью пользователя."
    ),
    USER_DISABLED(
        "ERROR_USER_DISABLED",
        "Учетная запись пользователя была отключена администратором."
    ),
    USER_TOKEN_EXPIRED(
        "ERROR_USER_TOKEN_EXPIRED",
        "Учетные данные пользователя больше не действительны. Пользователь должен снова войти в систему."
    ),
    USER_NOT_FOUND(
        "ERROR_USER_NOT_FOUND",
        "Нет записи пользователя, соответствующей этому идентификатору. Возможно, пользователь был удален."
    ),
    INVALID_USER_TOKEN(
        "ERROR_INVALID_USER_TOKEN",
        "Учетные данные пользователя больше не действительны. Пользователь должен снова войти в систему."
    ),
    OPERATION_NOT_ALLOWED(
        "ERROR_OPERATION_NOT_ALLOWED",
        "Эта операция не разрешена. Вы должны включить эту службу в консоли."
    ),
    WEAK_PASSWORD("ERROR_WEAK_PASSWORD", "Указанный пароль недействителен."),
    MISSING_EMAIL("ERROR_MISSING_EMAIL", "Необходимо указать адрес электронной почты."),
    UNKNOWN("UNKNOWN", "Неизвестная ошибка");

    companion object {
        fun fetchMsgByCode(code: String?) =
            values().find { it.code == code }?.msg ?: UNKNOWN.msg
    }
}