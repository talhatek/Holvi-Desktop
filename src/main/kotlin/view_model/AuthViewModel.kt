package view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import model.Key
import model.User
import util.*
import java.io.FileWriter

class AuthViewModel : ScreenViewModel() {
    var value by mutableStateOf("")
    private val _authState = MutableSharedFlow<AuthState>()
    val authState = _authState.asSharedFlow()

    @OptIn(ExperimentalComposeUiApi::class)
    fun auth() {
        launch(Dispatchers.IO) {
            if (FileChooser.Default.keepSafeFile.isFile) {
                val data = Yaml.default.decodeFromStream<User>(FileChooser.Default.keepSafeFile.inputStream())
                try {
                    if (data.value.decrypt(value).toInt() == value.toInt()){
                        _authState.emit(AuthState.SuccessState)
                        saveKey()
                    }
                    else
                        _authState.emit(AuthState.ErrorState)
                } catch (ex: Exception) {
                    _authState.emit(AuthState.ErrorState)

                }
            } else {
                if (FileChooser.Default.createdFile.mkdirs()) {
                    val data = Yaml.default.encodeToString(User(value = value.encrypt(value), sites = listOf()))
                    val fileWriter = FileWriter(FileChooser.Default.keepSafeFile)
                    fileWriter.write(data)
                    fileWriter.close()
                    _authState.emit(AuthState.SuccessState)
                    saveKey()
                } else
                    _authState.emit(AuthState.ErrorState)

            }
        }
    }

  private fun saveKey() {
      launch(Dispatchers.IO) {
           val fileKeyWriter= FileWriter(FileChooser.Default.keyFile)
            fileKeyWriter.write(Yaml.default.encodeToString(Key(value)))
            fileKeyWriter.close()
        }.start()

    }
}

sealed class AuthState {
    object SuccessState : AuthState()
    object ErrorState : AuthState()
}