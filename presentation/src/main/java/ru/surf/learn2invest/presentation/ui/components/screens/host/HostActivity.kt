package ru.surf.learn2invest.presentation.ui.components.screens.host

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.surf.learn2invest.presentation.R
import ru.surf.learn2invest.presentation.databinding.ActivityHostBinding

/**
 * Главный экран приложения с BottomBar.
 * Эта активность управляет отображением основного контента и навигацией в приложении.
 * Она также настраивает нижнюю навигационную панель (BottomBar) и связывает ее с контроллером навигации.
 */
@AndroidEntryPoint
internal class HostActivity : AppCompatActivity() {

    /**
     * Метод жизненного цикла активити, вызываемый при создании.
     * Здесь настраивается внешний вид экрана и навигация.
     *
     * @param savedInstanceState Состояние, сохраненное при предыдущем запуске.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityHostBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val style = SystemBarStyle.auto(
            this.getColor(R.color.white),
            this.getColor(R.color.main_background_dark),
            {
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            }
        )
        enableEdgeToEdge(
            statusBarStyle = style,
            navigationBarStyle = style
        )
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                0
            )
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.host_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            val navController = navHostFragment.navController
            val currentDestinationId = navController.currentDestination?.id

            if (currentDestinationId == menuItem.itemId) {
                // Уже на этом экране — не навигируем
                false
            } else {
                // Навигация с popUpTo, чтобы не создавать дубликаты в back stack
                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setLaunchSingleTop(true) // Если уже вверху стека — не создаём новый экземпляр
                    .setRestoreState(true)    // Восстанавливаем состояние, если фрагмент уже в back stack
                    .setPopUpTo(
                        menuItem.itemId,
                        false
                    ) // Убираем все фрагменты выше выбранного, но не удаляем сам
                    .build()

                navController.navigate(menuItem.itemId, null, navOptions)
                true
            }
        }
    }
}
