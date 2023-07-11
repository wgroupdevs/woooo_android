package com.wgroup.woooo_app.woooo.feature.mining.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.shared.components.BarGraph
import com.wgroup.woooo_app.woooo.shared.components.BarType
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.TopBarForSetting
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import woooo_app.woooo.shared.components.ViewDivider
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun MiningMainView(
    navigator: DestinationsNavigator
) {
//    Values pass to graph
    val dataList = mutableListOf(8,2,3,4,10)
    val floatValue = mutableListOf<Float>()
    val datesList = mutableListOf("Chat","Login","Invite","Call","Meeting")
    dataList.forEachIndexed { index,value ->
        floatValue.add(index = index,element = value.toFloat() / dataList.max().toFloat())
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        // top  bar
        TopBarForSetting(onBackPressed = { navigator.popBackStack() })
        Column(modifier = Modifier.padding(10.dp)) {
            // upper Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = Strings.todays)
                    Text(text = "0.3 Woo")
                }
                Box {
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp),
                        progress = 0.7F,
                        color = WooColor.white,
                        trackColor = WooColor.dark,
                        strokeWidth = 5.dp
                    )
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "70%",
                        fontSize = 12.sp,
                        color = WooColor.circulInner
                    )

                }
            }
            VerticalSpacer(Dimension.dimen_5)
            ViewDivider()
            VerticalSpacer(Dimension.dimen_5)
            // Daily Reward
            Column {
                Text(text = Strings.dailyRewardText)
                VerticalSpacer()
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)

                ) {
// D1
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextForDays(Strings.D1)
                        VerticalSpacer(Dimension.dimen_5)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(WooColor.lightBlue)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center),
                                imageVector = Icons.Outlined.CardGiftcard,
                                contentDescription = "",
                                tint = WooColor.circulInner
                            )
                        }
                        VerticalSpacer(Dimension.dimen_5)
                        TextForCollect(Strings.collectText)

                    }
// D2
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextForDays(Strings.D2)
                        VerticalSpacer(Dimension.dimen_5)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(WooColor.lightBlue)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center),
                                imageVector = Icons.Outlined.CardGiftcard,
                                contentDescription = "",
                                tint = WooColor.circulInner
                            )
                        }
                        VerticalSpacer(Dimension.dimen_5)
                        TextForCollect(Strings.collectText)

                    }
// D3
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextForDays(Strings.D3)
                        VerticalSpacer(Dimension.dimen_5)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(WooColor.lightBlue)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center),
                                imageVector = Icons.Outlined.CardGiftcard,
                                contentDescription = "",
                                tint = WooColor.circulInner
                            )
                        }
                        VerticalSpacer(Dimension.dimen_5)
                        TextForCollect(Strings.collectText)

                    }
// D4
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextForDays(Strings.D4)
                        VerticalSpacer(Dimension.dimen_5)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(WooColor.lightBlue)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center),
                                imageVector = Icons.Outlined.CardGiftcard,
                                contentDescription = "",
                                tint = WooColor.circulInner
                            )
                        }
                        VerticalSpacer(Dimension.dimen_5)
                        TextForCollect(Strings.collectText)

                    }
// D5
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextForDays(Strings.D5)
                        VerticalSpacer(Dimension.dimen_5)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(WooColor.lightBlue)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center),
                                imageVector = Icons.Outlined.CardGiftcard,
                                contentDescription = "",
                                tint = WooColor.circulInner
                            )
                        }
                        VerticalSpacer(Dimension.dimen_5)
                        TextForCollect(Strings.collectText)

                    }
// D6
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextForDays(Strings.D6)
                        VerticalSpacer(Dimension.dimen_5)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(WooColor.lightBlue)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center),
                                imageVector = Icons.Outlined.CardGiftcard,
                                contentDescription = "",
                                tint = WooColor.circulInner
                            )
                        }
                        VerticalSpacer(Dimension.dimen_5)
                        TextForCollect(Strings.collectText)

                    }
