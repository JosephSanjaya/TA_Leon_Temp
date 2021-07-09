package com.leon.su.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.net.Uri
import android.view.View
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.*
import com.gkemon.XMLtoPDF.PdfGenerator
import com.gkemon.XMLtoPDF.PdfGeneratorListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.leon.su.BuildConfig
import com.leon.su.R
import com.leon.su.data.users
import com.leon.su.domain.PDFType
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import java.io.File
import kotlinx.coroutines.tasks.await

inline val Fragment.appCompatActivity
    get() = if (activity is AppCompatActivity)
        activity as AppCompatActivity
    else
        null

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    isBackstack: Boolean = false,
    isAnimate: Boolean = false,
    isInclusive: Boolean = false,
    toolbarChange: ((ActionBar?) -> Unit)? = null
) {
    try {
        supportFragmentManager.replaceFragment(
            R.id.flFragments,
            fragment,
            isBackstack,
            isAnimate,
            isInclusive
        )
        toolbarChange?.invoke(supportActionBar)
    } catch (e: Throwable) {
        ToastUtils.showShort("Gagal navigasi.")
    }
}

fun FragmentManager.replaceFragment(
    placeholder: Int,
    fragment: Fragment,
    isBackstack: Boolean = false,
    isAnimate: Boolean = false,
    isInclusive: Boolean = false
) {
    beginTransaction().apply {
        if (isBackstack) addToBackStack(null)
        if (isAnimate) setCustomAnimations(
            android.R.anim.fade_in,
            android.R.anim.fade_out,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        if (isInclusive) popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        replace(placeholder, fragment)
    }.commit()
}

fun Context.createPDF(view: View, listener: PdfGeneratorListener) {
    PdfGenerator.getBuilder().setContext(this)
        .fromViewSource()
        .fromView(view)
        .setFileName(DateTime.now().format(DateFormat.DEFAULT_FORMAT))
        .setFolderName(PathUtils.getInternalAppDataPath())
        .openPDFafterGeneration(true)
        .build(listener)
}

suspend fun SharedPreferences.uploadPDF(
    context: Context,
    file: File,
    type: PDFType,
    action: () -> Unit
) {
    val upload = Firebase.storage.reference
        .child(DateTime.nowLocal().toString("dd-MM-yyyy"))
        .child(
            "${type.value} - ${users?.data?.nama ?: "Pegawai"} - ${
            DateTime.nowLocal().toString("HH:mm:ss")
            }.pdf"
        )
        .putFile(context.fileToUri(file))
    upload.await()
    action.invoke()
}

fun ToggleButton.setButtonStatus(status: Boolean) {
    backgroundTintList = ColorStateList.valueOf(
        ColorUtils.getColor(
            if (status) android.R.color.holo_green_light
            else android.R.color.holo_red_light
        )
    )
    isChecked = status
}

fun Context.fileToUri(file: File): Uri {
    return if (DeviceUtils.getSDKVersionCode() > 29) {
        FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".provider",
            file
        )
    } else {
        UriUtils.file2Uri(file)
    }
}
