package nav

import GreetingScreenComponent
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.replaceCurrent
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import screen.AuthScreenComponent
import screen.MenuScreenComponent
import util.Component

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

            is ScreenConfig.Auth -> AuthScreenComponent(
                componentContext,
                ::onAuthenticated,
            )

            is ScreenConfig.Menu -> MenuScreenComponent(
                componentContext,
                ::push
            )
            else -> {
                //todo
                AuthScreenComponent(
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


    private fun pop() {
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