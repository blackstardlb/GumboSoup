package nl.blackstardlb.gumbosoup

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.*

actual class ResourceLoader actual constructor() {
    actual fun loadResourceAsString(resourceName: String): String {
        val fileName = "${getResourceDir()}$resourceName"
        println("Loading file: $fileName")
        val f = fopen(fileName, "rb")
        fseek(f, 0, SEEK_END)
        val fsize = ftell(f).toULong()
        rewind(f)

        val buff = ByteArray(fsize.toInt())
        buff.usePinned {
            val result = fread(it.addressOf(0), 1, fsize, f)
            if (result != fsize) {
                throw Exception("Reading error")
            }
        }
        fclose(f)
        return buff.toKString()
    }

    private fun getResourceDir(): String {
        return getenv("RESOURCES_FOLDER")!!.toKString()
    }
}
