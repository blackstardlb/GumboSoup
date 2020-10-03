package nl.blackstardlb.gumbosoup

internal actual fun parseHtml(html: String): GSNode {
    return JsoupParser().parse(html)
}
