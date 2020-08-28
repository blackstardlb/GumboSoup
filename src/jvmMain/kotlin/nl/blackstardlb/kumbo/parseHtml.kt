package nl.blackstardlb.GumboSoup

import nl.blackstardlb.kumbo.GSNode

internal actual fun parseHtml(html: String): GSNode {
    return JsoupParser().parse(html)
}
