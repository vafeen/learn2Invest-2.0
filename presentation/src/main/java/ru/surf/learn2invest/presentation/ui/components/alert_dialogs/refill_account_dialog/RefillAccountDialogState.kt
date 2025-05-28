package ru.surf.learn2invest.presentation.ui.components.alert_dialogs.refill_account_dialog

/**
 * Состояние диалога пополнения баланса.
 *
 * @property balance Текущий баланс пользователя.
 * @property enteredBalance Введённая пользователем сумма для пополнения.
 */
internal data class RefillAccountDialogState(
    val balance: Float,
    val enteredBalance: String = "",
)
