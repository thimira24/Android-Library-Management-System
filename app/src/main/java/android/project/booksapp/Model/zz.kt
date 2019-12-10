package android.project.booksapp.Model

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

class zz {


    private fun transparentToolbar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            getWindow().setStatusBarColor(Color.TRANSPARENT)
        }
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}
