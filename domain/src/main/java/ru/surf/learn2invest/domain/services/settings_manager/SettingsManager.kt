package ru.surf.learn2invest.domain.services.settings_manager

import kotlinx.coroutines.flow.StateFlow
import ru.surf.learn2invest.domain.domain_models.Settings

/**
 * Интерфейс менеджера настроек приложения.
 */
interface SettingsManager {

    /**
     * Поток состояний настроек, который можно наблюдать для получения актуальных данных.
     */
    val settingsFlow: StateFlow<Settings>

    /**
     * Обновляет настройки, применяя функцию обновления к текущему состоянию.
     *
     * @param updating Функция, принимающая текущие настройки и возвращающая обновлённые.
     */
    fun update(updating: (Settings) -> Settings)
}
