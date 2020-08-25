package nl.blackstardlb.GumboSoup

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
        var workingDir = this.getWorkingDir()
        val segments = workingDir.split("/")
        if (segments.lastOrNull() == "debugTest") {
            workingDir = segments.subList(0, segments.size - 4).joinToString("/")
        }
        workingDir = workingDir + "/src/commonTest/resources/"
        return workingDir
    }

    private fun getWorkingDir(): String {
        val path = ByteArray(PATH_MAX)
        path.usePinned {
            getcwd(it.addressOf(0), PATH_MAX)
        }
        return path.toKString()
    }
}
