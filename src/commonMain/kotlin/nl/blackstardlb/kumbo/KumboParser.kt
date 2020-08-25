package nl.blackstardlb.GumboSoup


expect fun parseHtml(html: String): Node

object GumboSoupParser {
    fun parse(html: String): Node = parseHtml(html)
}
