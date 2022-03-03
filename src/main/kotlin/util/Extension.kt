package util

import androidx.compose.ui.graphics.Color
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.serializer
import nav.NavHostComponent
import org.jasypt.util.text.AES256TextEncryptor
import java.io.InputStream
import java.io.OutputStream

inline fun <reified T> Yaml.decodeFromStream(stream: InputStream): T =
    decodeFromStream(serializersModule.serializer(), stream)

inline fun <reified T> Yaml.encodeToStream(value: T, stream: OutputStream) =
    encodeToStream(serializersModule.serializer(), value, stream)

fun String.encrypt(key: String): String {
    val enc = AES256TextEncryptor()
    enc.setPassword(key)
    return enc.encrypt(this)
}

fun String.decrypt(key: String): String {
    val dec = AES256TextEncryptor()
    dec.setPassword(key)
    return dec.decrypt(this)
}

fun String.convertToScreenConfig():NavHostComponent.ScreenConfig{
  return  when(this){
        "Add" ->  NavHostComponent.ScreenConfig.Add
        "All" ->  NavHostComponent.ScreenConfig.All
        "Delete" ->  NavHostComponent.ScreenConfig.Delete
        "generate" ->  NavHostComponent.ScreenConfig.Generate
        else ->  throw Exception("Unknown screen config")
  }
}

val PrimaryColor = Color(0xFF6FCF97)
val WhiteColor = Color.White
val BlackColor = Color.Black
val TransparentColor = Color.Transparent