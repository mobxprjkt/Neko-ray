package com.neko.v2ray.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.appcompat.widget.Toolbar
import com.blacksquircle.ui.editorkit.utils.EditorTheme
import com.blacksquircle.ui.language.json.JsonLanguage
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.gson.*
import com.tencent.mmkv.MMKV
import com.neko.v2ray.R
import com.neko.v2ray.databinding.ActivityServerCustomConfigBinding
import com.neko.v2ray.dto.EConfigType
import com.neko.v2ray.dto.ServerConfig
import com.neko.v2ray.dto.V2rayConfig
import com.neko.v2ray.extension.toast
import com.neko.v2ray.util.MmkvManager
import com.neko.v2ray.util.Utils
import me.drakeet.support.toast.ToastCompat

class ServerCustomConfigActivity : BaseActivity() {
    private lateinit var binding: ActivityServerCustomConfigBinding

    private val mainStorage by lazy { MMKV.mmkvWithID(MmkvManager.ID_MAIN, MMKV.MULTI_PROCESS_MODE) }
    private val serverRawStorage by lazy { MMKV.mmkvWithID(MmkvManager.ID_SERVER_RAW, MMKV.MULTI_PROCESS_MODE) }
    private val editGuid by lazy { intent.getStringExtra("guid").orEmpty() }
    private val isRunning by lazy {
        intent.getBooleanExtra("isRunning", false)
                && editGuid.isNotEmpty()
                && editGuid == mainStorage?.decodeString(MmkvManager.KEY_SELECTED_SERVER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServerCustomConfigBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val toolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!Utils.getDarkModeStatus(this)) {
            binding.editor.colorScheme = EditorTheme.INTELLIJ_LIGHT
        }
        binding.editor.language = JsonLanguage()
        val config = MmkvManager.decodeServerConfig(editGuid)
        if (config != null) {
            bindingServer(config)
        } else {
            clearServer()
        }
    }

    /**
     * bingding seleced server config
     */
    private fun bindingServer(config: ServerConfig): Boolean {
        binding.etRemarks.text = Utils.getEditable(config.remarks)
        val raw = serverRawStorage?.decodeString(editGuid)
        if (raw.isNullOrBlank()) {
            binding.editor.setTextContent(Utils.getEditable(config.fullConfig?.toPrettyPrinting().orEmpty()))
        } else {
            binding.editor.setTextContent(Utils.getEditable(raw))
        }
        return true
    }

    /**
     * clear or init server config
     */
    private fun clearServer(): Boolean {
        binding.etRemarks.text = null
        return true
    }

    /**
     * save server config
     */
    private fun saveServer(): Boolean {
        if (TextUtils.isEmpty(binding.etRemarks.text.toString())) {
            toast(R.string.server_lab_remarks)
            return false
        }

        val v2rayConfig = try {
            Gson().fromJson(binding.editor.text.toString(), V2rayConfig::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            ToastCompat.makeText(this, "${getString(R.string.toast_malformed_josn)} ${e.cause?.message}", Toast.LENGTH_LONG).show()
            return false
        }

        val config = MmkvManager.decodeServerConfig(editGuid) ?: ServerConfig.create(EConfigType.CUSTOM)
        config.remarks = v2rayConfig.remarks ?: binding.etRemarks.text.toString().trim()
        config.fullConfig = v2rayConfig

        MmkvManager.encodeServerConfig(editGuid, config)
        serverRawStorage?.encode(editGuid, binding.editor.text.toString())
        toast(R.string.toast_success)
        finish()
        return true
    }

    /**
     * save server config
     */
    private fun deleteServer(): Boolean {
        if (editGuid.isNotEmpty()) {
            MaterialAlertDialogBuilder(this).setMessage(R.string.del_config_comfirm)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        MmkvManager.removeServer(editGuid)
                        finish()
                    }
                    .setNegativeButton(android.R.string.no) {_, _ ->
                        // do nothing
                    }
                    .show()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_server, menu)
        val delButton = menu.findItem(R.id.del_config)
        val saveButton = menu.findItem(R.id.save_config)

        if (editGuid.isNotEmpty()) {
            if (isRunning) {
                delButton?.isVisible = false
                saveButton?.isVisible = false
            }
        } else {
            delButton?.isVisible = false
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.del_config -> {
            deleteServer()
            true
        }
        R.id.save_config -> {
            saveServer()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
