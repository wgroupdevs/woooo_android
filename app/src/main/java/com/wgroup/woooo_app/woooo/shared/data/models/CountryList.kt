package com.wgroup.woooo_app.woooo.shared.data.models

data class CountryList(
    val countries: List<Country>
)

data class Country(
    val code: String,
    val dial_code: String,
    val name: String
)