package com.neko.v2ray.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.mikepenz.aboutlibraries.LibsBuilder
import com.neko.appupdater.AppUpdater
import com.neko.appupdater.enums.Display
import com.neko.appupdater.enums.UpdateFrom
import com.neko.nointernet.callbacks.ConnectionCallback
import com.neko.nointernet.dialogs.signal.NoInternetDialogSignal
import com.neko.v2ray.AppConfig
import com.neko.v2ray.R
import com.neko.v2ray.extension.toast
import com.neko.v2ray.util.Utils

class NekoAboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.uwu_about_activity)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val toolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .replace(R.id.content_wrapper, NekoAboutFragment())
            .commit()
    }

    fun uwuUpdater(view: View) {
        startNoInternetDialog()

        AppUpdater(this).apply {
            setUpdateFrom(UpdateFrom.JSON)
            setUpdateJSON(AppConfig.UWU_UPDATE_URL)
            setDisplay(Display.DIALOG)
            showAppUpdated(true)
            setCancelable(false)
            setButtonDoNotShowAgain("")
            start()
        }
    }

    fun uwuRepository(view: View) {
        Utils.openUri(this, AppConfig.v2rayNGUrl)
    }

    fun promotion(view: View) {
        Utils.openUri(this, AppConfig.PromotionUrl)
    }

    fun license(view: View) {
        LibsBuilder()
            .withSortEnabled(true)
            .withLicenseShown(true)
            .withLicenseDialog(true)
            .withVersionShown(true)
            .withAboutIconShown(true)
            .withAboutMinimalDesign(false)
            .withAboutVersionShown(true)
            .withAboutVersionShownName(true)
            .withAboutVersionShownCode(true)
            .withSearchEnabled(true)
            .withActivityTitle(getString(R.string.uwu_license_title))
            .start(this)
    }

    fun privacypolicy(view: View) {
        Utils.openUri(this, AppConfig.v2rayNGPrivacyPolicy)
    }

    fun uwumodder(view: View) {
        Utils.openUri(this, AppConfig.TgChannelUrl)
    }

    fun uwuCredits(view: View) {
        startActivity(Intent(this, CreditsActivity::class.java))
    }

    fun changelog(view: View) {
        Utils.openUri(this, AppConfig.UWU_CHANGELOG_URL)
    }

    private fun startNoInternetDialog() {
        NoInternetDialogSignal.Builder(this, lifecycle).apply {
            dialogProperties.apply {
                connectionCallback = object : ConnectionCallback {
                    override fun hasActiveConnection(hasActiveConnection: Boolean) {
                        // Optionally handle connection changes
                    }
                }
                cancelable = true
                showInternetOnButtons = true
                showAirplaneModeOffButtons = true
            }
        }.build()
    }
}
