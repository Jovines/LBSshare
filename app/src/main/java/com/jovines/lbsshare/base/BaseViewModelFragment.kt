package com.jovines.lbsshare.base

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jovines.lbsshare.base.viewmodel.BaseViewModel
import com.jovines.lbsshare.base.viewmodel.event.ProgressDialogEvent
import org.jetbrains.anko.support.v4.indeterminateProgressDialog

/**
 * Created By jay68 on 2018/8/23.
 */
abstract class BaseViewModelFragment<T : BaseViewModel> : BaseFragment() {
    protected lateinit var viewModel: T

    protected abstract val viewModelClass: Class<T>

    private var progressDialog: ProgressDialog? = null

    private fun initProgressBar() = indeterminateProgressDialog(message = "Loading...") {
        setOnDismissListener { viewModel.onProgressDialogDismissed() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = getViewModelFactory()
        viewModel = if (viewModelFactory != null) {
            ViewModelProvider(this, viewModelFactory).get(viewModelClass)
        } else {
            ViewModelProvider(this).get(viewModelClass)
        }
        viewModel.apply {
            toastEvent.observe { str -> str?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() } }
            longToastEvent.observe { str -> str?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() } }
            progressDialogEvent.observe {
                it ?: return@observe
                //确保只有一个对话框会被弹出
                if (it != ProgressDialogEvent.DISMISS_DIALOG_EVENT && progressDialog?.isShowing != true) {
                    progressDialog = progressDialog ?: initProgressBar()
                    progressDialog?.show()
                } else if (it == ProgressDialogEvent.DISMISS_DIALOG_EVENT && progressDialog?.isShowing != false) {
                    progressDialog?.dismiss()
                }
            }
        }
    }

    protected open fun getViewModelFactory(): ViewModelProvider.Factory? = null

    inline fun <T> LiveData<T>.observe(crossinline onChange: (T?) -> Unit) = observe(this@BaseViewModelFragment, Observer { onChange(value) })
    inline fun <T> LiveData<T>.observeNotNull(crossinline onChange: (T) -> Unit) = observe(this@BaseViewModelFragment, Observer {
        it ?: return@Observer
        onChange(it)
    })

    override fun onDestroyView() {
        super.onDestroyView()
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }
}