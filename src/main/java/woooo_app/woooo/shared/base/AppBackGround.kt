package woooo_app.woooo.shared.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wgroup.woooo_app.woooo.theme.WooColor
import woooo_app.woooo.utils.Dimension

@Composable
fun AppBackGround(content: @Composable (ColumnScope.() -> Unit)) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WooColor.backgroundColor)
            .padding(Dimension.dimen_10),content = content
    )
}