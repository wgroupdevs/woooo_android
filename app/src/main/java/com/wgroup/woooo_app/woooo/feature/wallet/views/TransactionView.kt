package com.wgroup.woooo_app.woooo.feature.wallet.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.R
import com.wgroup.woooo_app.woooo.shared.components.CustomDivider
import com.wgroup.woooo_app.woooo.shared.components.TopBarForSetting
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun TransactionsView(navigator: DestinationsNavigator) {

    data class DataClass(
        val name: String = "Danish",val img: Int,val title: String,val titlee: String
    )

    fun inputData(): MutableList<DataClass> {
        val list = mutableListOf<DataClass>()

        list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )

         list.add(
            DataClass(
                name = "10-Dec-20",img = R.drawable.app_logo,title = "Login",titlee = "0.004"
            )
        )


        return list
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        TopBarForSetting(onBackPressed = { navigator.popBackStack() })
        VerticalSpacer(Dimension.dimen_5)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(text = "dr12@gmail.com")
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Rounded.DateRange,
                    contentDescription = "",
                    tint = WooColor.white,
                )
                Text(text = "Last 30 Days",fontSize = 13.sp)
            }
        }
        VerticalSpacer()
        CustomDivider(false)
        VerticalSpacer()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = Strings.rcntTransText)
            Icon(imageVector = Icons.Rounded.Refresh,contentDescription = "",tint = WooColor.white)
        }
        VerticalSpacer(Dimension.dimen_20)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Date",fontSize = 14.sp)
            Text(text = "Currency",fontSize = 14.sp)
            Text(text = "Type",fontSize = 14.sp)
            Text(text = "Amount",fontSize = 14.sp)

        }
        LazyColumn(content = {
            items(inputData()) { item ->
                History(img = item.img,name = item.name,title = item.title,titlee = item.titlee)
            }
        })
    }

}

@Composable
fun History(name: String,img: Int,title: String,titlee: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = name,fontSize = 14.sp)
        Image(
            painter = painterResource(id = img),
            contentDescription = "",
            modifier = Modifier.size(30.dp)
        )
        Text(text = title,fontSize = 14.sp)
        Text(text = titlee,fontSize = 14.sp)
    }
}