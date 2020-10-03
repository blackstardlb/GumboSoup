package nl.blackstardlb.gumbosoup

expect class ResourceLoader() {
    fun loadResourceAsString(resourceName: String): String
}
