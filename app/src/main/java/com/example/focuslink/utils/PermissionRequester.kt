package com.example.focuslink.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionRequester {

    fun pedirPermisos(
        context: Context,
        permiso: String,
        launcher: ActivityResultLauncher<String>
    ) {
        if (ContextCompat.checkSelfPermission(context, permiso) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permiso", "Ya concedido: $permiso")
        } else {
            launcher.launch(permiso)
        }
    }

    fun permisosNecesarios(): List<String> {
        val permisos = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permisos.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permisos.add(Manifest.permission.VIBRATE)
        }

        return permisos
    }
}
