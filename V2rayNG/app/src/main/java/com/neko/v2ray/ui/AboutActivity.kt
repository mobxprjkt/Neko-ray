package com.neko.v2ray.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.tencent.mmkv.MMKV
import com.neko.v2ray.AppConfig
import com.neko.v2ray.BuildConfig
import com.neko.v2ray.R
import com.neko.v2ray.databinding.ActivityAboutBinding
import com.neko.v2ray.extension.toast
import com.neko.v2ray.extension.toastError
import com.neko.v2ray.extension.toastSuccess
import com.neko.v2ray.handler.SpeedtestManager
import com.neko.v2ray.util.Utils
import com.neko.v2ray.util.ZipUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class AboutActivity : BaseActivity() {

    private val binding by lazy { ActivityAboutBinding.inflate(layoutInflater) }
    private val extDir by lazy { File(Utils.backupPath(this)) }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                try {
                    showFileChooser()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                toast(R.string.toast_permission_denied)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.title_about)

        binding.tvBackupSummary.text = this.getString(R.string.summary_configuration_backup, extDir)

        binding.layoutBackup.setOnClickListener {
            val ret = backupConfiguration(extDir.absolutePath)
            if (ret.first) {
                toastSuccess(R.string.toast_success)
            } else {
                toastError(R.string.toast_failure)
            }
        }

        binding.layoutShare.setOnClickListener {
            val ret = backupConfiguration(cacheDir.absolutePath)
            if (ret.first) {
                startActivity(
                    Intent.createChooser(
                        Intent(Intent.ACTION_SEND).setType("application/zip")
                            .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            .putExtra(
                                Intent.EXTRA_STREAM,
                                FileProvider.getUriForFile(
                                    this, BuildConfig.APPLICATION_ID + ".cache", File(ret.second)
                                )
                            ), getString(R.string.title_configuration_share)
                    )
                )
            } else {
                toastError(R.string.toast_failure)
            }
        }

        binding.layoutRestore.setOnClickListener {
            val permission =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }

            if (ContextCompat.checkSelfPermission(this, permission) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                try {
                    showFileChooser()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                requestPermissionLauncher.launch(permission)
            }
        }

        binding.layoutSoureCcode.setOnClickListener {
            Utils.openUri(this, AppConfig.v2rayNGUrl)
        }

        binding.layoutFeedback.setOnClickListener {
            Utils.openUri(this, AppConfig.v2rayNGIssues)
        }

        binding.layoutTgChannel.setOnClickListener {
            Utils.openUri(this, AppConfig.TgChannelUrl)
        }

        binding.layoutPrivacyPolicy.setOnClickListener {
            Utils.openUri(this, AppConfig.v2rayNGPrivacyPolicy)
        }

        "v${BuildConfig.VERSION_NAME} (${SpeedtestManager.getLibVersion()})".also {
            binding.tvVersion.text = it
        }
    }

    private fun backupConfiguration(outputZipFilePos: String): Pair<Boolean, String> {
        val dateFormated = SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())
        val folderName = "${getString(R.string.app_name)}_${dateFormated}"
        val backupDir = this.cacheDir.absolutePath + "/$folderName"
        val outputZipFilePath = "$outputZipFilePos/$folderName.zip"

        val count = MMKV.backupAllToDirectory(backupDir)
        if (count <= 0) {
            return Pair(false, "")
        }

        if (ZipUtil.zipFromFolder(backupDir, outputZipFilePath)) {
            return Pair(true, outputZipFilePath)
        } else {
            return Pair(false, "")
        }
    }

    private fun restoreConfiguration(zipFile: File): Boolean {
        val backupDir = this.cacheDir.absolutePath + "/${System.currentTimeMillis()}"

        if (!ZipUtil.unzipToFolder(zipFile, backupDir)) {
            return false
        }

        val count = MMKV.restoreAllFromDirectory(backupDir)
        return count > 0
    }

    private fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }

        try {
            chooseFile.launch(Intent.createChooser(intent, getString(R.string.title_file_chooser)))
        } catch (ex: android.content.ActivityNotFoundException) {
            Log.e(AppConfig.ANG_PACKAGE, "File chooser activity not found: ${ex.message}", ex)
            toast(R.string.toast_require_file_manager)
        }
    }

    private val chooseFile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val uri = result.data?.data
            if (result.resultCode == RESULT_OK && uri != null) {
                try {
                    val targetFile =
                        File(this.cacheDir.absolutePath, "${System.currentTimeMillis()}.zip")
                    contentResolver.openInputStream(uri).use { input ->
                        targetFile.outputStream().use { fileOut ->
                            input?.copyTo(fileOut)
                        }
                    }
                    if (restoreConfiguration(targetFile)) {
                        toastSuccess(R.string.toast_success)
                    } else {
                        toastError(R.string.toast_failure)
                    }
                } catch (e: Exception) {
                    Log.e(AppConfig.ANG_PACKAGE, "Error during file restore: ${e.message}", e)
                    toastError(R.string.toast_failure)
                }
            }
        }
}