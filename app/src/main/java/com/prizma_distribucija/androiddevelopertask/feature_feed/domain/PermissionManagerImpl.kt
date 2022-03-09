package com.prizma_distribucija.androiddevelopertask.feature_feed.domain

import android.Manifest
import android.app.Activity
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

class PermissionManagerImpl : PermissionManager{

    private val permissionsNeeded = arrayOf(Manifest.permission.CAMERA)

    override fun hasPermissions(activity: Activity): Boolean {
        return EasyPermissions.hasPermissions(activity, *permissionsNeeded)
    }

    override fun requestPermissionsIfNeeded(activity: Activity) {
        if (hasPermissions(activity)) return
        EasyPermissions.requestPermissions(
            activity, "An app needs your permission to access the camera", 1,
            *permissionsNeeded
        )
    }
}