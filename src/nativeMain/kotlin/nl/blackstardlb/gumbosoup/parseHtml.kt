package nl.blackstardlb.gumbosoup

actual fun parseHtml(html: String): GSNode {
    return GumboParser().parse(html)
}
