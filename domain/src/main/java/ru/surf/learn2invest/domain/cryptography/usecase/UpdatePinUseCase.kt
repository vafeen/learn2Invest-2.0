package ru.surf.learn2invest.domain.cryptography.usecase

import ru.surf.learn2invest.domain.cryptography.PasswordHasher
import ru.surf.learn2invest.domain.services.settings_manager.SettingsManager
import javax.inject.Inject

/**
 * UseCase для обновления PIN-кода.
 *
 * @param settingsManager Менеджер настроек приложения.
 * @param passwordHasher Хэшер паролей для преобразования PIN-кода в хэш.
 */
class UpdatePinUseCase @Inject constructor(
    private val settingsManager: SettingsManager,
    private val passwordHasher: PasswordHasher,
) {
    /**
     * Обновляет PIN-код, сохраняя его хэш в настройках.
     *
     * @param newPin Новый PIN-код.
     */
    operator fun invoke(newPin: String) {
        settingsManager.update {
            it.copy(
                hash = passwordHasher.passwordToHash(
                    firstName = it.firstName,
                    lastName = it.lastName,
                    password = newPin
                )
            )
        }
    }
}
