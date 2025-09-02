package ru.surf.learn2invest.data.animator

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.surf.learn2invest.domain.animator.CustomAnimator
import ru.surf.learn2invest.domain.utils.animatorListener
import ru.surf.learn2invest.domain.utils.changeDrawableColor
import ru.surf.learn2invest.domain.utils.horizontalBias
import javax.inject.Inject

internal class CustomAnimatorImpl @Inject constructor() : CustomAnimator {

    override fun animateViewAlpha(
        view: View,
        duration: Long,
        onStart: (() -> Unit)?,
        onEnd: (() -> Unit)?,
        onCancel: (() -> Unit)?,
        onRepeat: (() -> Unit)?,
        values: FloatArray,
    ) {
        ObjectAnimator.ofFloat(view, "alpha", *values).also { animator ->
            animator.duration = duration
            animator.addListener(
                animatorListener(
                    onAnimationStart = {
                        onStart?.invoke()
                    },
                    onAnimationEnd = {
                        onEnd?.invoke()
                    }, onAnimationCancel = { onCancel?.invoke() }, onAnimationRepeat = {
                        onRepeat?.invoke()
                    })
            )
        }.start()
    }


    private fun animateHorizontalBiasImpl(
        view: View,
        duration: Long,
        values: FloatArray,
        onEnd: (() -> Unit)? = null,
    ): ValueAnimator = ValueAnimator.ofFloat(*values).also {
        it.duration = duration
        it.addUpdateListener { animator ->
            val biasValue = animator.animatedValue as Float
            val params = view.layoutParams as ConstraintLayout.LayoutParams
            params.horizontalBias = biasValue
            view.layoutParams = params
        }
        it.doOnEnd {
            onEnd?.invoke()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun animateDotsImpl(
        dot1: ImageView,
        dot2: ImageView,
        dot3: ImageView,
        dot4: ImageView,
        aimBias: Float,
        duration: Long
    ) {
        val homeBiasDot1 = dot1.horizontalBias()
        val homeBiasDot2 = dot2.horizontalBias()
        val homeBiasDot3 = dot3.horizontalBias()
        val homeBiasDot4 = dot4.horizontalBias()
        suspendCancellableCoroutine { continuation ->
            AnimatorSet().apply {
                playTogether(
                    animateHorizontalBiasImpl(
                        view = dot1,
                        duration = duration,
                        values = floatArrayOf(homeBiasDot1, aimBias),
                    ),
                    animateHorizontalBiasImpl(
                        view = dot2,
                        duration = duration,
                        values = floatArrayOf(homeBiasDot2, aimBias),
                    ),
                    animateHorizontalBiasImpl(
                        view = dot3,
                        duration = duration,
                        values = floatArrayOf(homeBiasDot3, aimBias),
                    ),
                    animateHorizontalBiasImpl(
                        view = dot4,
                        duration = duration,
                        values = floatArrayOf(homeBiasDot4, aimBias),
                    ),
                )
                doOnEnd {
                    continuation.resume(Unit, null)
                }
            }.start()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun returnDots(
        dot1: ImageView,
        dot2: ImageView,
        dot3: ImageView,
        dot4: ImageView,
        aimDot1Bias: Float,
        aimDot2Bias: Float,
        aimDot3Bias: Float,
        aimDot4Bias: Float,
        duration: Long
    ) {
        val currentDot1Bias = dot1.horizontalBias()
        val currentDot2Bias = dot2.horizontalBias()
        val currentDot3Bias = dot3.horizontalBias()
        val currentDot4Bias = dot4.horizontalBias()

        suspendCancellableCoroutine { continuation ->
            val animator = AnimatorSet()
            animator.apply {
                playTogether(
                    animateHorizontalBiasImpl(
                        view = dot1,
                        duration = duration,
                        values = floatArrayOf(currentDot1Bias, aimDot1Bias),
                    ),
                    animateHorizontalBiasImpl(
                        view = dot2,
                        duration = duration,
                        values = floatArrayOf(currentDot2Bias, aimDot2Bias),
                    ),
                    animateHorizontalBiasImpl(
                        view = dot3,
                        duration = duration,
                        values = floatArrayOf(currentDot3Bias, aimDot3Bias),
                    ),
                    animateHorizontalBiasImpl(
                        view = dot4,
                        duration = duration,
                        values = floatArrayOf(currentDot4Bias, aimDot4Bias),
                    )
                )
                doOnEnd {
                    continuation.resume(Unit, null)
                }
                doOnCancel {
                    continuation.cancel()
                }
            }
            // Обработка отмены корутины
            continuation.invokeOnCancellation {
                animator.cancel() // отменяем анимацию
            }
            animator.start()
        }
    }

    override suspend fun animateDots(
        dot1: ImageView,
        dot2: ImageView,
        dot3: ImageView,
        dot4: ImageView,
        needReturn: Boolean,
        truePIN: Boolean,
        aimBias: Float,
        duration: Long
    ) {
        val homeBiasDot1 = dot1.horizontalBias()
        val homeBiasDot2 = dot2.horizontalBias()
        val homeBiasDot3 = dot3.horizontalBias()
        val homeBiasDot4 = dot4.horizontalBias()
        animateDotsImpl(dot1, dot2, dot3, dot4, aimBias, duration)

        changeDrawableColor(
            if (truePIN) {
                Color.GREEN // Установка цвета в зеленый при верном PIN-коде.
            } else {
                Color.RED // Установка цвета в красный при неверном PIN-коде.
            }, arrayOf(dot1, dot2, dot3, dot4)
        )
        delay(800)
        if (needReturn) {
            changeDrawableColor(Color.WHITE, arrayOf(dot1, dot2, dot3, dot4))
            returnDots(
                dot1,
                dot2,
                dot3,
                dot4,
                homeBiasDot1,
                homeBiasDot2,
                homeBiasDot3,
                homeBiasDot4,
                duration,
            )
        }
    }


}