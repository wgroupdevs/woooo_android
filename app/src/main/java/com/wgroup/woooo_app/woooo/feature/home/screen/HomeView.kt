package com.wgroup.woooo_app.woooo.feature.home.screen

import TopAppBarComposable
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.R
import com.wgroup.woooo_app.woooo.destinations.SignUpScreenDestination
import com.wgroup.woooo_app.woooo.feature.home.ui.AppDrawer
import com.wgroup.woooo_app.woooo.feature.home.ui.CircularMenu
import com.wgroup.woooo_app.woooo.feature.home.viewmodel.HomeViewModel

import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.ViewDivider
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navigator: DestinationsNavigator,homeViewModel: HomeViewModel = hiltViewModel()) {
    BoxWithConstraints(
        Modifier
            .padding(top = Dimension.dimen_10)

    ) {
        Dimension.boxWithConstraintsScope = this
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberBottomSheetScaffoldState()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        ModalNavigationDrawer(drawerContent = {
            AppDrawer(navigator = navigator)
        },drawerState = drawerState) {
            Scaffold(
                topBar = {
                    TopAppBarComposable(navigationIcon = {
                        Image(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(
                                    50.dp
                                )
                                .clickable {
                                    scope.launch {
                                        drawerState.open()
                                    }

                                },
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = ""
                        )

                    })
                },

                ) { upperPadding ->
                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    containerColor = Color.Transparent,
                    sheetContainerColor = Color.Transparent,
                    sheetDragHandle = {},
                    sheetTonalElevation = 0.dp,
                    sheetShadowElevation = 0.dp,
                    sheetContent = { BottomSheetContent() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(upperPadding),
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
                                .padding(start = Dimension.dimen_20)
                                .clickable {
                                    navigator.navigate(SignUpScreenDestination)
                                },
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

                            GradientProgressbar(
                                gradientColors = listOf(
                                    WooColor.Yellow,
                                    WooColor.OrangeAccent,
                                    WooColor.Orange,
                                ), targetValue = 70f
                            )
                            //Pending Call,Chat,Meeting
                            PendingChatCallMeeting()

                        }

                    }

                }
            }

        }

    }

}

fun initCircleTextOffset(width: Dp) {

    Dimension.circleWheelSectorRadius = (Dimension.circleWheelHeight / 7.2F)
    Dimension.circleWheelTextHeight = (Dimension.circleWheelHeight * 0.1875F)

    if (width < 400.dp) {
        Log.d("DEVICE WIDTH SMALL",width.toString())
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
        Log.d("DEVICE WIDTH MEDIUM",width.toString())
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

@Composable
fun GradientProgressbar(
    indicatorHeight: Dp = 20.dp,
    backgroundIndicatorColor: Color = Color.LightGray.copy(alpha = 0.3f),
    indicatorPadding: Dp = 0.dp,
    gradientColors: List<Color>,
    animationDuration: Int = 1000,
    animationDelay: Int = 0,
    targetValue : Float
) {

    val animateNumber = animateFloatAsState(
        targetValue = targetValue,
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
fun BottomSheetContent(
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimension.dimen_20))
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.5f)
                .background(WooColor.textFieldBackGround)
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {

            Button(
                modifier = Modifier
                    .align(
                        alignment = Alignment.CenterHorizontally
                    ),
                onClick = { /*TODO*/ }) {
                Text(text = "Show more",style = MaterialTheme.typography.bodyMedium)
            }


            BottomSheetCard("Chat")
            VerticalSpacer()
            BottomSheetCard("Call")
            VerticalSpacer()
            BottomSheetCard("Meeting")
            VerticalSpacer()
            BottomSheetCard("Wallet")
            VerticalSpacer()
            BottomSheetCard("Daily Reward")
            VerticalSpacer()
        }

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
            Text(text = "Your Daily Progress",style = MaterialTheme.typography.bodyLarge)
            Text(text = "0% to complete",style = MaterialTheme.typography.labelSmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Outlined.AccessTime,contentDescription = "")
            HorizontalSpacer(Dimension.dimen_5)
            Text(text = "12 hrs",style = MaterialTheme.typography.labelSmall)
        }

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
            Text(text = "Call: ",style = MaterialTheme.typography.bodySmall)
            Text(text = "2 calls remaining",style = MaterialTheme.typography.bodySmall)

        }
        //Pending Chat
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "Chat: ",style = MaterialTheme.typography.bodySmall)
            Text(text = "5 messages remaining",style = MaterialTheme.typography.bodySmall)

        }
        //Pending Meetings
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "Meeting: ",style = MaterialTheme.typography.bodySmall)
            Text(text = "1 meeting remaining",style = MaterialTheme.typography.bodySmall)

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
            .border(border = BorderStroke(width = 1.dp,color = WooColor.white))
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
                Text(text = label,style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Outlined.ArrowForward,contentDescription = "")

                }
            }

            ViewDivider()

        }

    }
}

