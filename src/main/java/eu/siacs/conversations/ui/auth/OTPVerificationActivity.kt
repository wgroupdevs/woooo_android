package eu.siacs.conversations.ui.auth

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.ActivityOtpverificationBinding
import eu.siacs.conversations.http.model.requestmodels.EmailRequestModel
import eu.siacs.conversations.http.services.BaseModelAPIResponse
import eu.siacs.conversations.http.services.WooAPIService
import eu.siacs.conversations.ui.EditAccountActivity
import eu.siacs.conversations.ui.util.PrDialog

class OTPVerificationActivity : AppCompatActivity(), WooAPIService.OnConfirmAccountAPiResult,
    WooAPIService.OnResendOTPAPiResult {


    private lateinit var binding: ActivityOtpverificationBinding
    private lateinit var wooService: WooAPIService
    private var email: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpverificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        wooService = WooAPIService.getInstance()
        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.verifyBtn.setOnClickListener {
            confirmAccount()
        }
        binding.resendOtp.setOnClickListener {
            resendOTP()
        }
    }

    override fun onStart() {
        super.onStart()
        intent?.let {
            email = it.getStringExtra(ResetPasswordActivity.EMAIL) ?: ""
        }
    }

    private fun confirmAccount() {
        val code = binding.otpEt.text?.trim().toString()
        if (code.isBlank()) {
            Toast.makeText(this, "Please enter OTP", Toast.LENGTH_LONG).show()
            return
        } else if (code.length < 6) {
            Toast.makeText(this, "Invalid OTP (OTP must be 6 digits)", Toast.LENGTH_LONG).show()
            return
        }
        val params = mapOf("email" to email, "code" to code)
        Log.d(TAG, "PARAMS : $params")
        PrDialog.show(this)
        wooService.confirmAccount(params, this@OTPVerificationActivity)
    }


    private fun resendOTP() {
        PrDialog.show(this)
        wooService.resendOTP(
            false,
            EmailRequestModel(email = email),
            this@OTPVerificationActivity
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
        val customView: View = inflater.inflate(R.layout.title_des_ok_dialog, null)
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
                //goto verify OTP page
            } else {
                alertDialog.cancel()
            }
        }
        alertDialog.show()
    }


    override fun <T : Any?> onConfirmAccountResultFound(result: T) {
        runOnUiThread {
            PrDialog.hide()
            when (result) {
                is BaseModelAPIResponse -> {
                    if (result.Success) {
                        showAlertDialog(true, "Account Confirmed", result.Message ?: "")
                    } else {
                        showAlertDialog(false, "Confirmation Failed", result.Message ?: "")
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