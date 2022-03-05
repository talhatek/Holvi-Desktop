import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import nav.NavHostComponent
import util.FileChooser

@OptIn(ExperimentalDecomposeApi::class)
fun main() = application {
    val lifecycle = LifecycleRegistry()
    val root = NavHostComponent(DefaultComponentContext(lifecycle))
    val windowState = rememberWindowState()
    LifecycleController(lifecycle, windowState)
    Window(
        onCloseRequest = { FileChooser.Default.keyFile.delete(); ::exitApplication.invoke() },
        title = "Holvi",
        icon = painterResource("ic_holvi_icon.png"),
        resizable = false
    ) {
        root.render()
    }
}
