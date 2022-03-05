package util

import java.io.File
import java.nio.file.Path

class FileChooser {

    object Default {
        val keepSafeFile by lazy { File(Path.of("app\\resources").toAbsolutePath().toString() + "\\keep_safe.yaml") }
        val keyFile by lazy { File(Path.of("app\\resources").toAbsolutePath().toString() + "\\key.yaml") }
        val createdFile by lazy { File(Path.of("").toAbsolutePath().toString() + "\\app\\resources") }
    }
}