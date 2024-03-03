package com.example.mytestgo.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class StoragePermissionHelper(private val activity: Activity) {

    interface PermissionCallback {
        fun onPermissionGranted()
        fun onPermissionDenied()
    }

    companion object {
        private const val REQUEST_STORAGE_PERMISSION_CODE = 123
    }

    private var permissionCallback: PermissionCallback? = null

    open fun requestStoragePermission(callback: PermissionCallback) {
        permissionCallback = callback
        if (hasStoragePermission()) {
            permissionCallback?.onPermissionGranted()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_STORAGE_PERMISSION_CODE
            )
        }
    }

    open fun hasStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    }

    open fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                permissionCallback?.onPermissionGranted()
            } else {
                permissionCallback?.onPermissionDenied()
            }
        }
    }
}
