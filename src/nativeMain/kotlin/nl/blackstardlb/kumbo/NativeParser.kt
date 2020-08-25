package nl.blackstardlb.kumbo

import nl.blackstardlb.GumboSoup.Node

expect class NativeParser() {
    fun parse(html: String): Node
}
