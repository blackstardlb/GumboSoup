package nl.blackstardlb.GumboSoup

import nl.blackstardlb.kumbo.GSNode
import nl.blackstardlb.kumbo.GumboParser

actual fun parseHtml(html: String): GSNode {
    return GumboParser().parse(html)
}
