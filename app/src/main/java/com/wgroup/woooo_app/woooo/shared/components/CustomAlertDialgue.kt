package com.wgroup.woooo_app.woooo.shared.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wgroup.woooo_app.woooo.theme.WooColor
//
//@Composable
//fun ButtonClick(
//    buttonText: String,
//    onButtonClick: () -> Unit
//) {
//    Button(
//        shape = RoundedCornerShape(5.dp),
////        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
//        onClick = {
//            onButtonClick()
//        }) {
//        Text(
//            text = buttonText,
//            fontSize = 16.sp,
//            color = Color.White
//        )
//    }
//}
//
//// For Roboto font: https://fonts.google.com/specimen/Roboto?query=roboto
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CustomAlertDialogue(
//    cornerRadius: Dp = 12.dp,
//    deleteButtonColor: Color = Color(0xFFFF0000),
//    cancelButtonColor: Color = Color(0xFF35898F),
////    titleTextStyle: TextStyle = TextStyle(
////        color = Color.Black.copy(alpha = 0.87f),
////        fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
////        fontSize = 20.sp
////    ),
////    messageTextStyle: TextStyle = TextStyle(
////        color = Color.Black.copy(alpha = 0.95f),
////        fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
////        fontSize = 16.sp,
////        lineHeight = 22.sp
////    ),
////    buttonTextStyle: TextStyle = TextStyle(
////        fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
////        fontSize = 16.sp
////    ),
//    onDismiss: () -> Unit,
//) {
//
//    val context = LocalContext.current.applicationContext
//
//    // This helps to disable the ripple effect
//    val interactionSource = remember {
//        MutableInteractionSource()
//    }
//
//    val buttonCorner = 6.dp
//
//    AlertDialog(
//        onDismissRequest = {
//            onDismiss()
//        },modifier = Modifier
//            .clip(RoundedCornerShape(10.dp))
//            .background(WooColor.textBox)
//    )
//    {
//        Column(modifier = Modifier.padding(all = 16.dp)) {
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(
//                    space = 6.dp,
//                    alignment = Alignment.Start
//                )
//            ) {
//
//                // For icon, visit feathericons.com
//                // Icon name: trash-2
//                Icon(
//                    imageVector = Icons.Default.Delete,modifier = Modifier.size(26.dp),
//                    contentDescription = "Delete Icon",
//                    tint = deleteButtonColor
//                )
//
//                Text(
//                    text = "Delete Item?",
////                        style = titleTextStyle
//                )
//
//            }
//
//            Text(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp,bottom = 20.dp),
//                text = "Are you sure you want to delete this item from the list?",
////                    style = messageTextStyle
//            )
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(
//                    space = 10.dp,
//                    alignment = Alignment.End
//                )
//            ) {
//
//                // Cancel button
//                Box(
//                    modifier = Modifier
//                        .clickable(
//                            // This is to disable the ripple effect
//                            indication = null,
//                            interactionSource = interactionSource
//                        ) {
//                            Toast
//                                .makeText(context,"Cancel",Toast.LENGTH_SHORT)
//                                .show()
//                            onDismiss()
//                        }
//                        .border(
//                            width = 1.dp,
//                            color = cancelButtonColor,
//                            shape = RoundedCornerShape(buttonCorner)
//                        )
//                        .padding(top = 6.dp,bottom = 8.dp,start = 24.dp,end = 24.dp),
//                ) {
//                    Text(
//                        text = "Cancel",
////                            style = buttonTextStyle,
//                        color = cancelButtonColor
//                    )
//                }
//
//                // Delete button
//                Box(
//                    modifier = Modifier
//                        .clickable(
//                            // This is to disable the ripple effect
//                            indication = null,
//                            interactionSource = interactionSource
//                        ) {
//                            Toast
//                                .makeText(context,"Delete",Toast.LENGTH_SHORT)
//                                .show()
//                            onDismiss()
//                        }
//                        .background(
//                            color = deleteButtonColor,
//                            shape = RoundedCornerShape(buttonCorner)
//                        )
//                        .padding(top = 6.dp,bottom = 8.dp,start = 24.dp,end = 24.dp),
//                ) {
//                    Text(
//                        text = "Delete",
////                            style = buttonTextStyle,
//                        color = Color.White
//                    )
//                }
//
//            }
//        }
//    }
//}
//}