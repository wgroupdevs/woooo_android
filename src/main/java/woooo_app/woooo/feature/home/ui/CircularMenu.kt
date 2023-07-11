package woooo_app.woooo.feature.home.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.feature.home.screen.initCircleTextOffset
import com.wgroup.woooo_app.woooo.feature.home.viewmodel.CircularMenuViewModel
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import eu.siacs.conversations.R
import eu.siacs.conversations.ui.ConversationActivity
import eu.siacs.conversations.ui.ConversationsActivity
import eu.siacs.conversations.ui.StartConversationActivity
import eu.siacs.conversations.ui.WelcomeActivity
import kotlinx.coroutines.launch

@Composable
fun CircularMenu(navigator: DestinationsNavigator) {

    val circularMenuViewModel: CircularMenuViewModel = hiltViewModel()
    val inner_wheel = painterResource(id = R.drawable.inner_wheel)
    val meeting_text_active = painterResource(id = R.drawable.meeting_text_disable)
    val chat_text_active = painterResource(id = R.drawable.chat_text_disable)
    val call_text_active = painterResource(id = R.drawable.call_text_disable)
    val wallet_text_active = painterResource(id = R.drawable.wallet_text_disable)
    val scopeClockWise = rememberCoroutineScope()
    val scopeAntiClockWise = rememberCoroutineScope()

    val context = LocalContext.current

    Box(
        Modifier
            .padding(top = Dimension.dimen_10)
    ) {

        initCircleTextOffset(Dimension.boxWithConstraintsScope.maxWidth)

        Image(
            painter = inner_wheel, contentDescription = null, modifier = Modifier
                .width(Dimension.boxWithConstraintsScope.maxWidth * 0.33f)
                .height(Dimension.boxWithConstraintsScope.maxWidth * 0.33f)
                .align(Alignment.Center)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple(
                        bounded = false,
                        color = WooColor.white,
                        radius = 100.dp
                    ),
                ) {

                    scopeClockWise.launch {
                        circularMenuViewModel.rotateOuterCircleClockWise()
                    }
                    scopeAntiClockWise.launch {
                        circularMenuViewModel.rotateMiddleCircleAntiClockWise()
                    }


                },
            contentScale = ContentScale.FillBounds
        )

        //Middle Circle Compose
        Box(
            modifier = Modifier
                .align(Alignment.Center),
        ) {
            MiddleCircle(viewModel = circularMenuViewModel)

        }

        //OuterCircle Compose
        OuterCircle(viewModel = circularMenuViewModel)


//         ChatText
        Box(
            modifier = Modifier
                .absoluteOffset(
                    x = Dimension.chatTextOffset_X,
                    y = Dimension.chatTextOffset_Y
                )
                .align(Alignment.TopStart)
                .clickable {
                    scopeClockWise.launch {
                        circularMenuViewModel.rotateOuterCircleClockWise()
                    }
                    scopeAntiClockWise.launch {
                        circularMenuViewModel.rotateMiddleCircleAntiClockWise()

                        context.startActivity(Intent(context, ConversationActivity::class.java))


//                        navigator.navigate(DashboardScreenDestination)

                    }

                },
        ) {
            Image(
                painter = chat_text_active, contentDescription = "chat_text_active",
                modifier = Modifier
                    .height(Dimension.circleWheelTextHeight)
                    .width(Dimension.circleWheelTextHeight)
                    .rotate(Dimension.chatTextRotation)
            )
        }
        Box(
            modifier = Modifier
                .absoluteOffset(
                    x = -Dimension.meetingTextOffset_X,
                    y = Dimension.meetingTextOffset_Y
                )
                .align(Alignment.TopEnd)
                .clickable {
                    scopeClockWise.launch {
                        circularMenuViewModel.rotateOuterCircleAntiClockWise()
                    }
                    scopeAntiClockWise.launch {
                        circularMenuViewModel.rotateMiddleCircleClockWise()
                    }
                },
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
                .align(Alignment.BottomStart)
                .clickable {
                    scopeClockWise.launch {
                        circularMenuViewModel.rotateOuterCircleClockWise()
                    }
                    scopeAntiClockWise.launch {
                        circularMenuViewModel.rotateMiddleCircleAntiClockWise()
                    }
                },
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
                .align(Alignment.BottomEnd)
                .clickable {
                    scopeClockWise.launch {
                        circularMenuViewModel.rotateOuterCircleAntiClockWise()
                    }
                    scopeAntiClockWise.launch {
                        circularMenuViewModel.rotateMiddleCircleClockWise()
                    }
                },
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

@Composable
fun OuterCircle(viewModel: CircularMenuViewModel) {
    val outer_wheel = painterResource(id = R.drawable.outer_wheel_1)
    Image(
        painter = outer_wheel, contentDescription = "Outer Circle",
        modifier = Modifier
            .width(Dimension.boxWithConstraintsScope.maxWidth * 0.9f)
            .height(Dimension.boxWithConstraintsScope.maxWidth * 0.9f)
            .graphicsLayer {
                rotationZ = if (viewModel.isOuterRotatingClockWise.value) {
                    viewModel.clockWiseRotation.component1().value
                } else {
                    viewModel.antiClockWiseRotation.component1().value
                }
            },
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun MiddleCircle(viewModel: CircularMenuViewModel) {
    val middle_wheel = painterResource(id = R.drawable.middle_wheel_1)
    Image(
        painter = middle_wheel, contentDescription = "middle_wheel",
        modifier = Modifier
            .width(Dimension.boxWithConstraintsScope.maxWidth * 0.70f)
            .height(Dimension.boxWithConstraintsScope.maxWidth * 0.70f)
            .graphicsLayer {
                rotationZ = if (viewModel.isOuterRotatingClockWise.value) {
                    viewModel.antiClockWiseRotation.component1().value
                } else {
                    viewModel.clockWiseRotation.component1().value
                }
            },
        contentScale = ContentScale.FillBounds
    )
}

