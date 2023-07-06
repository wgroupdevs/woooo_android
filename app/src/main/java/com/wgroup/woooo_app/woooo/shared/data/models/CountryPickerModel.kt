package com.wgroup.woooo_app.woooo.shared.data.models

data class CountryListModel(
    val countries: List<CountriesList>
)

data class CountriesList(
    val code: String,
    val dial_code: String,
    val name: String
)