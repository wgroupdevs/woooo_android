package woooo_app.woooo.shared.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import eu.siacs.conversations.R
import woooo_app.woooo.feature.auth.GV

@Composable
fun UserProfileImage(size: Dp,onClick: () -> Unit) {
    val url = GV.getUserProfileImage.value
    val painter = rememberAsyncImagePainter(url)
    Box(
        Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = if (url.isEmpty()) painterResource(R.drawable.empty_profile) else painter,
            contentDescription = null,
            modifier = Modifier
                .size(size)
                .background(color = Color.Transparent,shape = CircleShape)
                .border(2.dp,Color.White,shape = CircleShape),
        )
    }
}