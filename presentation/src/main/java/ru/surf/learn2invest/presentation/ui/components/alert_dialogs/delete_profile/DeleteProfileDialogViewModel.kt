
package ru.surf.learn2invest.presentation.ui.components.alert_dialogs.delete_profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.surf.learn2invest.domain.database.usecase.ClearAppDatabaseUseCase
import ru.surf.learn2invest.domain.utils.launchIO
import ru.surf.learn2invest.presentation.R
import javax.inject.Inject

/**
 * ViewModel для диалога подтверждения удаления профиля.
 * Управляет логикой удаления профиля и закрытия диалога.
 *
 * @property clearAppDatabaseUseCase UseCase для очистки базы данных приложения (удаления профиля).
 * @property context Контекст приложения для доступа к ресурсам.
 */
@HiltViewModel
internal class DeleteProfileDialogViewModel @Inject constructor(
    private val clearAppDatabaseUseCase: ClearAppDatabaseUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow(
        DeleteProfileDialogState(
            text = context.getString(R.string.asking_to_delete_profile)
        )
    )

    /** Поток состояния диалога. */
    val state = _state.asStateFlow()
    private val _effect = MutableSharedFlow<DeleteProfileDialogEffect>()

    /** Поток эффектов (навигация, закрытие диалога и т.д.). */
    val effect: SharedFlow<DeleteProfileDialogEffect> = _effect

    /**
     * Обрабатывает входящие интенты (действия пользователя).
     *
     * @param intent Интент, пришедший от пользователя (удалить профиль или отменить).
     */
    fun handle(intent: DeleteProfileDialogIntent) {
        when (intent) {
            DeleteProfileDialogIntent.DeleteProfile -> deleteProfile()
            DeleteProfileDialogIntent.Dismiss -> dismiss()
        }
    }

    /**
     * Закрывает диалог.
     */
    private fun dismiss() {
        viewModelScope.launchIO {
            _effect.emit(DeleteProfileDialogEffect.Dismiss)
        }
    }

    /**
     * Удаляет профиль и перезапускает приложение.
     */
    private fun deleteProfile() {
        viewModelScope.launchIO {
            clearAppDatabaseUseCase()
            _effect.emit(DeleteProfileDialogEffect.DismissAndRestartApp)
        }
    }
}
