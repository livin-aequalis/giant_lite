package com.example.demoewallet.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.lifecycleScope
import com.example.demoewallet.app.utils.readAssetsFile
import com.example.demoewallet.app.widgets.CreateOrImportWallet
import com.example.demoewallet.app.widgets.CreateWalletView
import com.example.demowallet.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import jp.co.soramitsu.common.compose.theme.FearlessTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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