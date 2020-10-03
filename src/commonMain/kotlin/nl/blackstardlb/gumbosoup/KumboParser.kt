package nl.blackstardlb.gumbosoup

internal expect fun parseHtml(html: String): GSNode

object GumboSoupParser {
    fun parse(html: String): GSNode = parseHtml(html)
}
