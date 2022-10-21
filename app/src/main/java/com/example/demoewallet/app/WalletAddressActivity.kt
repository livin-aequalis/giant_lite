package com.example.demoewallet.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.demoewallet.app.theme.colorPrimary
import com.example.demoewallet.app.widgets.ImportWalletView
import com.example.demoewallet.app.widgets.WalletAddressView
import dagger.hilt.android.AndroidEntryPoint
import jp.co.soramitsu.common.compose.theme.FearlessTheme

@AndroidEntryPoint
class WalletAddressActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "GIANT Wallet")
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    onBackPressed()
                                }) {
                                    Icon(Icons.Filled.ArrowBack, "")
                                }
                            },
                            backgroundColor = colorPrimary,
                            contentColor = Color.White,
                            elevation = 12.dp
                        )
                    },
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = paddingValues.calculateBottomPadding())
                            .verticalScroll(rememberScrollState())
                    ) {
                        val viewModel: TempViewModel by viewModels()
                        val address = intent.getStringExtra("address")
                        if (address != null) {
                            WalletAddressView(
                                viewModel,
                                assets,
                                lifecycleOwner = lifecycleScope,
                                address
                            )
                        }
                    }
                }
            }

        }
    }
}
