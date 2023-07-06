package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wgroup.woooo_app.woooo.shared.components.view_models.CountryPickerViewModel
import com.wgroup.woooo_app.woooo.theme.WooColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryPicker() {
    val countryPickerViewModel: CountryPickerViewModel = hiltViewModel()

    AlertDialog(
        onDismissRequest = {},modifier = Modifier
            .padding(vertical = 50.dp)
            .fillMaxHeight()
//            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color = WooColor.textBox)
            .border(
                border = BorderStroke(1.dp,color = WooColor.white),shape = RoundedCornerShape(10.dp)
            )
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp,vertical = 10.dp)) {

            WooTextField(
                unfocusedColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp))
                    .border(
                        border = BorderStroke(1.dp,color = WooColor.white),
                        shape = RoundedCornerShape(30.dp)
                    ),
                hint = "Search"
            )
            LazyColumn {
                items(countryPickerViewModel.getCountryList.size) { date ->

                    CustomListTile(
                        colors = ListItemDefaults.colors(containerColor = WooColor.textBox),
                        leadingIcon = {
                            Text(text = countryPickerViewModel.getCountryList[date].dial_code)
                        },
                        onClick = {},
                        title = countryPickerViewModel.getCountryList[date].name,
                    )
                    ViewDivider()
                }
            }
        }
    }
}
