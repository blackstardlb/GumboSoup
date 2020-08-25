package nl.blackstardlb.GumboSoup

actual fun parseHtml(html: String): Node {
    return Gumbo().parse(html)
}
