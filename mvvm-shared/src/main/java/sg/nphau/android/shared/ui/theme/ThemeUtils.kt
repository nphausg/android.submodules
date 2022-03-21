/*
 * Created by nphau on 01/11/2021, 00:46
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:44
 */

package sg.nphau.android.shared.ui.theme

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import sg.nphau.android.R

/**
 * Execute [f] only if the current Android SDK version is [version] or newer.
 * Do nothing otherwise.
 */
inline fun doFromSdk(version: Int, f: () -> Unit) {
    if (Build.VERSION.SDK_INT >= version) f()
}

object ThemeUtils {

    @JvmStatic
    fun getScreenHeight(paramContext: Context): Int {
        return paramContext.resources.displayMetrics.heightPixels
    }

    @JvmStatic
    fun getScreenWidth(paramContext: Context): Int {
        return paramContext.resources.displayMetrics.widthPixels
    }

    fun hideSystemUI(activity: Activity) = with(activity.window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            insetsController?.let {
                // Default behavior is that if navigation bar is hidden, the system will "steal" touches
                // and show it again upon user's touch. We just want the user to be able to show the
                // navigation bar by swipe, touches are handled by custom code -> change system bar behavior.
                // Alternative to deprecated SYSTEM_UI_FLAG_IMMERSIVE.
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                // make navigation bar translucent (alternative to deprecated
                // WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                // - do this already in hideSystemUI() so that the bar
                // is translucent if user swipes it up
                navigationBarColor = activity.getColor(R.color.color_nav_bar)
                // Finally, hide the system bars, alternative to View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                // and SYSTEM_UI_FLAG_FULLSCREEN.
                it.hide(WindowInsets.Type.systemBars())
            }
        } else {
            // Enables regular immersive mode.
            // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
            // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility = (
                    // Do not let system steal touches for showing the navigation bar
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Hide the nav bar and status bar
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            // Keep the app content behind the bars even if user swipes them up
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            // make navbar translucent - do this already in hideSystemUI() so that the bar
            // is translucent if user swipes it up
            @Suppress("DEPRECATION")
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    fun showSystemUI(activity: Activity) = with(activity.window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // show app content in fullscreen, i. e. behind the bars when they are shown (alternative to
            // deprecated View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            setDecorFitsSystemWindows(false)
            // finally, show the system bars
            insetsController?.show(WindowInsets.Type.systemBars())
        } else {
            // Shows the system bars by removing all the flags
            // except for the ones that make the content appear under the system bars.
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }

    fun setLightStatusBar(activity: Activity, color: Int = Color.WHITE) = with(activity.window) {
        doFromSdk(Build.VERSION_CODES.M) {
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility =
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = color
        }
    }

    @JvmStatic
    fun setDarkStatusBar(activity: Activity, color: Int = Color.WHITE) = with(activity.window) {
        @Suppress("DEPRECATION")
        (doFromSdk(Build.VERSION_CODES.M) {
            clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            statusBarColor = color
        })
    }

    @JvmStatic
    fun isPortraitMode(context: Context): Boolean =
        context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    @JvmStatic
    fun isDarkTheme(context: Context) = context.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    @JvmStatic
    fun isLightTheme(context: Context) = !isDarkTheme(context)

    @JvmStatic
    fun setNightMode(forceNight: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (forceNight) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}