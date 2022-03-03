package screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.collectLatest
import util.*
import view_model.AuthState
import view_model.AuthViewModel

class AuthScreenViewModelComponent(
    private val componentContext: ComponentContext,
    private val onAuthenticated: () -> Unit
) : BaseViewModelComponent<AuthViewModel>(AuthViewModel::class.java,componentContext), Component,
    ComponentContext by componentContext {

    @Composable
    override fun render() {
        authScreen(onAuthenticated, viewModel)
    }

}

@Composable
fun authScreen(
    onAuthenticated: () -> Unit,
    viewModel: AuthViewModel
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.authState.collectLatest {
            when (it) {
                is AuthState.SuccessState -> {
                    onAuthenticated.invoke()
                }
                is AuthState.ErrorState -> {
                    scaffoldState.snackbarHostState.showSnackbar("Are you sure about your password ?")
                }

            }

        }
    }
    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize().background(BlackColor),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Welcome Back!", color = WhiteColor, style = MaterialTheme.typography.h4)
            Text(
                text = "Please authenticate to continue...",
                color = WhiteColor,
                style = MaterialTheme.typography.body1
            )
            TextField(
                value = viewModel.value, onValueChange = { viewModel.value = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = TransparentColor,
                    focusedIndicatorColor = WhiteColor,
                    unfocusedIndicatorColor = WhiteColor,
                    textColor = WhiteColor,
                    placeholderColor = WhiteColor,
                    cursorColor = WhiteColor
                )
            )
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryColor), onClick = {
                    viewModel.auth()
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Authenticate",
                    Modifier.padding(4.dp),
                    style = MaterialTheme.typography.body1
                )
            }
            Icon(
                painter = painterResource("icon_lock.png"),
                contentDescription = "lock",
                tint = WhiteColor
            )
        }
    }

}
