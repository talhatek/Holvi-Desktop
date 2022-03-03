package util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ScreenViewModel : CoroutineScope {
    private val supervisorJob = SupervisorJob()
    override val coroutineContext = Dispatchers.IO + supervisorJob

    open fun onDestroy() {
        supervisorJob.cancel()
    }

}