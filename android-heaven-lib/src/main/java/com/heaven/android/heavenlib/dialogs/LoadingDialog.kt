package com.heaven.android.heavenlib.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.heaven.android.heavenlib.R
import com.heaven.android.heavenlib.base.dialog.BaseDialogFragment
import com.heaven.android.heavenlib.databinding.DialogLoadingBinding

class LoadingDialog : BaseDialogFragment<DialogLoadingBinding>() {
    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogLoadingBinding {
        return DialogLoadingBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()
    }
}
