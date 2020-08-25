package nl.blackstardlb.GumboSoup

import gumbo.*
import kotlinx.cinterop.*
import platform.posix.*

class Gumbo {
    fun parse(html: String): Node {
        val options = kGumboDefaultOptions
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

    fun GumboNode.print() {
        val tag = if (isNodeText()) "Text" else this.v.element.tag.name
        val text = if (isNodeText()) this.v.text.text?.toKString()?.trim() else ""
        println("$tag : $text")
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

    fun readFile(fileName: String): String {
        val f = fopen(fileName, "rb")
        fseek(f, 0, SEEK_END)
        val fsize = ftell(f).toULong()
        rewind(f)

        val buff = ByteArray(fsize.toInt())
        buff.usePinned {
            val result = fread(it.addressOf(0), 1, fsize, f)
            if (result != fsize) {
                throw Exception("Reading error")
            }
        }
        fclose(f)
        return buff.toKString()
    }

    inline fun <reified T : CPointed> GumboVector.toList(): List<T> {
        val length = this.length.toInt()
        return (0 until length).mapNotNull { this.data?.get(it)?.reinterpret<T>()?.pointed }
            .toList()
    }

    fun GumboAttribute.print(): Unit {
        println("${this.name?.toKString()} : ${this.value?.toKString()}")
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
