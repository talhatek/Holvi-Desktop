package nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.application
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.replaceCurrent
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import com.sun.tools.javac.Main
import kotlinx.coroutines.MainScope
import screen.AddScreenComponent
import screen.AllScreenViewModelComponent
import screen.AuthScreenViewModelComponent
import screen.MenuScreenComponent
import util.Component
import java.awt.Window

class NavHostComponent(
    componentContext: ComponentContext
) : Component, ComponentContext by componentContext {

    private val router = router<ScreenConfig, Component>(
        initialConfiguration = ScreenConfig.Auth,
        childFactory = ::createScreenComponent,
        handleBackButton = true
    )

    private fun createScreenComponent(
        screenConfig: ScreenConfig,
        componentContext: ComponentContext
    ): Component {
        return when (screenConfig) {

            is ScreenConfig.Auth -> AuthScreenViewModelComponent(
                componentContext,
                ::onAuthenticated,
            )

            is ScreenConfig.Menu -> MenuScreenComponent(
                componentContext,
                ::push
            )
            is ScreenConfig.All -> AllScreenViewModelComponent(
                componentContext,
                ::onBackPress
            )
            is ScreenConfig.Add -> AddScreenComponent(
                componentContext,
                ::onBackPress
            )
            else -> {
                //todo
                AuthScreenViewModelComponent(
                    componentContext,
                    ::onAuthenticated,
                )
            }
        }
    }

    private fun onAuthenticated() {
        router.replaceCurrent(ScreenConfig.Menu)
    }

    private fun push(screen: ScreenConfig) {
        router.push(screen)
    }

    private fun onBackPress() {
        router.pop()
    }

    @Composable
    override fun render() {
        Children(routerState = router.state) {
            it.instance.render()
        }
    }

    sealed class ScreenConfig : Parcelable {
        object Auth : ScreenConfig()
        object Menu : ScreenConfig()
        object Add : ScreenConfig()
        object Delete : ScreenConfig()
        object All : ScreenConfig()
        object Generate : ScreenConfig()
    }
}