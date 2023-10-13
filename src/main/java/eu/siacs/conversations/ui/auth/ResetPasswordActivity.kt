package eu.siacs.conversations.ui.auth

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.ActivityResetPasswordBinding
import eu.siacs.conversations.http.model.requestmodels.EmailRequestModel
import eu.siacs.conversations.http.model.requestmodels.ResetPasswordRequestModel
import eu.siacs.conversations.http.services.BaseModelAPIResponse
import eu.siacs.conversations.http.services.WooAPIService
import eu.siacs.conversations.ui.EditAccountActivity
import eu.siacs.conversations.ui.util.PrDialog


class ResetPasswordActivity : AppCompatActivity(), WooAPIService.OnResetPasswordAPiResult,
    WooAPIService.OnResendOTPAPiResult {
    private var email: String? = null
    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var wooService: WooAPIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        wooService = WooAPIService.getInstance()
        binding.backArrow.setOnClickListener {
            finish()
        }
        binding.resetBtn.setOnClickListener {
            resetPassword()
        }
        binding.resendOtp.setOnClickListener { resendOTP() }
        passwordTextWatcher()

    }

    override fun onStart() {
        super.onStart()
        intent?.let {
            email = it.getStringExtra(EMAIL)
        }
    }

    private fun resendOTP() {
        PrDialog.show(this)
        wooService.resendOTP(
            false,
            EmailRequestModel(email = email ?: ""),
            this@ResetPasswordActivity
        )

    }


    private fun passwordTextWatcher() {
        binding.newPasswordEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                validatePassword(s.toString())
            }
        })
    }

    private fun validatePassword(password: String): Boolean {
        val eightChar = password.length >= 8
        val upperCase = Regex("[A-Z]").containsMatchIn(password)
        val lowerCase = Regex("[a-z]").containsMatchIn(password)
        val oneNumber = Regex("[0-9]").containsMatchIn(password)
        val specialChar = Regex("^(.*?[$&+,:;/=?@#|'<>.^*()_%!-])").containsMatchIn(password)
        if (eightChar && upperCase && lowerCase && oneNumber && specialChar) {
            binding.passwordLayout.error = null
            return true
        } else {
            binding.passwordLayout.error = "Password hints\n" +
                    "1-Minimum 8 characters\n" +
                    "2-Upper case letter (A-Z)\n" +
                    "3-Lower case letter (a-z)\n" +
                    "4-Number (0-9)\n" +
                    "5-Special character (@,%,?)"
        }
        return false
    }

    private fun resetPassword() {

        val otp = binding.otpEt.text?.trim().toString()
        val password = binding.newPasswordEt.text?.trim().toString()
        val confirmPassword = binding.confirmPasswordEt.text?.trim().toString()

        if (otp.isBlank()) {
            Toast.makeText(this, "Enter OTP", Toast.LENGTH_LONG).show()
            return
        } else if (password.isBlank()) {
            Toast.makeText(this, "Enter new password", Toast.LENGTH_LONG).show()
            return
        } else if (confirmPassword.isBlank()) {
            Toast.makeText(this, "ReEnter new password", Toast.LENGTH_LONG).show()
            return
        } else if (!validatePassword(password)) {
            Toast.makeText(
                this,
                "Your entered password doesn't meet the necessary conditions.",
                Toast.LENGTH_SHORT
            ).show()
            return
        } else if (!password.contentEquals(confirmPassword)) {
            Toast.makeText(
                this,
                "The password and the confirmation password don't match.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        PrDialog.show(this)
        wooService.resetPassword(
            ResetPasswordRequestModel(
                confirmPassword = confirmPassword,
                email = email ?: "",
                otp = otp,
                password = password
            ), this
        )


    }

    companion object {
        const val EMAIL = "email"
        const val TAG = "ResetPassword_TAG"


    }


    private fun showAlertDialog(isSuccess: Boolean, title: String, description: String) {
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.popup_dialog_theme)
        // Inflate the custom layout
        val inflater = LayoutInflater.from(this)
        val customView: View = inflater.inflate(R.layout.signup_dialog, null)
        alertDialogBuilder.setView(customView)
        // Create and show the AlertDialog

        val okButton = customView.findViewById<Button>(R.id.okay_btn)
        val titleTV = customView.findViewById<TextView>(R.id.signup_title)
        val desTV = customView.findViewById<TextView>(R.id.signup_description)

        titleTV.text = title
        desTV.text = description
        val alertDialog = alertDialogBuilder.create()

        okButton.setOnClickListener {
            if (isSuccess) {
                alertDialog.cancel()
                finish()
            } else {
                alertDialog.cancel()
            }
        }
        alertDialog.show()
    }


    override fun <T : Any?> onResetPasswordResultFound(result: T) {
        runOnUiThread {
            PrDialog.hide()
            when (result) {
                is BaseModelAPIResponse -> {
                    if (result.Success) {
                        showAlertDialog(true, "Password Reset", result.Message ?: "")
                    } else {
                        showAlertDialog(false, "Failed", result.Message ?: "")
                    }
                }

                else -> {
                    PrDialog.hide()
                    Log.d(EditAccountActivity.TAG, "ECEPTION FOUND... $result")
                }
            }
        }
    }

    override fun <T : Any?> onResendOTPResultFound(result: T) {
        runOnUiThread {
            PrDialog.hide()
            when (result) {
                is BaseModelAPIResponse -> {
                    showAlertDialog(false, "Resend OTP", result.Message ?: "")
                }

                else -> {
                    PrDialog.hide()
                    Log.d(EditAccountActivity.TAG, "ECEPTION FOUND... $result")
                }
            }
        }
    }

}