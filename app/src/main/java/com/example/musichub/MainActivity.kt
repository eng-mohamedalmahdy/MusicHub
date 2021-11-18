package com.example.musichub

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.musichub.domain.services.MusicPlayerService
import com.example.musichub.ui.home.Home
import com.example.musichub.ui.musicpage.MusicListViewModel
import com.example.musichub.ui.theme.MusicHubTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

private const val TAG = "MainActivity"


@AndroidEntryPoint
class MainActivity : ComponentActivity(), ServiceConnection {
    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>
    private val musicListViewModel: MusicListViewModel by viewModels()
    private var musicPlayerService: MusicPlayerService? = null
    private var permissionsGranted = MutableStateFlow(true)


    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicHubTheme {
                val permissionsGrantedState = permissionsGranted.collectAsState()
                Surface(color = MaterialTheme.colors.background) {
                    if (permissionsGrantedState.value) {
                        Home(
                            topBarTitle = getString(R.string.app_name),
                            musicListViewModel = musicListViewModel
                        )
                    }

                }
            }
        }

        requestPermissions {
            lifecycleScope.launchWhenStarted {
                initMusicService()
                permissionsGranted.emit(true)
            }
        }
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as MusicPlayerService.MusicPlayerServiceBinder
        musicPlayerService = binder.getService()
        Log.d(TAG, "onServiceDisconnected: Music player service connected")

    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicPlayerService = null
        Log.d(TAG, "onServiceDisconnected: Music player service disconnected")
    }

    override fun onPause() {
        super.onPause()
        unbindService(this)
    }

    override fun onResume() {
        super.onResume()
        initMusicService()
    }

    private fun initMusicService() {
        val intent = Intent(this, MusicPlayerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, intent)
        } else {
            startService(intent)
        }
        bindService(intent, this, BIND_AUTO_CREATE)
    }

    private fun requestPermissions(onPermissionGrant: () -> Unit) {
        requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.MANAGE_EXTERNAL_STORAGE] == true ||
                    (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
                            permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true)
                ) {
                    onPermissionGrant()
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
