package com.wgroup.woooo_app.woooo.shared.components.view_models

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.wgroup.woooo_app.woooo.shared.data.models.CountriesList
import com.wgroup.woooo_app.woooo.shared.data.models.CountryListModel
import java.io.IOException
import java.nio.charset.Charset
import javax.inject.Inject

class CountryPickerViewModel @Inject constructor() : ViewModel() {

    private val _setValueOfList = mutableStateListOf<CountriesList>()
    var getCountryList: MutableList<CountriesList> = _setValueOfList
    fun addItem(value: List<CountriesList>) {
        _setValueOfList.addAll(value)
    }

    fun readJsonFileFromAssets(context: Context): String? {
        _setValueOfList.clear()
        var jsonString: String?
        try {
            val inputStream = context.assets.open("countrycodes.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            jsonString = String(buffer,Charset.defaultCharset())
            val data = Gson().fromJson(jsonString.toString(),CountryListModel::class.java)
//            getCountryList = data.countries
            addItem(data.countries)
            Log.d("${getCountryList.size}","wewererwe")

        } catch (e: IOException) {
            Log.e("ReadJsonFileFromAssets","Error reading JSON file",e)
            jsonString = null
        }
        return jsonString
    }
}