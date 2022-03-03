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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import kotlinx.coroutines.flow.collectLatest
import util.BaseComponent
import util.Component
import view_model.AuthState
import view_model.AuthViewModel


class AuthScreenComponent(
    private val componentContext: ComponentContext,
    private val onAuthenticated: () -> Unit
) : BaseComponent<AuthViewModel>(), Component,
    ComponentContext by componentContext {

    init {
        viewModel = getVM()
        componentContext.lifecycle.subscribe(object : Lifecycle.Callbacks {
            override fun onDestroy() {
                viewModel.onDestroy()
                println(viewModel::class.simpleName + "destroyed")
                super.onDestroy()
            }

            override fun onCreate() {
                println(viewModel::class.simpleName + "created")
                super.onCreate()
            }
        })

    }

    @Composable
    override fun render() {
        authScreen(onAuthenticated, viewModel)
    }

    override fun getVM(): AuthViewModel {
        return AuthViewModel()
    }


}

@Composable
fun authScreen(
    onAuthenticated: () -> Unit,
    viewModel: AuthViewModel
) {

    LaunchedEffect(key1 = true) {
        viewModel.authState.collectLatest {
            when (it) {
                is AuthState.SuccessState -> {
                    onAuthenticated.invoke()
                }
                is AuthState.ErrorState -> {

                }

            }

        }
    }
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize().background(Color.Black),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Welcome Back!", color = Color.White, style = MaterialTheme.typography.h4)
            Text(
                text = "Please authenticate to continue...",

                color = Color.White,
                style = MaterialTheme.typography.body1
            )
            TextField(
                value = viewModel.value, onValueChange = { viewModel.value = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    textColor = Color.White,
                    placeholderColor = Color.White,
                    cursorColor = Color.White
                )
            )
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6FCF97)), onClick = {
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
                tint = Color.White
            )
        }
    }

}
