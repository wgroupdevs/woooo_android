package com.wgroup.woooo_app

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.wgroup.woooo_app.woooo.theme.Woooo_androidTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    var local = LocalContext.current
    Woooo_androidTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
//            DestinationsNavHost(navGraph = NavGraphs.root)

//            var openDialog by remember {
//                mutableStateOf(true) // Initially dialog is closed
//            }
//
//            ButtonClick(buttonText = "Open Dialog") {
//                openDialog = true
//            }
//
//            if (openDialog) {
//                CustomAlertDialogue {
//                    openDialog = false
//                }
//            }

            Box(modifier = Modifier
                .height(300.dp)
                .clickable(onClick = {
                    readJsonFileFromAssets(local)

                    Log.d("", "")
                })
            ) {
//                CountryCodePicker()

            }
        }
    }
}


fun readJsonFileFromAssets(context: Context): String? {
    var jsonString: String?
    try {
        val inputStream = context.assets.open("countrycodes.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        jsonString = String(buffer, Charset.defaultCharset())
        Log.d("ReadJsonFileFromAssets", jsonString)

        val data = Gson().fromJson(jsonString.toString(), CountryModel::class.java)

        Log.d("ReadJsonFileFromAssets", "${data.countries.size}")
        data.countries.forEach {
            Log.d("ReadJsonFileFromAssets", it.name)

        }

    } catch (e: IOException) {
        Log.e("ReadJsonFileFromAssets", "Error reading JSON file", e)
        jsonString = null
    }
    return jsonString
}

data class MyData(val name: String, val dial_code: String, val code: String)
//
//fun readJsonFile(context: Context): MyData? {
//    val inputStream = context.resources.openRawResource(R.raw.countrycodes)
//    val reader = BufferedReader(InputStreamReader(inputStream))
//    val jsonString = StringBuilder()
//    var line: String? = reader.readLine()
//    while (line != null) {
//        jsonString.append(line)
//        line = reader.readLine()
//    }
//    reader.close()
//    Log.d(line,"Readed file Data")
//
//    return Gson().fromJson(jsonString.toString(),MyData::class.java)
//}

data class CountryModel(
    val countries: List<Country>
)

data class Country(
    val code: String,
    val dial_code: String,
    val name: String
)