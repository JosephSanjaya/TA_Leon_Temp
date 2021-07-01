/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.leon.su.R

fun Context.makeLoadingDialog(
    isCancelable: Boolean,
    dismissListener: DialogInterface.OnDismissListener? = null
) =
    MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
        .setView(R.layout.dialog_loading)
        .setOnDismissListener(dismissListener)
        .setBackground(ColorDrawable(Color.TRANSPARENT))
        .setCancelable(isCancelable)
        .create().apply {
            window?.setWindowAnimations(R.style.MaterialFadeAnimation)
        }
