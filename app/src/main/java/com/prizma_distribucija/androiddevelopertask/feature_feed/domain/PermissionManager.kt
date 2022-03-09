package com.prizma_distribucija.androiddevelopertask.feature_feed.domain

import android.app.Activity

interface PermissionManager {

    fun hasPermissions(activity: Activity) : Boolean

    fun requestPermissionsIfNeeded(activity: Activity)
}