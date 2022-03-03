package util

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle

abstract class BaseViewModelComponent<T : ScreenViewModel>(vm : Class<T>, componentContext: ComponentContext) {
    var viewModel: T = vm.getDeclaredConstructor().newInstance()

    init {
        componentContext.lifecycle.subscribe(object : Lifecycle.Callbacks {
            override fun onDestroy() {
                viewModel.onDestroy()
                println(viewModel::class.simpleName + "destroyed")
                super.onDestroy()
            }

            override fun onCreate() {
                println(viewModel::class.simpleName + "created")
                super.onCreate()
            }
        })
    }

}