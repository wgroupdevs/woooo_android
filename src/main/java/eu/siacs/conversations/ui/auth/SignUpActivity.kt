package eu.siacs.conversations.ui.auth

import android.app.AlertDialog
import android.content.Intent
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
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.ActivitySignUpBinding
import eu.siacs.conversations.http.model.SignUpModel
import eu.siacs.conversations.http.model.SignUpRequestModel
import eu.siacs.conversations.http.services.BaseModelAPIResponse
import eu.siacs.conversations.http.services.WooAPIService
import eu.siacs.conversations.ui.EditAccountActivity
import eu.siacs.conversations.ui.WalletMainFragment
import eu.siacs.conversations.ui.util.PrDialog
import eu.siacs.conversations.ui.util.isValidEmail
import eu.siacs.conversations.ui.wallet.WalletViewModel


@AndroidEntryPoint
class SignUpActivity : AppCompatActivity(), WooAPIService.OnSignUpAPiResult {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var codePicker: CountryCodePicker
    private val TAG = "SignUpActivity_TAG"
    private var walletAddress = ""

    lateinit var walletViewModel: WalletViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        codePicker = binding.countryCodePicker
        passwordTextWatcher()
        binding.backArrow.setOnClickListener {
            finish()
        }
        binding.signUpBtn.setOnClickListener {
            if (walletAddress.isBlank()) {
                connectWallet()
                return@setOnClickListener
            }
            validateSignUpForm()
        }

        walletViewModel = ViewModelProvider(this)[WalletViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        if (walletAddress.isBlank()) {
            connectWallet()
        }
    }

    private fun connectWallet() {
        if (walletViewModel.isWalletConnected) {
            walletAddress = walletViewModel.ethereumState.value?.selectedAddress.toString()
            return
        }
        walletViewModel.showWalletNotConnectedDialog(this, title = "Configure Wallet", onSuccess = {
            walletAddress = it
        }, onError = {
            Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show()
        })
    }

    private fun passwordTextWatcher() {
        binding.passwordEt.addTextChangedListener(object : TextWatcher {
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


    private fun clearSignUpForm() {
        binding.firstNameEt.text?.clear()
        binding.lastNameEt.text?.clear()
        binding.emailEt.text?.clear()
        binding.phoneNumberField.text?.clear()
        binding.passwordEt.text?.clear()
        binding.confirmPasswordEt.text?.clear()
        binding.referralCodeEt.text?.clear()
    }

    private fun validateSignUpForm() {

        val fName = binding.firstNameEt.text?.trim().toString()
        val lName = binding.lastNameEt.text?.trim().toString()
        val email = binding.emailEt.text?.trim().toString()
        var phoneNumber = binding.phoneNumberField.text?.trim().toString()
        val countryCode = binding.countryCodePicker.selectedCountryCodeWithPlus
        val password = binding.passwordEt.text?.trim().toString()
        val confirmPassword = binding.confirmPasswordEt.text?.trim().toString()
        val referralCode = binding.referralCodeEt.text?.trim().toString()

        if (fName.isBlank()) {
            Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show()
            return
        } else if (lName.isBlank()) {
            Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show()
            return

        } else if (email.isBlank()) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
            return

        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_LONG).show()
            return
        } else if (phoneNumber.isBlank()) {
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show()
            return
        } else if (phoneNumber.length < 8) {
            Toast.makeText(this, "invalid phone number format", Toast.LENGTH_SHORT).show()
            return
        } else if (password.isBlank()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            return

        } else if (confirmPassword.isBlank()) {
            Toast.makeText(this, "Please re-enter password", Toast.LENGTH_SHORT).show()
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

        phoneNumber = countryCode.plus(phoneNumber)


        val userSignUpRequest = SignUpRequestModel(
            firstName = fName,
            lastName = lName,
            email = email,
            phoneNumber = phoneNumber,
            password = password,
            userReferralCode = referralCode,
            walletAddress = walletAddress
        )

        val wooAuthService = WooAPIService.getInstance()

        PrDialog.show(this)

        wooAuthService.signUp(userSignUpRequest, this@SignUpActivity)
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
                clearSignUpForm()
                finish()

                val otpIntent = Intent(this@SignUpActivity, OTPVerificationActivity::class.java)
                otpIntent.putExtra(OTPVerificationActivity.EMAIL, email)

                startActivity(otpIntent)

                //goto verify OTP page
            } else {
                alertDialog.cancel()
            }
        }
        alertDialog.show()
    }


    override fun <T : Any?> onSignUpResultFound(result: T) {

        runOnUiThread {
            PrDialog.hide()
            when (result) {
                is SignUpModel -> {
                    showAlertDialog(
                        isSuccess = true,
                        "Register successfully",
                        "An OTP is sent to your email please verify."
                    )
                }

                is BaseModelAPIResponse -> {
                    val message = (result as BaseModelAPIResponse).Message
                    showAlertDialog(
                        isSuccess = false,
                        "Registration failed",
                        message
                    )
                    Log.d(
                        TAG,
                        "onSignUpResultFound ERROR : " + (result as BaseModelAPIResponse).Message
                    )
                }

                else -> {
                    PrDialog.hide()
                    Log.d(EditAccountActivity.TAG, "ECEPTION FOUND... $result")
                }
            }
        }
    }


}















