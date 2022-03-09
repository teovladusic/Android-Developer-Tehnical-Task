package com.prizma_distribucija.androiddevelopertask.fakes

import android.app.Activity
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.PermissionManager

class PermissionManagerFakeImpl : PermissionManager {

    companion object  {
        var havePermissionsBeenRequested = false
        var hasPermissions = true
    }

    init {
        setDefaults()
    }

    override fun hasPermissions(activity: Activity): Boolean {
        return hasPermissions
    }

    override fun requestPermissionsIfNeeded(activity: Activity) {
        havePermissionsBeenRequested = true
    }

    private fun setDefaults() {
        havePermissionsBeenRequested = false
        hasPermissions = true
    }
}