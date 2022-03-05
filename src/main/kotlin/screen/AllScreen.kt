package screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import model.Site
import util.*
import view_model.AllSitesState
import view_model.AllViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch

class AllScreenViewModelComponent(componentContext: ComponentContext, private val onBackPresses: () -> Unit) :
    BaseViewModelComponent<AllViewModel>(AllViewModel::class.java, componentContext), Component,
    ComponentContext by componentContext {

    @Composable
    override fun render() {
        allScreen(viewModel, onBackPresses)
    }
}

@Composable
fun allScreen(viewModel: AllViewModel, onBackPresses: () -> Unit) {
    val data = viewModel.allSites.collectAsState().value
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        backgroundColor = ScaffoldBackGround,
        topBar = {
            TopAppBarBackWithLogo {
                onBackPresses.invoke()
            }
        }, scaffoldState = scaffoldState
    ) {
        when (data) {
            is AllSitesState.SuccessState -> {
                if (data.data.isEmpty()) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().background(BlackColor)) {
                        Text(text = "You don't have any saved password.", color = WhiteColor)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        content = {
                            items(data.data) { item ->
                                PasswordItem(item, data.data.indexOf(item))
                            }
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterVertically
                        ),
                        contentPadding = PaddingValues(top = 16.dp)

                    )
                }
            }
            is AllSitesState.ErrorState -> {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("Error occurred!")
                }
            }
            else -> Unit
        }

    }
}

@Composable
fun PasswordItem(password: Site, position: Int) {
    var passwordText by remember { mutableStateOf("*".repeat(password.password.length)) }
    var resId by remember { mutableStateOf("ic_invisible.png") }
    var visible by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth(.6f),
        elevation = 12.dp,
        backgroundColor = if (position % 2 == 0) SecondaryColor else PrimaryColor
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = password.siteName,
                style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                ),
                color = WhiteColor
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(.7f)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(.4f),
                        text = passwordText,
                        style = TextStyle(
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Normal

                        ),
                        color = WhiteColor
                    )
                    Spacer(Modifier.fillMaxWidth(.1f))
                    IconButton(
                        onClick = {
                            if (visible) {
                                passwordText = "*".repeat(password.password.length)
                                resId = "ic_invisible.png"
                            } else {
                                passwordText = password.password
                                resId = "ic_visible.png"
                            }
                            visible = !visible

                        },
                        enabled = true,
                        modifier = Modifier.fillMaxWidth(.2f)
                    ) {
                        Icon(
                            painter = painterResource(resId),
                            contentDescription = "hiddenOrShown",
                            tint = WhiteColor
                        )
                    }

                }
            }
            Text(
                text = password.userName,
                style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                ),
                color = Color.White
            )

        }
    }

}

