package ru.surf.learn2invest.domain.cryptography.usecase

import ru.surf.learn2invest.domain.services.settings_manager.SettingsManager
import javax.inject.Inject

/**
 * UseCase для удаления торгового пароля.
 *
 * @param settingsManager Менеджер настроек приложения.
 */
class RemoveTradingPasswordUseCase @Inject constructor(
    private val settingsManager: SettingsManager,
) {
    /**
     * Удаляет хэш торгового пароля из настроек.
     */
    operator fun invoke() {
        settingsManager.update { it.copy(tradingPasswordHash = null) }
    }
}
