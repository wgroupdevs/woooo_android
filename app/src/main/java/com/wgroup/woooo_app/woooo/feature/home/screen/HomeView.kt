package com.wgroup.woooo_app.woooo.feature.home.screen

import TopAppBarComposable
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.feature.home.ui.CircularMenu
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Button
import kotlinx.coroutines.launch

@Composable
fun HomeView() {

    HomePage()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {

    Scaffold(
        topBar = {
            TopAppBarComposable()
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
                ) {

                CircularMenu()
//                SimpleBottomSheetScaffoldSample()

            }
        },


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleBottomSheetScaffoldSample() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        containerColor = Color.Transparent,
        sheetContainerColor = Color.Transparent,
        sheetDragHandle = {},
        sheetShadowElevation = 1.dp,
        sheetPeekHeight = 50.dp,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Swipe up to expand sheet")
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sheet content")
                Spacer(Modifier.height(20.dp))

            }
        }) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
//            Text("Scaffold Content")
        }
    }
}
