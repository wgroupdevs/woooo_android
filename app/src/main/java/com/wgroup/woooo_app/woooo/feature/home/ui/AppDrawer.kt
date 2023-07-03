package com.wgroup.woooo_app.woooo.feature.home.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.R
import com.wgroup.woooo_app.woooo.shared.components.CustomListTile
import com.wgroup.woooo_app.woooo.theme.WooColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit = {},
    closeDrawer: () -> Unit = {}
) {
    ModalDrawerSheet(
        modifier = modifier
            .border(
                border = BorderStroke(width = 0.5.dp, color = WooColor.white),
                shape = RoundedCornerShape(0.dp, 15.dp, 15.dp, 0.dp)
            ),
        drawerContainerColor = Color.Transparent
    ) {
        DrawerHeader(modifier)
//        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.spacer_padding)))
        CustomListTile(
            title = "Home",
            leadingIcon = {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
            }, onClick = {})

    }
}


@Composable
fun DrawerHeader(modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxWidth()
    ) {

        Image(
            painterResource(id = R.drawable.outer_wheel_1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(CircleShape)
        )


        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

