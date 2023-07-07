package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.LoginWithPhoneViewModel
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.SignUpViewModel
import com.wgroup.woooo_app.woooo.shared.components.view_models.CountryPickerViewModel
import com.wgroup.woooo_app.woooo.theme.WooColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryPicker(onDismissRequest: () -> Unit,viewModel: ViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val width = LocalConfiguration.current.screenWidthDp
    val countryPickerViewModel: CountryPickerViewModel = hiltViewModel()
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .padding(vertical = 50.dp)
            .fillMaxHeight()
            .requiredWidthIn(width.dp,min(a = width.dp - 40.dp,b = width.dp - 40.dp))
            .clip(RoundedCornerShape(10.dp))
            .alpha(0.8f)
            .background(color = WooColor.textBox)
            .border(
                border = BorderStroke(1.dp,color = WooColor.white),shape = RoundedCornerShape(10.dp)
            )
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp,vertical = 10.dp)) {

            // Display the search results
            WooTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
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
            SearchResults(query = searchQuery,viewModel = viewModel)

//            LazyColumn {
//                items(countryPickerViewModel.getCountryList.size) { date ->
//                    ListItem(
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(15.dp))
//                            .clickable(
//                                onClick = {
//                                    countryPickerViewModel.setSelectedCountryDialCodeValue(
//                                        countryPickerViewModel.getCountryList[date].dial_code
//                                    )
//                                    when (viewModel) {
//                                        is SignUpViewModel -> viewModel.setShowCountryPickerValue(
//                                            false
//                                        )
//
//                                        is LoginWithPhoneViewModel -> {
//                                            viewModel.setShowCountryPickerValue(
//                                                false
//                                            )
//                                            countryPickerViewModel.setSelectedCountryValue(
//                                                countryPickerViewModel.getCountryList[date].name
//                                            )
//                                        }
//                                    }
//
//                                },
//                            ),
//                        leadingContent = {
//                            Text(
//                                text = countryPickerViewModel.getCountryList[date].dial_code,
//                                fontSize = 15.sp,
//                                fontWeight = FontWeight.Light
//                            )
//                        },
//                        headlineContent = {
//                            Text(
//                                text = countryPickerViewModel.getCountryList[date].name,
//                                fontSize = 15.sp,
//                                fontWeight = FontWeight.Light
//
//                            )
//                        },
//                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
//                    )
//                    ViewDivider()
//                }
//            }
        }
    }
}

@Composable
fun SearchResults(query: String,viewModel: ViewModel) {
    val countryPickerViewModel: CountryPickerViewModel = hiltViewModel()

    val filteredList = remember(query) {
        countryPickerViewModel.getCountryList.filter { pair ->
            pair.name.contains(query,ignoreCase = true)
        }
    }

    // Display the filtered results
    LazyColumn {
        items(filteredList.size) { pair ->
            Column {
                Text(
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable(
                            onClick = {
                                countryPickerViewModel.setSelectedCountryDialCodeValue(
                                    filteredList[pair].dial_code
                                )
                                when (viewModel) {
                                    is SignUpViewModel -> viewModel.setShowCountryPickerValue(
                                        false
                                    )

                                    is LoginWithPhoneViewModel -> {
                                        viewModel.setShowCountryPickerValue(
                                            false
                                        )
                                        countryPickerViewModel.setSelectedCountryValue(
                                            filteredList[pair].name
                                        )
                                    }
                                }

                            },
                        ),text = "${filteredList[pair].dial_code}: ${filteredList[pair].name}"
                )
                ViewDivider()
            }
//            ListItem(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(15.dp))
//                    .clickable(
//                        onClick = {
//                            countryPickerViewModel.setSelectedCountryDialCodeValue(
//                                countryPickerViewModel.getCountryList[pair].dial_code
//                            )
//                            when (viewModel) {
//                                is SignUpViewModel -> viewModel.setShowCountryPickerValue(
//                                    false
//                                )
//
//                                is LoginWithPhoneViewModel -> {
//                                    viewModel.setShowCountryPickerValue(
//                                        false
//                                    )
//                                    countryPickerViewModel.setSelectedCountryValue(
//                                        countryPickerViewModel.getCountryList[pair].name
//                                    )
//                                }
//                            }
//
//                        },
//                    ),
//                leadingContent = {
//                    Text(
//                        text = countryPickerViewModel.getCountryList[pair].dial_code,
//                        fontSize = 15.sp,
//                        fontWeight = FontWeight.Light
//                    )
//                },
//                headlineContent = {
//                    Text(
//                        text = countryPickerViewModel.getCountryList[pair].name,
//                        fontSize = 15.sp,
//                        fontWeight = FontWeight.Light
//
//                    )
//                },
//                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
//            )
        }
    }
}

//@Composable
//fun MyApp() {
//    var searchQuery by remember { mutableStateOf("") }
//
//    Column {
//        // Search input field
//        TextField(value = searchQuery,
//            onValueChange = { searchQuery = it },
//            label = { Text("Search") })
//
//        // Display the search results
//        SearchResults(query = searchQuery)
//    }
//}




