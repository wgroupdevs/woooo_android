package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(value: String, setShowDialog: (Boolean) -> Unit, setValue: (String) -> Unit) {

    val txtFieldError = remember { mutableStateOf("") }
    val txtField = remember { mutableStateOf(value) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = WooColor.loaderColorBackGround,
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Filled.Cancel,
                            contentDescription = "",
                            tint = WooColor.white,
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) })
                    }
                    VerticalSpacer(Dimension.dimen_10)
                    Text(
                        text = "iniasncsdjc \n asdkjncsd",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

//
//                    TextField(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .border(
//                                BorderStroke(
//                                    width = 2.dp,
//                                    color = colorResource(id = if (txtFieldError.value.isEmpty()) android.R.color.holo_green_light else android.R.color.holo_red_dark)
//                                ),
//                                shape = RoundedCornerShape(50)
//                            ),
//                        colors = TextFieldDefaults.textFieldColors(
//                            focusedIndicatorColor = Color.Transparent,
//                            unfocusedIndicatorColor = Color.Transparent
//                        ),
//                        leadingIcon = {
//                            Icon(
//                                imageVector = Icons.Filled.Money,
//                                contentDescription = "",
//                                tint = colorResource(android.R.color.holo_green_light),
//                                modifier = Modifier
//                                    .width(20.dp)
//                                    .height(20.dp)
//                            )
//                        },
//                        placeholder = { Text(text = "Enter value") },
//                        value = txtField.value,
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        onValueChange = {
//                            txtField.value = it.take(10)
//                        })

                    VerticalSpacer(Dimension.dimen_20)
                    Row(
                        horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
                    ) {

                        TextButton(onClick = {
                            setShowDialog(false)
                        }) {
                            Text(text = "Cancel")
                        }
                        HorizontalSpacer()
                        TextButton(onClick = {
                            setShowDialog(false)
                        }) {
                            Text(text = "Ok")
                        }
                    }

//                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
//
//                        Button(
//                            onClick = {
//                                if (txtField.value.isEmpty()) {
//                                    txtFieldError.value = "Field can not be empty"
//                                    return@Button
//                                }
//                                setValue(txtField.value)
//                                setShowDialog(false)
//                            },
//                            shape = RoundedCornerShape(50.dp),
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(50.dp)
//                        ) {
//                            Text(text = "Done")
//                        }
//                    }
                }
            }
        }
    }
}