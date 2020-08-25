package nl.blackstardlb.GumboSoup

expect class ResourceLoader() {
    fun loadResourceAsString(resourceName: String): String
}
