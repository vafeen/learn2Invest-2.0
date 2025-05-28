package ru.surf.learn2invest.presentation.ui.components.screens.sign_in.changing_pin

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.surf.learn2invest.presentation.ui.components.screens.sign_in.common.AuthActivity

/**
 * Активность для изменения PIN-кода.
 *
 * Наследует базовую функциональность аутентификации от [AuthActivity]
 * и использует специализированную ViewModel [AuthChangingPinActivityViewModel]
 */
@AndroidEntryPoint
internal class AuthChangingPinActivity() : AuthActivity() {
    override val viewModel: AuthChangingPinActivityViewModel by viewModels()
}