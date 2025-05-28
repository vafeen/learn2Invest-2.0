package ru.surf.learn2invest.presentation.ui.main

import android.content.Context
import android.content.Intent
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.surf.learn2invest.domain.animator.usecase.AnimateAlphaUseCase
import ru.surf.learn2invest.domain.services.settings_manager.SettingsManager
import ru.surf.learn2invest.domain.utils.launchIO
import ru.surf.learn2invest.domain.utils.withContextMAIN
import ru.surf.learn2invest.presentation.R
import ru.surf.learn2invest.presentation.ui.components.screens.sign_in.common.AuthActivity
import ru.surf.learn2invest.presentation.ui.components.screens.sign_up.SignUpActivity
import javax.inject.Inject

/**
 * ViewModel для главного экрана приложения.
 * Управляет данными профиля, анимациями и навигацией.
 *
 * @property settingsManager Менеджер для работы с настройками и профилем пользователя
 * @property animateAlphaUseCase UseCase для анимации изменения прозрачности элементов
 * @property context Контекст приложения для доступа к ресурсам
 */
@HiltViewModel
internal class MainActivityViewModel @Inject constructor(
    private val settingsManager: SettingsManager,
    private val animateAlphaUseCase: AnimateAlphaUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _effects = MutableSharedFlow<MainActivityEffect>()

    /** Поток для обработки UI-эффектов (навигация, завершение активности) */
    val effects = _effects.asSharedFlow()

    /**
     * Обрабатывает входящие Intent-ы от активности.
     *
     * @param intent Входящий Intent для обработки. Поддерживается только [MainActivityIntent.ProcessSplash]
     */
    fun handleIntent(intent: MainActivityIntent) {
        viewModelScope.launchIO {
            when (intent) {
                is MainActivityIntent.ProcessSplash -> processSplash(intent.textView)
            }
        }
    }

    /**
     * Обрабатывает логику отображения splash-экрана.
     *
     * @param textView TextView для отображения приветственного сообщения
     *
     * Логика:
     * 1. Проверяет заполненность профиля
     * 2. При валидном профиле:
     *    - Анимирует появление приветствия
     *    - Перенаправляет на экран авторизации
     * 3. При невалидном профиле:
     *    - Перенаправляет на экран регистрации после задержки
     */
    private suspend fun processSplash(textView: TextView) {
        val profile = profileFlow.value
        if (profile.firstName != "undefined" &&
            profile.lastName != "undefined" &&
            profile.hash != null
        ) {
            withContextMAIN {
                textView.alpha = 0f
                textView.text =
                    "${context.getString(R.string.hello)}, ${profileFlow.value.firstName}!"
                animateAlphaUseCase.invoke(
                    view = textView,
                    duration = 2000,
                    onEnd = {
                        viewModelScope.launchIO {
                            _effects.emit(MainActivityEffect.StartIntent {
                                AuthActivity.newInstanceSignIN(it)
                            })
                            _effects.emit(MainActivityEffect.Finish)
                        }
                    },
                    values = floatArrayOf(
                        0f,
                        1f
                    )
                )
            }
        } else {
            delay(2000)
            _effects.emit(MainActivityEffect.StartIntent {
                Intent(it, SignUpActivity::class.java)
            })
            _effects.emit(MainActivityEffect.Finish)
        }
    }

    /** Поток данных профиля пользователя */
    private val profileFlow = settingsManager.settingsFlow
}
