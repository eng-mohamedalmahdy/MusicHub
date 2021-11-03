package com.example.musichub

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.musichub.domain.toast
import com.example.musichub.ui.home.Home
import com.example.musichub.ui.musiclist.MusicListViewModel
import com.example.musichub.ui.theme.MusicHubTheme


class MainActivity : ComponentActivity() {
    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>
    private val musicListViewModel: MusicListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicHubTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Home(topBarTitle = getString(R.string.app_name),
                        firstTabText = getString(R.string.music),
                        secondTapText = getString(R.string.albums),
                        musicListViewModel = musicListViewModel)
                }
            }
        }
        requestPermissions()
    }

    private fun requestPermissions() {
        requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.MANAGE_EXTERNAL_STORAGE] == true ||
                    (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
                            permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true)
                ) {

                } else {
                    launchPermissionsRequest(requestMultiplePermissions)
                }
            }

        launchPermissionsRequest(requestMultiplePermissions)
    }

    private fun launchPermissionsRequest(requestMultiplePermissions: ActivityResultLauncher<Array<String>>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

                    )
            )
        } else {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                )
            )
        }
    }
}