// D7
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextForDays(Strings.D7)
                        VerticalSpacer(Dimension.dimen_5)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(WooColor.lightBlue)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center),
                                imageVector = Icons.Outlined.CardGiftcard,
                                contentDescription = "",
                                tint = WooColor.circulInner
                            )
                        }
                        VerticalSpacer(Dimension.dimen_5)
                        TextForCollect(Strings.collectText)

                    }

                }
                VerticalSpacer()
                Text(
                    text = Strings.claimAssetText,
                    modifier = Modifier.align(Alignment.End),
                    fontSize = 14.sp
                )
            }
            VerticalSpacer()
            ViewDivider()
            VerticalSpacer()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = Strings.timeLeftText)
                Text(text = "12:12:31")
            }
            VerticalSpacer()
            // chat progress
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = Strings.chatText,modifier = Modifier.weight(3f))
                CustomLinearProgressIndicator(
                    progress = 0.9f,
                    Color.Green,
                    Modifier.weight(7f),
                    Color.Green,
                    "Send 5 Messages to Claim Asset"
                )
            }
            // meeting progress
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = Strings.meetingText,modifier = Modifier.weight(3f))
                CustomLinearProgressIndicator(
                    progress = 0.5f,
                    Color(0xFFFF6600),
                    Modifier.weight(7f),
                    Color(0xFFFF6600),
                    "1 Meeting to Claim Asset"
                )
            }
            // Call progress
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = Strings.callText,modifier = Modifier.weight(3f))
                CustomLinearProgressIndicator(
                    progress = 0.3f,
                    Color.Red,
                    Modifier.weight(7f),
                    Color.Red,
                    "1 Meeting to Claim Asset"
                )
            }
            VerticalSpacer()
            ViewDivider()
            VerticalSpacer()
            // mining History
            Text(text = Strings.mHstryText)
            // Weak Month Year  All
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp,WooColor.white,shape = RoundedCornerShape(20.dp))
                        .size(40.dp)
                        .background(WooColor.textBox)

                ) {
                    Text(text = "W",modifier = Modifier.align(Alignment.Center))
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp,WooColor.white,shape = RoundedCornerShape(20.dp))
                        .size(40.dp)
                        .background(WooColor.textBox)

                ) {
                    Text(text = "M",modifier = Modifier.align(Alignment.Center))
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp,WooColor.white,shape = RoundedCornerShape(20.dp))
                        .size(40.dp)
                        .background(WooColor.textBox)

                ) {
                    Text(text = "Y",modifier = Modifier.align(Alignment.Center))
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp,WooColor.white,shape = RoundedCornerShape(20.dp))
                        .size(40.dp)
                        .background(WooColor.textBox)

                ) {
                    Text(text = "All",modifier = Modifier.align(Alignment.Center))
                }

            }
            // Graph
            BarGraph(
                graphBarData = floatValue,
                xAxisScaleData = datesList,
                barData_ = dataList,
                height = 200.dp,
                roundType = BarType.TOP_CURVED,
                barWidth = 55.dp,
                barArrangement = Arrangement.SpaceEvenly
            )
            VerticalSpacer()
            ViewDivider()
            VerticalSpacer(Dimension.dimen_5)

            // wallet view

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "",
                        tint = WooColor.white,
                        modifier = Modifier.size(50.dp)
                    )
                    HorizontalSpacer(Dimension.dimen_5)
                    Text(text = "4.8325 woo \n$ 57.3489",fontSize = 15.sp)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp,WooColor.white,shape = RoundedCornerShape(10.dp))
                        .size(50.dp)
                        .background(WooColor.textBox)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "",
                        tint = WooColor.white,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(27.dp)
                    )
                }

            }
        }
    }
}

@Composable
fun CustomLinearProgressIndicator(
    progress: Float,color: Color,modifier: Modifier = Modifier,textColor: Color,bottomText: String
) {
    val percentage = progress * 100
    val split = percentage.toString().split(".")
    val withOutPoint = split[0]
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "$withOutPoint%",
            fontSize = 13.sp,
            color = textColor,
            modifier = Modifier.offset(x = 100.dp)
        )
        LinearProgressIndicator(
            progress = percentage / 100f,
            color = color,
            modifier = Modifier
                .fillMaxSize()
                .height(10.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Text(text = bottomText,style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun TextForDays(text: String) {
    Text(text = text,fontSize = 11.sp,color = WooColor.circulInner)
}

@Composable
fun TextForCollect(text: String) {
    Text(text = text,fontSize = 10.sp,color = WooColor.circulInner)
}

