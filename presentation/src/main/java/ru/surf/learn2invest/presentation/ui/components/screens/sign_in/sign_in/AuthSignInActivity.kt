package ru.surf.learn2invest.presentation.ui.components.screens.sign_in.sign_in

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.surf.learn2invest.presentation.ui.components.screens.sign_in.common.AuthActivity

/**
 * Активность для входа по PIN-коду.
 *
 * Наследует базовую функциональность от [AuthActivity]
 * и использует [AuthSignInActivityViewModel] для управления логикой входа
 */
@AndroidEntryPoint
internal class AuthSignInActivity : AuthActivity() {
    override val viewModel: AuthSignInActivityViewModel by viewModels()
}