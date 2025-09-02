package ru.surf.learn2invest.domain.animator

import android.view.View
import android.widget.ImageView

/**
 * Интерфейс для кастомной анимации.
 */
interface CustomAnimator {

    /**
     * Анимирует прозрачность представления.
     *
     * @param view        Представление, которое будет анимировано.
     * @param duration    Продолжительность анимации в миллисекундах.
     * @param onStart     Callback, вызываемый при начале анимации. По умолчанию — null.
     * @param onEnd       Callback, вызываемый при завершении анимации. По умолчанию — null.
     * @param onCancel    Callback, вызываемый при отмене анимации. По умолчанию — null.
     * @param onRepeat    Callback, вызываемый при повторении анимации. По умолчанию — null.
     * @param values      Массив значений прозрачности, через которые будет проходить анимация.
     */
    fun animateViewAlpha(
        view: View,
        duration: Long,
        onStart: (() -> Unit)? = null,
        onEnd: (() -> Unit)? = null,
        onCancel: (() -> Unit)? = null,
        onRepeat: (() -> Unit)? = null,
        vararg values: Float,
    )


    suspend fun animateDots(
        dot1: ImageView,
        dot2: ImageView,
        dot3: ImageView,
        dot4: ImageView,
        needReturn: Boolean,
        truePIN: Boolean,
        aimBias: Float,
        duration: Long,
    )
}
