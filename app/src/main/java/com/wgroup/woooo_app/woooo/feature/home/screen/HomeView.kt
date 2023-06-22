package com.wgroup.woooo_app.woooo.feature.home.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.R
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension

@Composable
fun HomeView() {


    BoxExample()


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxExample() {
    val inner_wheel = painterResource(id = R.drawable.inner_wheel)
    val middle_wheel = painterResource(id = R.drawable.middle_wheel)
    val outer_wheel = painterResource(id = R.drawable.outer_wheel)
    val meeting_text_active = painterResource(id = R.drawable.meeting_text_active)
    val chat_text_active = painterResource(id = R.drawable.chat_text_active)
    val call_text_active = painterResource(id = R.drawable.call_text_active)
    val wallet_text_active = painterResource(id = R.drawable.wallet_text_active)

    Scaffold(

        topBar = {

            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = WooColor.primary),
                title = { Text(text = "Wooooo") },
            )

        },

        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),

                ) {
                BoxWithConstraints(
                    Modifier
                        .padding(top = Dimension.dimen_25)
                        .align(Alignment.CenterHorizontally)
                ) {
                    val boxWithConstraintsScope = this


                    Log.d("Constraint Width :", boxWithConstraintsScope.maxWidth.toString())

                    initCircleTextOffset(boxWithConstraintsScope.maxWidth)

                    Image(
                        painter = inner_wheel, contentDescription = null, modifier = Modifier
                            .width(boxWithConstraintsScope.maxWidth * 0.33f)
                            .height(boxWithConstraintsScope.maxWidth * 0.33f)
                            .align(Alignment.Center),
                        contentScale = ContentScale.FillBounds
                    )

                    Image(
                        painter = middle_wheel, contentDescription = null, modifier = Modifier
                            .width(boxWithConstraintsScope.maxWidth * 0.70f)
                            .height(boxWithConstraintsScope.maxWidth * 0.70f)
                            .align(Alignment.Center),
                        contentScale = ContentScale.FillBounds
                    )

                    Image(
                        painter = outer_wheel, contentDescription = null,
                        modifier = Modifier
                            .width(boxWithConstraintsScope.maxWidth * 0.9f)
                            .height(boxWithConstraintsScope.maxWidth * 0.9f)
                            .align(Alignment.Center),
                        contentScale = ContentScale.FillBounds
                    )

//                    /Text
                    Box(
                        modifier = Modifier
                            .absoluteOffset(
                                x = Dimension.chatTextOffset_X,
                                y = Dimension.chatTextOffset_Y
                            )
                            .align(Alignment.TopStart),
                    ) {
                        Image(
                            painter = chat_text_active, contentDescription = null,
                            modifier = Modifier
                                .height(Dimension.circleWheelTextHeight)
                                .width(Dimension.circleWheelTextHeight)
                                .rotate(Dimension.chatTextRotation)
                                .align(Alignment.TopStart),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .absoluteOffset(
                                x = -Dimension.meetingTextOffset_X,
                                y = Dimension.meetingTextOffset_Y
                            )
                            .align(Alignment.TopEnd),
                    ) {
                        Image(
                            painter = meeting_text_active, contentDescription = null,
                            modifier = Modifier
                                .height(Dimension.circleWheelTextHeight)
                                .width(Dimension.circleWheelTextHeight)
                                .rotate(-6f)
                                .align(Alignment.TopEnd),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .absoluteOffset(
                                x = Dimension.callTextOffset_X,
                                y = -Dimension.callTextOffset_Y
                            )
                            .align(Alignment.BottomStart),
                    ) {
                        Image(
                            painter = call_text_active, contentDescription = null,
                            modifier = Modifier
                                .height(Dimension.circleWheelTextHeight)
                                .width(Dimension.circleWheelTextHeight)

                                .align(Alignment.BottomStart),
                        )
                    }   ///Text
                    Box(
                        modifier = Modifier
                            .absoluteOffset(
                                x = -Dimension.walletTextOffset_X,
                                y = -Dimension.walletTextOffset_Y
                            )
                            .align(Alignment.BottomEnd),
                    ) {
                        Image(
                            painter = wallet_text_active, contentDescription = null,
                            modifier = Modifier
                                .height(Dimension.circleWheelTextHeight)
                                .width(Dimension.circleWheelTextHeight)
                                .rotate(5F)
                        )
                    }

                }


            }
        },

        bottomBar = { BottomAppBar(containerColor = WooColor.primary) { Text("BottomAppBar") } }
    )


}


fun initCircleTextOffset(width: Dp) {

    Dimension.circleWheelSectorRadius = (Dimension.circleWheelHeight / 7.2F)
    Dimension.circleWheelTextHeight = (Dimension.circleWheelHeight * 0.1875F)

    if (width < 400.dp) {
        Log.d("DEVICE WIDTH SMALL", width.toString())
        //Chat offset
        Dimension.chatTextOffset_X=width * 0.2F
        Dimension.chatTextOffset_Y=58.dp
        //Call offset
        Dimension.callTextOffset_X=width * 0.185F
        Dimension.callTextOffset_Y=60.dp
        //Wallet offset
        Dimension.walletTextOffset_X=width * 0.2F
        Dimension.walletTextOffset_Y=60.dp
        //Meeting offset
        Dimension.meetingTextOffset_X=width * 0.20F
        Dimension.meetingTextOffset_Y=59.dp
    } else {
        //Chat offset
        Dimension.chatTextOffset_X=width * 0.2F
        Dimension.chatTextOffset_Y=70.dp
        //Call offset
        Dimension.callTextOffset_X=width * 0.2F
        Dimension.callTextOffset_Y=74.dp
        //Wallet offset
        Dimension.walletTextOffset_X=width * 0.2F
        Dimension.walletTextOffset_Y=73.dp
        //Meeting offset
        Dimension.meetingTextOffset_X=width * 0.2F
        Dimension.meetingTextOffset_Y=73.dp
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
