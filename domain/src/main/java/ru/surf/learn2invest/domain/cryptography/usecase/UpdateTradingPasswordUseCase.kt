package ru.surf.learn2invest.domain.cryptography.usecase

import ru.surf.learn2invest.domain.cryptography.PasswordHasher
import ru.surf.learn2invest.domain.services.settings_manager.SettingsManager
import javax.inject.Inject

/**
 * UseCase для обновления торгового пароля.
 *
 * @param settingsManager Менеджер настроек приложения.
 * @param passwordHasher Хэшер паролей для преобразования торгового пароля в хэш.
 */
class UpdateTradingPasswordUseCase @Inject constructor(
    private val settingsManager: SettingsManager,
    private val passwordHasher: PasswordHasher,
) {
    /**
     * Обновляет торговый пароль, сохраняя его хэш в настройках.
     *
     * @param newTradingPassword Новый торговый пароль.
     */
    operator fun invoke(newTradingPassword: String) {
        settingsManager.update {
            it.copy(
                tradingPasswordHash = passwordHasher.passwordToHash(
                    firstName = it.firstName,
                    lastName = it.lastName,
                    password = newTradingPassword
                )
            )
        }
    }
}
