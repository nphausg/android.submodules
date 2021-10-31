/*
 * Created by nphau on 01/11/2021, 00:47
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package com.nphau.android.shared.libs

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.pm.PackageInfoCompat
import com.nphau.android.R

object CommonUtils {

    fun getAppVersion(context: Context): String {
        return String.format(
            "%s(%s)",
            getAppVersionName(context),
            getAppVersionCode(context)
        )
    }

    fun getAppVersionName(context: Context): String {
        var versionName = ""
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            versionName = packageInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionName
    }

    fun getAppVersionCode(context: Context): String {
        var versionCode = ""
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            versionCode = PackageInfoCompat.getLongVersionCode(packageInfo).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionCode
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun makeVibrator(context: Context, amplitude: Long = 10) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v?.vibrate(VibrationEffect.createOneShot(amplitude, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v?.vibrate(amplitude)
        }
    }

    fun openGpsSetting(activity: Activity, requestCode: Int) {
        try {
            activity.startActivityForResult(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                requestCode
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun goToSettings(activity: Activity, requestCode: Int = -1) {
        if (requestCode == -1) {
            activity.startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:" + activity.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            })
        } else {
            try {
                activity.startActivityForResult(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    addCategory(Intent.CATEGORY_DEFAULT)
                    data = Uri.parse("package:" + activity.packageName)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                }, requestCode)
            } catch (e: Exception) {
                activity.startActivityForResult(
                    Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS),
                    requestCode
                )
            }
        }
    }

    fun isChromeOs(context: Context): Boolean {
        return context.packageManager.hasSystemFeature("org.chromium.arc.device_management")
    }

    fun hasTelephony(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
    }

    fun hasMicrophone(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun hasFingerprint(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }

    fun hasBluetooth(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
    }

    fun copyTextToClipboard(context: Context, text: String?) {
        (context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)
            ?.setPrimaryClip(ClipData.newPlainText("code", text))
        Toast.makeText(context, R.string.common_copied, Toast.LENGTH_LONG).show()
    }


    fun openStore(context: Context) {
        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}"))
            )
        } catch (e: Exception) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                )
            )
        }
    }

    fun isAppInstalled(mActivity: Activity, packageClass: String): Boolean {
        val pm = mActivity.packageManager
        var isInstalled = false
        try {
            pm.getPackageInfo(packageClass, PackageManager.GET_ACTIVITIES)
            isInstalled = true
        } catch (e: Exception) {
        }
        return isInstalled
    }

    fun openNetworkSetting(activity: Activity) {
        activity.startActivity(Intent(Settings.ACTION_SETTINGS))
    }

}
