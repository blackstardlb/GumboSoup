package nl.blackstardlb.GumboSoup

actual fun parseHtml(html: String): Node {
    return JsoupHelper().parse(html)
}
