package screen

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import util.BaseViewModelComponent
import util.Component
import view_model.AllViewModel

class AllScreenViewModelComponent(componentContext: ComponentContext, private val onBackPresses: () -> Unit) :
    BaseViewModelComponent<AllViewModel>(AllViewModel::class.java, componentContext), Component,
    ComponentContext by componentContext {

    @Composable
    override fun render() {

    }
}