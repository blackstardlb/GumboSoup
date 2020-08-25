package nl.blackstardlb.GumboSoup

import org.jsoup.Jsoup
import org.jsoup.nodes.*
import org.jsoup.nodes.TextNode

class JsoupHelper {
    fun parse(html: String): Node {
        val document = Jsoup.parse(html)
        return document.toNode()
    }

    fun Document.toNode(): Node {
        val children = mutableListOf<Node>()
        val node = Node("document", null, this.attributes().asList().map { it.key to it.value }.toMap(), null, children)
        children.addAll(this.childNodes().filterNot { it is DocumentType }.map { it.toNode(node) })
        return node
    }


    private fun org.jsoup.nodes.Node.toNode(parent: Node?): Node {
        return when (this) {
            is TextNode -> this.toNode(parent)
            is Element -> this.toNode(parent)
            is Comment -> this.toNode(parent)
            is DataNode -> this.toNode(parent)
            else -> throw NotImplementedError("Node type ${this.javaClass} not implemented\n$this")
        }
    }

    private fun Element.toNode(parent: Node?): Node {
        val children = mutableListOf<Node>()
        val node = Node(
            this.tagName(),
            null,
            this.attributes().asList().map { it.key to it.value }.toMap(),
            parent,
            children
        )
        children.addAll(this.childNodes().map { it.toNode(node) })
        return node
    }

    private fun TextNode.toNode(parent: Node?): Node {
        val children = mutableListOf<Node>()
        val node = Node(
            null,
            text(),
            this.attributes().asList().map { it.key to it.value }.toMap(),
            parent,
            children
        )
        children.addAll(this.childNodes().map { it.toNode(node) })
        return node
    }

    private fun Comment.toNode(parent: Node?): Node {
        val children = mutableListOf<Node>()
        val node = Node(
            null,
            data,
            this.attributes().asList().map { it.key to it.value }.toMap(),
            parent,
            children
        )
        children.addAll(this.childNodes().map { it.toNode(node) })
        return node
    }

    private fun DataNode.toNode(parent: Node?): Node {
        val children = mutableListOf<Node>()
        val node = Node(
            null,
            wholeData,
            this.attributes().asList().map { it.key to it.value }.toMap(),
            parent,
            children
        )
        children.addAll(this.childNodes().map { it.toNode(node) })
        return node
    }
}

