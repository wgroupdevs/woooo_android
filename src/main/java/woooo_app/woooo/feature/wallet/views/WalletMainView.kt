package woooo_app.woooo.feature.wallet.views

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Forward10
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.feature.wallet.viewmodel.WalletMainViewViewModel
import com.wgroup.woooo_app.woooo.shared.components.CustomIcon
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Strings
import woooo_app.woooo.destinations.SendCurrencyMainScreenDestination
import woooo_app.woooo.destinations.TransactionMainScreenDestination
import woooo_app.woooo.utils.Dimension

@Composable
fun WalletMainView(navigator: DestinationsNavigator) {
    val colors = mutableListOf<Color>(
        WooColor.Yellow,Color.Cyan
    )
    val walletMainViewModel: WalletMainViewViewModel = hiltViewModel()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        VerticalSpacer(100.dp)
        // pie Chart
        Box {
            PieChart(
                data = mapOf(
                    Pair("Sample-1",100),
                    Pair("Sample-2",7),
                ),
                listOfColors = colors
            )
            // pie chart center Currency
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(text = "Woo",fontSize = 25.sp)
                Text(text = "4.0",fontSize = 15.sp)

            }
        }
        VerticalSpacer(30.dp)

        // Row of Currencies

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .height(50.dp)

//                .clip(
//                    shape = RoundedCornerShape(5.dp)
//                )
                .border(
                    BorderStroke(1.dp,WooColor.white),shape = RoundedCornerShape(10.dp)
                )
                .fillMaxWidth(0.5f)
                .padding(10.dp)

        ) {
            Text(text = "5.796")
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { walletMainViewModel.setCurrencyPopUpValue(true) }) {
                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(2.dp)
//                        .background(Color.White),
                )
                HorizontalSpacer(5.dp)
                Text(text = walletMainViewModel.getCurrentValueOption.value)
//                HorizontalSpacer(Dimension.dimen_5)
                CustomIcon(icon = Icons.Outlined.ArrowDropDown)
                DropdownMenuExample()
            }

        }

        // Row of transaction and Wallet text

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Row(
                modifier = Modifier.clickable(onClick = {
                    navigator.navigate(
                        TransactionMainScreenDestination
                    )
                }),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = Strings.trnscetgText)
                HorizontalSpacer(5.dp)
                CustomIcon(icon = Icons.Rounded.ArrowForwardIos)

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = Strings.walletText)
                HorizontalSpacer(5.dp)
                CustomIcon(icon = Icons.Rounded.ArrowForwardIos)

            }
        }

        // list of Wallets

        LazyColumn {

            val listItems = listOf("Item 1","Item 2","Item 3","Item 4")
            items(count = listItems.size,itemContent = {

                ListItem(modifier = Modifier
                    .clickable(onClick = {
                        navigator.navigate(
                            SendCurrencyMainScreenDestination
                        )
                    })
                    .padding(5.dp)
                    .padding(start = 10.dp,end = 10.dp)
                    .border(
                        BorderStroke(1.dp,WooColor.white),shape = RoundedCornerShape(30.dp)
                    )
                    .fillMaxWidth()
//                        .wrapContentHeight()
                    .background(WooColor.textFieldBackGround),
                    colors = ListItemDefaults.colors(containerColor = WooColor.primary),
                    headlineContent = {
                        Text(
                            fontSize = 10.sp,
                            text = listItems[0],
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    leadingContent = {
                        CustomIcon(icon = Icons.Rounded.ArrowForwardIos)
                    },
                    trailingContent = {
                        Row(horizontalArrangement = Arrangement.Center) {
                            Text(text = " 0.0  BTC")
                            HorizontalSpacer(Dimension.dimen_5)
                            CustomIcon(icon = Icons.Rounded.Forward10)
                        }
                    }

                )

            })

        }

    }
}

@Composable
fun DropdownMenuExample() {
    val walletMainViewModel: WalletMainViewViewModel = hiltViewModel()

    DropdownMenu(
        expanded = walletMainViewModel.getCurrencyPopUp.value,
        onDismissRequest = { walletMainViewModel.setCurrencyPopUpValue(false) },
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .background(WooColor.textBox)
    ) {
        DropdownMenuItem(onClick = {
            walletMainViewModel.setCurrentValueOption("USD")
            walletMainViewModel.setCurrencyPopUpValue(false)
        },text = { Text("USD") })
        DropdownMenuItem(onClick = {
            walletMainViewModel.setCurrentValueOption("YEN")
            walletMainViewModel.setCurrencyPopUpValue(false)
        },text = { Text("YEN") })

        DropdownMenuItem(onClick = {
            walletMainViewModel.setCurrentValueOption("EURO")
            walletMainViewModel.setCurrencyPopUpValue(false)
        },text = { Text("EURO") })

    }
}

//}
@Composable
fun PieChart(
    data: Map<String,Int>,
    radiusOuter: Dp = 80.dp,
    chartBarWidth: Dp = 25.dp,
    animDuration: Int = 2000,
    listOfColors: MutableList<Color>
) {

    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    data.values.forEachIndexed { index,values ->
        floatValue.add(index,360 * values.toFloat() / totalSum.toFloat())
    }

    var animationPlayed by remember { mutableStateOf(false) }

    var lastValue = 0f

    // it is the diameter value of the Pie
    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,animationSpec = tween(
            durationMillis = animDuration,delayMillis = 0,easing = LinearOutSlowInEasing
        )
    )

    // if you want to stabilize the Pie Chart you can use value -90f
    // 90f is used to complete 1/4 of the rotation
    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,animationSpec = tween(
            durationMillis = animDuration,delayMillis = 0,easing = LinearOutSlowInEasing
        )
    )

    // to play the animation only once when the function is Created or Recomposed
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Pie Chart using Canvas Arc
        Box(
            modifier = Modifier.size(animateSize.dp),contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(radiusOuter)
                    .rotate(animateRotation)
            ) {
                // draw each Arc for each data entry in Pie Chart
                floatValue.forEachIndexed { index,value ->
                    drawArc(
                        color = listOfColors[index],
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(),cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }
    }
}