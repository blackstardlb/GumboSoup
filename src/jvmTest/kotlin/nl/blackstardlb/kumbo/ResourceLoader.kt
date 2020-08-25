package nl.blackstardlb.GumboSoup

actual class ResourceLoader actual constructor() {
    actual fun loadResourceAsString(resourceName: String): String {
        return ResourceLoader::class.java.getResourceAsStream("/$resourceName").bufferedReader().readText()
    }
}
