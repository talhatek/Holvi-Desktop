package screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import nav.NavHostComponent
import util.*


class MenuScreenComponent(
    private val componentContext: ComponentContext,
    private val navigate: (screen: NavHostComponent.ScreenConfig) -> Unit

) : Component, ComponentContext by componentContext {
    @Composable
    override fun render() {
        menuScreen(listOf("Add", "All", "Delete", "Generate"), navigate)
    }
}

@Composable
@Preview
fun menuScreen(menuItems: List<String>, navigate: (screen: NavHostComponent.ScreenConfig) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth().background(BlackColor),
        horizontalAlignment = CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        Text(
            text = "What you wanna do?",
            style = MaterialTheme.typography.h3,
            color = WhiteColor
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        BoxWithConstraints {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = CenterHorizontally,
                contentPadding = PaddingValues(vertical = 16.dp)

            ) {
                items(menuItems) { menuItem ->
                    menuItem(title = menuItem) { selectedTitle ->
                        navigate.invoke(selectedTitle.convertToScreenConfig())
                    }
                }


            }
        }
    }
}

@Composable
fun menuItem(
    title: String,
    onClicked: (title: String) -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth(0.7f),
        onClick = { onClicked.invoke(title) },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryColor)
    ) {
        Text(text = title, style = MaterialTheme.typography.body2, modifier = Modifier.padding(all = 8.dp))
    }


}