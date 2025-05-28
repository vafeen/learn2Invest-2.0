package ru.surf.learn2invest.presentation.ui.components.alert_dialogs.reset_stats

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.surf.learn2invest.domain.database.usecase.ClearAppDatabaseUseCase
import ru.surf.learn2invest.domain.services.settings_manager.SettingsManager
import ru.surf.learn2invest.domain.utils.launchIO
import ru.surf.learn2invest.domain.utils.withContextIO
import ru.surf.learn2invest.presentation.R
import javax.inject.Inject

/**
 * ViewModel для диалога сброса статистики.
 *
 * Управляет логикой сброса статистики пользователя, включая обнуление баланса и очистку базы данных.
 *
 * @param settingsManager Менеджер профиля для управления профилем пользователя.
 * @param clearAppDatabaseUseCase UseCase для очистки базы данных приложения.
 * @param context Контекст приложения для доступа к ресурсам.
 */
@HiltViewModel
internal class ResetStatsDialogViewModel @Inject constructor(
    private val settingsManager: SettingsManager,
    private val clearAppDatabaseUseCase: ClearAppDatabaseUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    /**
     * Поток эффектов для обработки событий UI (закрытие диалога, toast-уведомления и т.п.).
     */
    private val _effects = MutableSharedFlow<ResetStatsDialogEffect>()
    val effects = _effects.asSharedFlow()

    /**
     * Поток состояния диалога.
     */
    private val _state = MutableStateFlow(
        ResetStatsDialogState(
            text = context.getString(R.string.reset_stats)
        )
    )
    val state = _state.asStateFlow()

    /**
     * Обрабатывает интент пользователя (сбросить статистику или закрыть диалог).
     *
     * @param intent Интент, определяющий действие пользователя.
     */
    fun handleIntent(intent: ResetStatsDialogIntent) {
        viewModelScope.launchIO {
            when (intent) {
                ResetStatsDialogIntent.ResetStats -> resetStats()
                ResetStatsDialogIntent.Dismiss -> _effects.emit(ResetStatsDialogEffect.Dismiss)
            }
        }
    }

    /**
     * Сбрасывает статистику пользователя: обнуляет баланс и очищает базу данных.
     *
     * После сброса отправляет эффект для отображения toast-уведомления и закрытия диалога.
     */
    private suspend fun resetStats() {
        // Создаем копию теку6щего профиля с обнулением балансов
        val savedProfile = settingsManager.settingsFlow.value.copy(
            fiatBalance = 0f,
            assetBalance = 0f
        )

        // Обработка сброса данных в фоновом потоке
        withContextIO {
            clearAppDatabaseUseCase() // Очищаем базу данных
            settingsManager.update { savedProfile } // Обновляем профиль
        }

        // Показ уведомления о сбросе статистики
        _effects.emit(ResetStatsDialogEffect.ToastResetStateSuccessful)
        _effects.emit(ResetStatsDialogEffect.Dismiss)
    }
}
