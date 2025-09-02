package ru.surf.learn2invest.domain.animator.usecase

import android.widget.ImageView
import ru.surf.learn2invest.domain.animator.CustomAnimator
import javax.inject.Inject

/**
 * Юзкейс анимирования движения точки от PIN-кода к центру.
 */
class AnimateDotsUseCase @Inject constructor(private val customAnimator: CustomAnimator) {

    suspend operator fun invoke(
        dot1: ImageView,
        dot2: ImageView,
        dot3: ImageView,
        dot4: ImageView,
        needReturn: Boolean,
        truePIN: Boolean,
    ) {
        val aimBias = 0.5f
        val duration = 300L
        customAnimator.animateDots(dot1, dot2, dot3, dot4, needReturn, truePIN, aimBias, duration)
    }
}