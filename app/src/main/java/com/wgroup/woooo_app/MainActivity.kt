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
import java.io.InputStreamReader

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
                    readJsonFile(local)

                    Log.d("","")
                })) {
//                CountryCodePicker()

            }
        }
    }
}

data class MyData(val name: String,val dial_code: String,val code: String)

fun readJsonFile(context: Context): MyData? {
    val inputStream = context.resources.openRawResource(R.raw.countrycodes)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val jsonString = StringBuilder()
    var line: String? = reader.readLine()
    while (line != null) {
        jsonString.append(line)
        line = reader.readLine()
    }
    reader.close()
    Log.d(line,"Readed file Data")

    return Gson().fromJson(jsonString.toString(),MyData::class.java)
}