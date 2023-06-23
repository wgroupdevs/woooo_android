package com.wgroup.woooo_app.woooo.feature.home.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.feature.home.ui.CircularMenu
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension

@Composable
fun HomeView() {

    BoxExample()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxExample() {
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
                horizontalAlignment = Alignment.CenterHorizontally

                ) {

                CircularMenu()

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
