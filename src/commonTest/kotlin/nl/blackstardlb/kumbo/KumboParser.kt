package nl.blackstardlb.GumboSoup

import kotlin.test.Test

class GumboSoupParserTest {
    private val resourceLoader: ResourceLoader = ResourceLoader()

    @Test
    fun test() {
        val html = resourceLoader.loadResourceAsString("signin.html")
        val node = GumboSoupParser.parse(html)
        node.getDescendantById("loginForm")?.getDescendantsByTag("input")?.forEach { println(it) }
    }
}
