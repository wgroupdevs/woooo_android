package eu.siacs.conversations.ui.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.ActivityForgotPasswordBinding
import eu.siacs.conversations.http.model.requestmodels.EmailRequestModel
import eu.siacs.conversations.http.services.BaseModelAPIResponse
import eu.siacs.conversations.http.services.WooAPIService
import eu.siacs.conversations.ui.EditAccountActivity
import eu.siacs.conversations.ui.util.PrDialog
import eu.siacs.conversations.ui.util.isValidEmail

class ForgotPasswordActivity : AppCompatActivity(), WooAPIService.OnForgotPasswordAPiResult {

    private lateinit var binding: ActivityForgotPasswordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.fpRecoverBtn.setOnClickListener {
            recoverPassword()
        }
    }


    private fun recoverPassword() {

        val email = binding.emailEt.text?.trim().toString()

        if (email.isBlank()) {
            Toast.makeText(this, "Please enter email to continue", Toast.LENGTH_LONG).show()
            return
        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_LONG).show()
            return
        }

        val wooService = WooAPIService.getInstance()

        PrDialog.show(this)
        wooService.forgotPassword(EmailRequestModel(email), this)
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

                val email = binding.emailEt.text?.trim().toString()
                alertDialog.cancel()
                binding.emailEt.text?.clear()
                finish()
                val otpIntent =
                    Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
                otpIntent.putExtra(ResetPasswordActivity.EMAIL, email)

                startActivity(otpIntent)

                //goto verify OTP page
            } else {
                alertDialog.cancel()
            }
        }
        alertDialog.show()
    }


    override fun <T : Any?> onForgotPasswordResultFound(result: T) {
        runOnUiThread {
            PrDialog.hide()
            when (result) {
                is BaseModelAPIResponse -> {
                    if (result.Success) {
                        showAlertDialog(true, "OTP sent", result.Message ?: "")
                    } else {
                        showAlertDialog(false, "Forgot password", result.Message ?: "")
                    }
                }

                else -> {
                    PrDialog.hide()
                    Log.d(EditAccountActivity.TAG, "ECEPTION FOUND... $result")
                }
            }
        }
    }

}