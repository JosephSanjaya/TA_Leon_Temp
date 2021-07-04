package com.leon.su.utils

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.ToastUtils
import com.leon.su.R

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
