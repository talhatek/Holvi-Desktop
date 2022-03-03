package view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.encodeToString
import model.User
import util.ScreenViewModel
import util.decodeFromStream
import util.decrypt
import util.encrypt
import java.io.File
import java.io.FileWriter
import java.nio.file.Path

class AuthViewModel : ScreenViewModel() {
    var value by mutableStateOf("")
    private val _authState = MutableSharedFlow<AuthState>()
    val authState = _authState.asSharedFlow()

    @OptIn(ExperimentalComposeUiApi::class)
    fun auth() {
        launch(Dispatchers.IO) {
            val file = File(Path.of("app\\resources").toAbsolutePath().toString() + "\\keep_safe.yaml")
            if (file.isFile) {
                val data = Yaml.default.decodeFromStream<User>(file.inputStream())
                try {
                    if (data.value.decrypt(value).toInt() == value.toInt())
                        _authState.emit(AuthState.SuccessState)
                    else
                        _authState.emit(AuthState.ErrorState)
                } catch (ex: Exception) {
                    _authState.emit(AuthState.ErrorState)

                }
            } else {
                val newFile = File(Path.of("").toAbsolutePath().toString() + "\\app\\resources").mkdirs()
                if (newFile) {
                    val data = Yaml.default.encodeToString(User(value = value.encrypt(value), sites = listOf()))
                    val fileWriter = FileWriter(file)
                    fileWriter.write(data)
                    fileWriter.close()
                    _authState.emit(AuthState.SuccessState)
                } else
                    _authState.emit(AuthState.ErrorState)

            }
        }
    }
}

sealed class AuthState {
    object SuccessState : AuthState()
    object ErrorState : AuthState()
}