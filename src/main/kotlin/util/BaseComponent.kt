package util

abstract class BaseComponent<T : ScreenViewModel> {
    lateinit var viewModel: T
    abstract fun getVM(): T
}