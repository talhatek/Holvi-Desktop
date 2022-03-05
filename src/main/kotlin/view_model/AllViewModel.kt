package view_model

import androidx.compose.runtime.MutableState
import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import model.Key
import model.Site
import model.User
import nav.NavHostComponent
import util.*

class AllViewModel : ScreenViewModel() {
    private val _allSites = MutableStateFlow<AllSitesState>(AllSitesState.EmptyState)
    val allSites = _allSites.asStateFlow()

    init {
        fetch()
    }

    private fun fetch() {
        launch(Dispatchers.IO) {
            try {
                _allSites.emit(AllSitesState.LoadingState)
                val data = Yaml.default.decodeFromStream<User>(FileChooser.Default.keepSafeFile.inputStream())
                val key = Yaml.default.decodeFromStream<Key>(FileChooser.Default.keyFile.inputStream())
                data.sites.map {
                    it.password = it.password.decrypt(key.secretKey)
                    it.userName = it.userName.decrypt(key.secretKey)
                }
                _allSites.emit(AllSitesState.SuccessState(data.sites))
            }
            catch (ex:Exception){
                _allSites.emit(AllSitesState.ErrorState)
            }
        }
    }
}

sealed class AllSitesState {
    class SuccessState(val data: List<Site>) : AllSitesState()
    object ErrorState : AllSitesState()
    object LoadingState : AllSitesState()
    object EmptyState : AllSitesState()
}