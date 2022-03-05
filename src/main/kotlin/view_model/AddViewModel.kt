package view_model

import androidx.compose.material.darkColors
import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import model.Key
import model.Site
import model.User
import util.*
import java.io.FileWriter

class AddViewModel : ScreenViewModel() {
    private val _clearInputsSharedFlow = MutableSharedFlow<Int>()
    val clearInputsSharedFlow = _clearInputsSharedFlow.asSharedFlow()
    private val password = MutableStateFlow("")
    val passwordStateFlow = password.asStateFlow()
    private val _passwordAddState = MutableSharedFlow<AddPasswordState>()
    val passwordAddState = _passwordAddState.asSharedFlow()
    private val passwordManager = PasswordManager()
    fun addPassword(site: Site) {
        launch(Dispatchers.IO) {
            val controlPassword = controlPassword(site = site)
            if (controlPassword) {
                try {
                    addSiteToYaml(site = site)
                    _passwordAddState.emit(AddPasswordState.Success)
                    _clearInputsSharedFlow.emit(1)
                } catch (ex: Exception) {
                    _passwordAddState.emit(AddPasswordState.Failure("Password could not added."))
                }
            } else
                _passwordAddState.emit(AddPasswordState.Failure("You must fill required fields."))
        }
    }

    private fun addSiteToYaml(site: Site) {
        val data = Yaml.default.decodeFromStream<User>(FileChooser.Default.keepSafeFile.inputStream())
        val newYaml = data.addSite(site, getKey())
        val encodedString = Yaml.default.encodeToString(User(value = data.value, newYaml.sites))
        val fileWriter = FileWriter(FileChooser.Default.keepSafeFile)
        fileWriter.write(encodedString)
        fileWriter.close()
    }

    private fun getKey(): String {
        val data = Yaml.default.decodeFromStream<Key>(FileChooser.Default.keyFile.inputStream())
        return data.secretKey

    }

    fun clearPassword() {
        launch {
            password.emit("")
        }
    }

    private fun controlPassword(site: Site): Boolean {
        return !(site.password.isBlank() || site.siteName.isBlank() || site.userName.isBlank())
    }

    fun generatePassword(): String {
        val data = passwordManager.generatePassword(length = 8)
        launch {
            password.emit(data)
        }
        return data
    }
}


sealed class AddPasswordState {
    object Success : AddPasswordState()
    class Failure(val message: String) : AddPasswordState()
    object Empty : AddPasswordState()
}
