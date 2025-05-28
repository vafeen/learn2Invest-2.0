package ru.surf.learn2invest.presentation.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ru.surf.learn2invest.domain.utils.launchMAIN
import ru.surf.learn2invest.presentation.databinding.ActivityMainBinding

@AndroidEntryPoint
internal class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        initListeners()
        viewModel.handleIntent(MainActivityIntent.ProcessSplash(binding.splashTextView))
    }

    private fun initListeners() {
        lifecycleScope.launchMAIN {
            viewModel.effects.collect { effect ->
                when (effect) {
                    MainActivityEffect.Finish -> this@MainActivity.finish()
                    is MainActivityEffect.StartIntent -> startActivity(effect.creating(this@MainActivity))
                }
            }
        }
    }

}
