package nl.blackstardlb.GumboSoup

import nl.blackstardlb.kumbo.NativeParser

actual fun parseHtml(html: String): Node {
    return NativeParser().parse(html)
}
