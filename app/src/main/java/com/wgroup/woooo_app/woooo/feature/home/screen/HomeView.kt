package com.wgroup.woooo_app.woooo.feature.home.screen

import TopAppBarComposable
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.feature.home.ui.CircularMenu
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.ViewDivider
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension

@Composable
fun HomeView() {

    HomePage()

}

@Composable
fun HomePage() {
    BoxWithConstraints(
        Modifier
            .padding(top = Dimension.dimen_10)


    ) {
        Dimension.boxWithConstraintsScope = this

        SimpleBottomSheetScaffoldSample()


    }

}

fun initCircleTextOffset(width: Dp) {

    Dimension.circleWheelSectorRadius = (Dimension.circleWheelHeight / 7.2F)
    Dimension.circleWheelTextHeight = (Dimension.circleWheelHeight * 0.1875F)

    if (width < 400.dp) {
        Log.d("DEVICE WIDTH SMALL", width.toString())
        //Chat offset
        Dimension.chatTextOffset_X = width * 0.2F
        Dimension.chatTextOffset_Y = 58.dp
        //Call offset
        Dimension.callTextOffset_X = width * 0.185F
        Dimension.callTextOffset_Y = 60.dp
        //Wallet offset
        Dimension.walletTextOffset_X = width * 0.2F
        Dimension.walletTextOffset_Y = 60.dp
        //Meeting offset
        Dimension.meetingTextOffset_X = width * 0.20F
        Dimension.meetingTextOffset_Y = 59.dp
    } else {
        //Chat offset
        Dimension.chatTextOffset_X = width * 0.2F
        Dimension.chatTextOffset_Y = 70.dp
        //Call offset
        Dimension.callTextOffset_X = width * 0.2F
        Dimension.callTextOffset_Y = 74.dp
        //Wallet offset
        Dimension.walletTextOffset_X = width * 0.2F
        Dimension.walletTextOffset_Y = 73.dp
        //Meeting offset
        Dimension.meetingTextOffset_X = width * 0.2F
        Dimension.meetingTextOffset_Y = 73.dp
        Log.d("DEVICE WIDTH MEDIUM", width.toString())
    }


    //
    // print("Circle Wheel Height: ${Dimension.circleWheelHeight}");
    // print("Circle Middle Wheel Height: ${Dimension.circleMiddleWheelHeight}");
    // print("Circle Inner Wheel Height: ${Dimension.circleInnerWheelHeight}");
    // print("Center Space Radius: ${Dimension.circleWheelCenterSpaceRadius}");
    // print("Sector Radius: ${Dimension.circleWheelSectorRadius}");
    // print("Text Height: ${Dimension.circleWheelTextHeight}");
}

enum class ExpandedType {
    HALF, FULL, COLLAPSED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleBottomSheetScaffoldSample() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()


    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    var expandedType by remember {
        mutableStateOf(ExpandedType.COLLAPSED)
    }
    val height by animateIntAsState(
        when (expandedType) {
            ExpandedType.HALF -> screenHeight / 2
            ExpandedType.FULL -> screenHeight
            ExpandedType.COLLAPSED -> 70
        }
    )






    BottomSheetScaffold(
        topBar = {
            TopAppBarComposable()
        },
        scaffoldState = scaffoldState,
        containerColor = Color.Transparent,
        sheetContainerColor = Color.Transparent,
        sheetDragHandle = {},
        sheetTonalElevation = 0.dp,
        sheetShadowElevation = 0.dp,
        sheetContent = { BottomSheetContent() }


//        sheetContent = {
//            Box(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(Dimension.dimen_20))
//            ) {
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .alpha(0.5f)
//                        .background(WooColor.textFieldBackGround)
//                )
//
//                Column(
//                    modifier = Modifier
//                        .verticalScroll(rememberScrollState())
//                ) {
//
//                    Button(
//                        modifier = Modifier
//                            .align(
//                                alignment = Alignment.CenterHorizontally
//                            ),
//                        onClick = { /*TODO*/ }) {
//                        Text(text = "Show more",style = MaterialTheme.typography.bodyMedium)
//                    }
//
//
//                    BottomSheetCard("Chat")
//                    VerticalSpacer()
//                    BottomSheetCard("Call")
//                    VerticalSpacer()
//                    BottomSheetCard("Meeting")
//                    VerticalSpacer()
//                    BottomSheetCard("Wallet")
//                    VerticalSpacer()
//                    BottomSheetCard("Daily Reward")
//                    VerticalSpacer()
//                }
//
//
//            }
//
//        }
////


    )


    { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ViewDivider()
            VerticalSpacer()
            //Name Text
            Text(
                text = "Hi, Mac27",
                modifier = Modifier
                    .align(
                        alignment = Alignment.Start
                    )
                    .padding(start = Dimension.dimen_20),
                style = MaterialTheme.typography.bodyLarge
            )

            CircularMenu()
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = Dimension.dimen_20),
                Arrangement.spacedBy(Dimension.dimen_20)

            ) {
                //DailyProgress Label
                DailyProgress()
                //DailyProgressBarIndicator

                GradientProgressbar()
                //Pending Call,Chat,Meeting
                PendingChatCallMeeting()


            }


        }

    }
}


@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
    ) {
        Row {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState {
//                            Toast.makeText(context, "Non Draggable Area", Toast.LENGTH_SHORT).show()

                        }
                    )
                    .fillMaxWidth()
                    .height(Dimension.boxWithConstraintsScope.maxHeight / 2)
                    .background(Color.Yellow)) {
            }

            Box(
                modifier = modifier
                    .padding(end = 8.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp
                        )
                    )
                    .height(Dimension.boxWithConstraintsScope.maxHeight / 2)
                    .background(Color.Transparent),
                contentAlignment = Alignment.BottomCenter
            ) {

                Icon(
                    modifier = modifier,
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                )
            }
        }


        Text(
            modifier = modifier
                .fillMaxWidth()
                .background(WooColor.primary)
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 4.dp
                ),
            text = "Scan Serial With QR",
        )
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.DarkGray)
        )
    }
}


@Composable
fun DailyProgress() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimension.dimen_20),


        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Your Daily Progress", style = MaterialTheme.typography.bodyLarge)
            Text(text = "0% to complete", style = MaterialTheme.typography.labelSmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Outlined.AccessTime, contentDescription = "")
            HorizontalSpacer(Dimension.dimen_5)
            Text(text = "12 hrs", style = MaterialTheme.typography.labelSmall)
        }

    }
}

@Composable
fun GradientProgressbar(
    indicatorHeight: Dp = 20.dp,
    backgroundIndicatorColor: Color = Color.LightGray.copy(alpha = 0.3f),
    indicatorPadding: Dp = 20.dp,
    gradientColors: List<Color> = listOf(
        WooColor.Yellow,
        WooColor.OrangeAccent,
        WooColor.Orange,
    ),
    animationDuration: Int = 1000,
    animationDelay: Int = 0
) {

    val animateNumber = animateFloatAsState(
        targetValue = 80f,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animationDelay
        )
    )


    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(indicatorHeight)
            .padding(horizontal = indicatorPadding)
    ) {

        // Background indicator
        drawLine(
            color = backgroundIndicatorColor,
            cap = StrokeCap.Round,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = 0f)
        )

        // Convert the downloaded percentage into progress (width of foreground indicator)
        val progress =
            (animateNumber.value / 100) * size.width // size.width returns the width of the canvas

        // Foreground indicator
        drawLine(
            brush = Brush.linearGradient(
                colors = gradientColors
            ),
            cap = StrokeCap.Round,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = progress, y = 0f)
        )

    }

}


@Composable
fun PendingChatCallMeeting() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimension.dimen_20),
        horizontalAlignment = Alignment.Start
    ) {

        //Pending Call
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "Call: ", style = MaterialTheme.typography.bodySmall)
            Text(text = "2 calls remaining", style = MaterialTheme.typography.bodySmall)

        }
        //Pending Chat
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "Chat: ", style = MaterialTheme.typography.bodySmall)
            Text(text = "5 messages remaining", style = MaterialTheme.typography.bodySmall)

        }
        //Pending Meetings
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "Meeting: ", style = MaterialTheme.typography.bodySmall)
            Text(text = "1 meeting remaining", style = MaterialTheme.typography.bodySmall)

        }
    }
}

@Composable
fun BottomSheetCard(label: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimension.dimen_20)
            .height(300.dp)
            .clip(RoundedCornerShape(Dimension.dimen_20))
            .border(border = BorderStroke(width = 1.dp, color = WooColor.white))
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    Dimension.dimen_20,
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = label, style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Outlined.ArrowForward, contentDescription = "")

                }
            }

            ViewDivider()

        }


    }
}

