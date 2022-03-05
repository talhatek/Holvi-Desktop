package screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import model.Site
import util.*
import view_model.AddPasswordState
import view_model.AddViewModel

class AddScreenComponent(componentContext: ComponentContext, val onBackPress: () -> Unit) :
    BaseViewModelComponent<AddViewModel>(AddViewModel::class.java, componentContext), Component,
    ComponentContext by componentContext {
    @Composable
    override fun render() {
        AddScreen(viewModel, onBackPress)
    }

}

@Composable
fun AddScreen(viewModel: AddViewModel, onBackPress: () -> Unit) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var siteName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    LaunchedEffect(key1 = true) {
        viewModel.passwordAddState.collect {
            when (it) {
                is AddPasswordState.Success -> {
                    scaffoldState.snackbarHostState.showSnackbar("Password added successfully.")
                }
                is AddPasswordState.Failure -> {
                    scaffoldState.snackbarHostState.showSnackbar(it.message)
                }
                else -> Unit
            }
        }
    }
    Scaffold(
        backgroundColor = BlackColor,
        topBar = {
            TopAppBarBackWithLogo {
                viewModel.clearPassword()
                onBackPress.invoke()
            }
        },
        bottomBar = {
            BottomButton("Add") {
                scope.launch(Dispatchers.IO) {
                    viewModel.addPassword(
                        Site(
                            siteName = siteName,
                            password = password,
                            userName = userName
                        )
                    )
                }

            }
        },

        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.8f).background(BlackColor),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputView(viewModel = viewModel, hintParam = "Site Name") {
                    siteName = it
                }

                InputView(viewModel, hintParam = "User Name") {
                    userName = it

                }
                PasswordInputView(viewModel, hintParam = "Password") {
                    password = it
                }
                CircleTextButton(text = "G", percentage = 10) {
                    password = viewModel.generatePassword()
                }
            }
        },
        scaffoldState = scaffoldState
    )
}


@Composable
fun InputView(viewModel: AddViewModel?, hintParam: String, onValueChanged: (input: String) -> Unit) {
    var value by remember { mutableStateOf("") }
    var hint by remember { mutableStateOf(hintParam) }
    LaunchedEffect(key1 = true, block = {
        viewModel?.clearInputsSharedFlow?.collectLatest {
            if (it == 1) {
                value = ""

            }
        }
    })
    TextField(
        value = value,
        onValueChange = {
            value = it
            onValueChanged.invoke(it)
        },
        placeholder = {
            Text(
                text = hint,
                modifier = Modifier
                    .alpha(.5f)
                    .background(TransparentColor)
                    .fillMaxWidth(),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,

                    ),
                color = WhiteColor
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = TransparentColor,
            focusedIndicatorColor = WhiteColor,
            unfocusedIndicatorColor = WhiteColor
        ),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        modifier = Modifier
            .fillMaxWidth(.7f)
            .onFocusEvent {
                if (it.isFocused) {
                    if (value.isEmpty())
                        hint = ""
                } else
                    if (value.isEmpty())
                        hint = hintParam
            },
        singleLine = true
    )

}


@Composable
fun PasswordInputView(
    viewModel: AddViewModel?,
    hintParam: String,
    onValueChanged: (input: String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var value by remember { mutableStateOf("") }
    var hint by remember { mutableStateOf(hintParam) }

    LaunchedEffect(key1 = true, block = {
        viewModel?.clearInputsSharedFlow?.collectLatest {
            if (it == 1) {
                value = ""
                focusManager.clearFocus()
            }
        }
    })
    LaunchedEffect(key1 = true, block = {
        viewModel?.passwordStateFlow?.collectLatest {
            if (it.isNotEmpty()) {
                value = it
                focusManager.clearFocus()
            }
        }
    })


    TextField(
        value = value,
        onValueChange = {
            value = it
            onValueChanged.invoke(it)
        },
        placeholder = {
            Text(
                text = hint,
                modifier = Modifier
                    .alpha(.5f)
                    .background(TransparentColor)
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,

                    ),
                color = WhiteColor
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = TransparentColor,
            focusedIndicatorColor = WhiteColor,
            unfocusedIndicatorColor = WhiteColor
        ),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        modifier = Modifier
            .fillMaxWidth(.7f)
            .onFocusEvent {
                if (it.isFocused) {
                    if (value.isEmpty())
                        hint = ""
                } else
                    if (value.isEmpty())
                        hint = hintParam
            },
        singleLine = true
    )

}