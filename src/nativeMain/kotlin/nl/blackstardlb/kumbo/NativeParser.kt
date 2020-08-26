package nl.blackstardlb.kumbo

import gumbo.*
import kotlinx.cinterop.*
import nl.blackstardlb.GumboSoup.Node

class NativeParser {
    private val options = kGumboDefaultOptions

    fun parse(html: String): Node {
        val output = gumbo_parse_with_options(options.ptr, html, html.length.toULong())
        val toGumboSoupNode = output!!.pointed.root!!.reinterpret<GumboNode>().pointed.toGumboSoupNode()
        gumbo_destroy_output(options.ptr, output)
        return toGumboSoupNode
    }

    fun GumboNode.allNodes(): List<GumboNode> {
        return mutableListOf(this).also { it.addAll(this.allDescendants()) }
    }

    fun GumboNode.allDescendants(): List<GumboNode> {
        val nodes = mutableListOf<GumboNode>()
        if (this.isNodeElement()) {
            val children = this.v.element.children
            children.toList<GumboNode>().forEach {
                nodes.add(it)
                nodes.addAll(it.allNodes())
            }
        }
        return nodes
    }

    fun GumboNode.allChildren(): List<GumboNode> {
        if (this.isNodeElement()) {
            return this.v.element.children.toList()
        }
        return emptyList()
    }

    fun GumboNode.isNodeElement(): Boolean {
        return this.type == GumboNodeType.GUMBO_NODE_ELEMENT
    }

    fun GumboNode.isNodeDocument(): Boolean {
        return this.type == GumboNodeType.GUMBO_NODE_DOCUMENT
    }

    fun GumboNode.isNodeText(): Boolean {
        return this.type == GumboNodeType.GUMBO_NODE_TEXT || return this.type == GumboNodeType.GUMBO_NODE_WHITESPACE || return this.type == GumboNodeType.GUMBO_NODE_COMMENT
    }

    inline fun <reified T : CPointed> GumboVector.toList(): List<T> {
        val length = this.length.toInt()
        return (0 until length).mapNotNull { this.data?.get(it)?.reinterpret<T>()?.pointed }
            .toList()
    }

    fun GumboNode.toGumboSoupNode(parent: Node? = null): Node {
        val text = text()?.text?.toKString()?.trim()
        val tag = element()?.let { gumbo_normalized_tagname(it.tag) }?.toKString() ?: document()?.let { "document" }

        if (tag == null && text == null) {
            println("type: " + this.type)
        }

        val attributes = element()
            ?.attributes
            ?.toList<GumboAttribute>()
            ?.map { (it.name?.toKString() ?: "") to (it.value?.toKString() ?: "") }
            ?.toMap() ?: emptyMap()

        val children = mutableListOf<Node>()
        val node = Node(
            tag,
            text,
            attributes,
            parent,
            children
        )
        children.addAll(this.allChildren().map { it.toGumboSoupNode(node) }.toList())
        return node
    }

    fun GumboNode.text(): GumboText? {
        return if (this.isNodeText()) v.text else null
    }

    fun GumboNode.element(): GumboElement? {
        return if (this.isNodeElement()) v.element else null
    }

    fun GumboNode.document(): GumboDocument? {
        return if (this.isNodeDocument()) v.document else null
    }
}
