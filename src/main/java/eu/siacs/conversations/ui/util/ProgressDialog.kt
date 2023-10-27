package eu.siacs.conversations.ui.util

import android.app.ProgressDialog
import android.content.Context

class PrDialog {
    companion object {
        private var progressDialog: ProgressDialog? = null
        fun show(context: Context?) {
            progressDialog = ProgressDialog(context)
            progressDialog?.setMessage("Please wait")
            progressDialog?.setCancelable(false)
            progressDialog?.show()
        }

        fun hide() {
            if (progressDialog != null && progressDialog?.isShowing == true) {
                progressDialog?.dismiss()
            }
        }
    }
}