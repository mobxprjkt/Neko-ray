package com.neko.v2ray.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.appcompat.widget.Toolbar
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.multiprocess.RemoteWorkManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.neko.v2ray.AngApplication
import com.neko.v2ray.R
import com.neko.v2ray.databinding.ActivitySubEditBinding
import com.neko.v2ray.dto.SubscriptionItem
import com.neko.v2ray.extension.toast
import com.neko.v2ray.service.SubscriptionUpdater
import com.neko.v2ray.util.MmkvManager
import com.neko.v2ray.util.SoftInputAssist
import com.neko.v2ray.util.Utils
import java.util.concurrent.TimeUnit

import com.neko.imageslider.ImageSlider
import com.neko.imageslider.constants.ActionTypes
import com.neko.imageslider.constants.AnimationTypes
import com.neko.imageslider.constants.ScaleTypes
import com.neko.imageslider.interfaces.ItemChangeListener
import com.neko.imageslider.interfaces.ItemClickListener
import com.neko.imageslider.interfaces.TouchListener
import com.neko.imageslider.models.SlideModel

class SubEditActivity : BaseActivity() {
    private lateinit var binding: ActivitySubEditBinding
    private lateinit var softInputAssist: SoftInputAssist

    var del_config: MenuItem? = null
    var save_config: MenuItem? = null

    private val subStorage by lazy { MMKV.mmkvWithID(MmkvManager.ID_SUB, MMKV.MULTI_PROCESS_MODE) }
    private val editSubId by lazy { intent.getStringExtra("subId").orEmpty() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val toolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val json = subStorage?.decodeString(editSubId)
        if (!json.isNullOrBlank()) {
            bindingServer(Gson().fromJson(json, SubscriptionItem::class.java))
        } else {
            clearServer()
        }
        softInputAssist = SoftInputAssist(this)
        
        val imageSlider = findViewById<ImageSlider>(R.id.image_slider) // init imageSlider
        val imageList = ArrayList<SlideModel>() // Create image list
        imageList.add(SlideModel("https://telegra.ph/file/ab718d248f3a0682a549d.png", ""))
        imageList.add(SlideModel("https://telegra.ph/file/9fa70f1e6d880e5a52319.jpg", ""))
        imageList.add(SlideModel("https://telegra.ph/file/c714839ece8899d1a2cfb.jpg", ""))
        imageList.add(SlideModel("https://telegra.ph/file/e0e87781ff6b21c876b8e.jpg", ""))
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
        imageSlider.setSlideAnimation(AnimationTypes.ZOOM_OUT)
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                // You can listen here.
                println("normal")
            }
            override fun doubleClick(position: Int) {
                // Do not use onItemSelected if you are using a double click listener at the same time.
                // Its just added for specific cases.
                // Listen for clicks under 250 milliseconds.
                println("its double")
            }
        })
        imageSlider.setItemChangeListener(object : ItemChangeListener {
            override fun onItemChanged(position: Int) {
                //println("Pos: " + position)
            }
        })
        imageSlider.setTouchListener(object : TouchListener {
            override fun onTouched(touched: ActionTypes, position: Int) {
                if (touched == ActionTypes.DOWN){
                    imageSlider.stopSliding()
                } else if (touched == ActionTypes.UP ) {
                    imageSlider.startSliding(1000)
                }
            }
        })
    }

    /**
     * bingding seleced server config
     */
    private fun bindingServer(subItem: SubscriptionItem): Boolean {
        binding.etRemarks.text = Utils.getEditable(subItem.remarks)
        binding.etUrl.text = Utils.getEditable(subItem.url)
        binding.chkEnable.isChecked = subItem.enabled
        binding.autoUpdateCheck.isChecked = subItem.autoUpdate
        return true
    }

    /**
     * clear or init server config
     */
    private fun clearServer(): Boolean {
        binding.etRemarks.text = null
        binding.etUrl.text = null
        binding.chkEnable.isChecked = true
        return true
    }

    /**
     * save server config
     */
    private fun saveServer(): Boolean {
        val subItem: SubscriptionItem
        val json = subStorage?.decodeString(editSubId)
        var subId = editSubId
        if (!json.isNullOrBlank()) {
            subItem = Gson().fromJson(json, SubscriptionItem::class.java)
        } else {
            subId = Utils.getUuid()
            subItem = SubscriptionItem()
        }

        subItem.remarks = binding.etRemarks.text.toString()
        subItem.url = binding.etUrl.text.toString()
        subItem.enabled = binding.chkEnable.isChecked
        subItem.autoUpdate = binding.autoUpdateCheck.isChecked

        if (TextUtils.isEmpty(subItem.remarks)) {
            toast(R.string.sub_setting_remarks)
            return false
        }
//        if (TextUtils.isEmpty(subItem.url)) {
//            toast(R.string.sub_setting_url)
//            return false
//        }

        subStorage?.encode(subId, Gson().toJson(subItem))
        toast(R.string.toast_success)
        finish()
        return true
    }

    /**
     * save server config
     */
    private fun deleteServer(): Boolean {
        if (editSubId.isNotEmpty()) {
            MaterialAlertDialogBuilder(this).setMessage(R.string.del_config_comfirm)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        MmkvManager.removeSubscription(editSubId)
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
        del_config = menu.findItem(R.id.del_config)
        save_config = menu.findItem(R.id.save_config)

        if (editSubId.isEmpty()) {
            del_config?.isVisible = false
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

    override fun onResume() {
        softInputAssist.onResume()
        super.onResume()
    }

    override fun onPause() {
        softInputAssist.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        softInputAssist.onDestroy()
        super.onDestroy()
    }
}
