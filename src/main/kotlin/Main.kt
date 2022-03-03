// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import nav.NavHostComponent
import util.Component
import kotlin.system.exitProcess


@OptIn(ExperimentalDecomposeApi::class)
fun main() = application {
    val lifecycle = LifecycleRegistry()
    val root = NavHostComponent(DefaultComponentContext(lifecycle))
    val windowState = rememberWindowState()
    LifecycleController(lifecycle, windowState)
    Window(
        onCloseRequest = ::exitApplication,
        title = "Holvi",
        icon = painterResource("ic_holvi_icon.png"),
        resizable = false
    ) {
        root.render()
    }
}




class GreetingScreenComponent(
    private val componentContext: ComponentContext,
    private val name: String,
    private val onGoBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    companion object {
        private val greetings = listOf(
            "Bonjour",
            "Hola",
            "Olá",
            "Ciao",
            "Hi",
            "Hallo",
            "Hey"
        )
    }

    @Composable
    override fun render() {
        GreetingScreen(
            greeting = "${greetings.random()}, $name",
            onGoBackClicked = onGoBackClicked,
            componentContext
        )
    }
}

@Composable
fun GreetingScreen(
    greeting: String,
    onGoBackClicked: () -> Unit,
    componentContext: ComponentContext
) {
    componentContext.lifecycle.subscribe(object : Lifecycle.Callbacks {
        override fun onDestroy() {
            println("onDestroyGreetingScreen")
            super.onDestroy()
        }

        override fun onCreate() {
            println("onCreateGreetingScreen")
            super.onCreate()
        }
    })
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Greeting
        Text(
            text = greeting,
            fontSize = 40.sp
        )

        // Spacing between text and button
        Spacer(modifier = Modifier.height(30.dp))

        // Go back button
        Button(onClick = {
        }) {
            Text(text = "GO BACK!")
        }
    }
}

