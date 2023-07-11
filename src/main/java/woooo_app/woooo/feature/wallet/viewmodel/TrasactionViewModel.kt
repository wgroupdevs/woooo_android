package com.wgroup.woooo_app.woooo.feature.wallet.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import java.io.IOException
import java.nio.charset.Charset
import javax.inject.Inject

//
//import androidx.compose.runtime.State
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.snapshots.SnapshotStateList
//import androidx.lifecycle.ViewModel
//import com.wgroup.woooo_app.woooo.feature.wallet.data.models.Trasactions_Model
//import javax.inject.Inject
//
//class TrasactionViewModel @Inject constructor() : ViewModel() {
//
//    private val list = mutableStateListOf<Trasactions_Model>()
//        val getList : SnapshotStateList<Trasactions_Model> = list
//    fun addItem(item: Trasactions_Model) {
//        list.add(item)
//    }
//}
