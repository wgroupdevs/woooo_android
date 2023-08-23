package com.wgroup.woooo_app.woooo.feature.wallet.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.feature.home.viewmodel.CircularMenuViewModel
import woooo_app.woooo.shared.components.CustomButton
import woooo_app.woooo.shared.components.CustomIcon
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.theme.WooColor
import woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings
import woooo_app.woooo.destinations.WalletMainScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Wallet_Pin_Verify_Dialog(
    navigator: DestinationsNavigator,
    onDismiss: () -> Unit,
    viewModel: ViewModel,
    onBackClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .alpha(0.8f)
            .background(WooColor.textBox)
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.clickable(onClick = onBackClick),
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "",
                    tint = WooColor.white
                )
                HorizontalSpacer()
                Text(text = Strings.acsWalt)
            }
            VerticalSpacer(Dimension.dimen_10)
            BoxWithConstraints(
                Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AccountBalanceWallet,
                    contentDescription = "",
                    modifier = Modifier.size(100.dp),
                    tint = WooColor.white
                )
                Box(
                    Modifier
                        .padding(10.dp)
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(WooColor.textFieldBackGround)
                        .align(Alignment.BottomStart)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "",
                        tint = WooColor.white,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)

                    )
                }

            }
            VerticalSpacer()

            //PinPut

            OtpFields()

            VerticalSpacer()

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = Strings.lgnWthFingre,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 13.sp
                )
                CustomIcon(icon = Icons.Rounded.Fingerprint,modifier = Modifier.size(24.dp))
            }
            VerticalSpacer(Dimension.dimen_20)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = Strings.frgtText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 13.sp
                )
                CustomIcon(icon = Icons.Rounded.HelpOutline,modifier = Modifier.size(24.dp))
            }
            Box(
                modifier = Modifier.fillMaxHeight(),contentAlignment = Alignment.BottomEnd
            ) {
                CustomButton(
                    border = BorderStroke(1.dp,Color.White),
                    onClick = {
                        navigator.navigate(
                            WalletMainScreenDestination
                        )
                        when (viewModel) {
                            is CircularMenuViewModel -> {
                                viewModel.setOpenVerifyDialogValue(false)
                            }
                        }
                    },
                    content = {
                        Text(
                            text = Strings.vrfyText,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                    },
                )
            }

        }

    }
}

@Composable
fun OtpFields() {
    val focusManager = LocalFocusManager.current

    val (getOne,setOne) = remember {
        mutableStateOf("")
    }
    val (getTwo,setTwo) = remember {
        mutableStateOf("")
    }
    val (getThree,setThree) = remember {
        mutableStateOf("")
    }

    val (getFour,setFour) = remember {
        mutableStateOf("")
    }
    val (getFive,setFive) = remember {
        mutableStateOf("")
    }
    val (getSix,setSix) = remember {
        mutableStateOf("")
    }
    LaunchedEffect(
        key1 = getOne,
    ) {
        if (getOne.isNotEmpty()) {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Next,
            )
        }
    }
    LaunchedEffect(
        key1 = getTwo,
    ) {
        if (getTwo.isNotEmpty()) {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Next,
            )
        }
    }
    LaunchedEffect(
        key1 = getThree,
    ) {
        if (getThree.isNotEmpty()) {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Next,
            )
        }
    }
    LaunchedEffect(
        key1 = getFour,
    ) {
        if (getFour.isNotEmpty()) {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Next,
            )
        }
    }
    LaunchedEffect(
        key1 = getFive,
    ) {
        if (getFive.isNotEmpty()) {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Next,
            )
        }
    }
    LaunchedEffect(
        key1 = getSix,
    ) {
        if (getSix.isNotEmpty()) {
            focusManager.clearFocus()
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        OutlinedTextField(
            shape = RoundedCornerShape(
                topStart = 40.dp,bottomEnd = 0.dp,topEnd = 0.dp,bottomStart = 40.dp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = WooColor.textFieldBackGround,
                cursorColor = WooColor.primary,
                focusedBorderColor = WooColor.white,
                unfocusedBorderColor = WooColor.white,
            ),
            visualTransformation = PasswordVisualTransformation(),
            value = getOne,
            onValueChange = {
                if (it.length <= 1) {
                    setOne(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Next,
            ),
            modifier = Modifier.width(40.dp),
        )
        OutlinedTextField(
            shape = RoundedCornerShape(0.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = WooColor.textFieldBackGround,
                cursorColor = WooColor.primary,
                focusedBorderColor = WooColor.white,
                unfocusedBorderColor = WooColor.white,
            ),
            visualTransformation = PasswordVisualTransformation(),
            value = getTwo,
            onValueChange = {
                if (it.length <= 1) {
                    setTwo(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Next,
            ),
            modifier = Modifier.width(40.dp),
        )
        OutlinedTextField(
            shape = RoundedCornerShape(0.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = WooColor.textFieldBackGround,
                cursorColor = WooColor.primary,
                focusedBorderColor = WooColor.white,
                unfocusedBorderColor = WooColor.white,
            ),
            visualTransformation = PasswordVisualTransformation(),
            value = getThree,
            onValueChange = {
                if (it.length <= 1) {
                    setThree(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Next,
            ),
            modifier = Modifier.width(40.dp),
        )
        OutlinedTextField(
            shape = RoundedCornerShape(0.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = WooColor.textFieldBackGround,
                cursorColor = WooColor.primary,
                focusedBorderColor = WooColor.white,
                unfocusedBorderColor = WooColor.white,
            ),
            visualTransformation = PasswordVisualTransformation(),
            value = getFour,
            onValueChange = {
                if (it.length <= 1) {
                    setFour(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier.width(40.dp),
        )
        OutlinedTextField(
            shape = RoundedCornerShape(0.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = WooColor.textFieldBackGround,
                cursorColor = WooColor.primary,
                focusedBorderColor = WooColor.white,
                unfocusedBorderColor = WooColor.white,
            ),
            visualTransformation = PasswordVisualTransformation(),
            value = getFive,
            onValueChange = {
                if (it.length <= 1) {
                    setFive(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier.width(40.dp),
        )
        OutlinedTextField(
            shape = RoundedCornerShape(
                topStart = 0.dp,bottomEnd = 40.dp,topEnd = 40.dp,bottomStart = 0.dp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = WooColor.textFieldBackGround,
                cursorColor = WooColor.primary,
                focusedBorderColor = WooColor.white,
                unfocusedBorderColor = WooColor.white,
            ),
            visualTransformation = PasswordVisualTransformation(),
            value = getSix,
            onValueChange = {
                if (it.length <= 1) {
                    setSix(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier.width(40.dp),
        )
    }
}