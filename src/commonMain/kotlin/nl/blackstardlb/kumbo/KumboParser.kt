package nl.blackstardlb.GumboSoup

import nl.blackstardlb.kumbo.GSNode


internal expect fun parseHtml(html: String): GSNode

object GumboSoupParser {
    fun parse(html: String): GSNode = parseHtml(html)
}
