package com.example.demoewallet.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.lifecycleScope
import com.example.demoewallet.app.utils.readAssetsFile
import com.example.demoewallet.app.widgets.CreateOrImportWallet
import com.example.demoewallet.app.widgets.CreateWalletView
import com.example.demowallet.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // In Compose world
                MaterialTheme {

                    /*

                    GetMnemonicWordsButton(viewModel,assets, lifecycleOwner =lifecycleScope )*/
                    val viewModel: TempViewModel by viewModels()

                    CreateOrImportWallet(viewModel,assets, lifecycleOwner =lifecycleScope )
                }
            }
        }
    }
}