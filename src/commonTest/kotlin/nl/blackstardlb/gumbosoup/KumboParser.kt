package nl.blackstardlb.gumbosoup

import kotlin.test.Test
import kotlin.test.assertTrue

class GumboSoupParserTest {
    private val resourceLoader: ResourceLoader = ResourceLoader()

    @Test
    fun test() {
        val html = resourceLoader.loadResourceAsString("signin.html")
        val node = GumboSoupParser.parse(html)
        val inputs = node.getDescendantById("loginForm")?.getDescendantsByTag("input")
        assertTrue { inputs != null }
        assertTrue { inputs!!.size == 4 }
        inputs!!.forEach { println(it) }
    }
}
