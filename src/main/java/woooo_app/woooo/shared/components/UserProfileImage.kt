package woooo_app.woooo.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import woooo_app.woooo.feature.auth.GV

@Composable
fun UserProfileImage(size: Dp,onClick: () -> Unit) {

    Box(
        Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = GV.getUserProfileImage.value,
            modifier = Modifier
                .size(size)
//                .height(150.dp)
//                .width(150.dp)
                .background(color = Color.Transparent,shape = CircleShape)
                .border(2.dp,Color.White,shape = CircleShape),
            contentDescription = "This is a circular image",
            contentScale = ContentScale.FillBounds
        )
    }
}