package com.wgroup.woooo_app.woooo.feature.profile.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.R
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.ViewDivider
import com.wgroup.woooo_app.woooo.theme.WooColor

@Composable
fun UpdateProfileView() {
    Column(
        Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                contentDescription = "",
                tint = WooColor.white,
                modifier = Modifier.clickable(onClick = {})
            )
            HorizontalSpacer()
            Text(text = "Profile")
        }
        VerticalSpacer()
        ViewDivider()
        VerticalSpacer()
        Box(
            Modifier
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(modifier = Modifier.size(150.dp),
                painter = painterResource(R.drawable.profile_view),
                contentDescription = "This is a circular image",
                contentScale = ContentScale.FillBounds
            )
            Box(modifier = Modifier.align(Alignment.BottomEnd).offset(x = 10.dp ))
            {
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = "",
                    tint = WooColor.white,
                    modifier = Modifier.align(Alignment.BottomEnd).size(50.dp)
                )
            }
        }



        Text(
            text = "About",style = MaterialTheme.typography.labelMedium.copy(color = WooColor.white)
        )
    }
}