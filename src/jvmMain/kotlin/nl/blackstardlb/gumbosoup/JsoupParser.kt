package nl.blackstardlb.gumbosoup

import org.jsoup.Jsoup
import org.jsoup.nodes.*

internal class JsoupParser {
    fun parse(html: String): GSNode {
        val document = Jsoup.parse(html)
        return document.toNode()
    }

    private fun Document.toNode(): GSNode {
        val doctype: DocumentType? = this.documentType()
        return DocumentGSNode(doctype?.name(), doctype?.publicId(), doctype?.systemId()) { node ->
            this.children().map { it.toNode(node) }
        }
    }

    private fun org.jsoup.nodes.Node.toNode(parent: GSNode?): GSNode {
        return when (this) {
            is TextNode -> this.toNode(parent)
            is Element -> this.toNode(parent)
            is Comment -> this.toNode(parent)
            is DataNode -> this.toNode(parent)
            else -> throw NotImplementedError("Node type ${this.javaClass} not implemented\n$this")
        }
    }

    private fun Element.toNode(parent: GSNode?): ElementGSNode {
        val attributes = this.attributes().asList().map { it.key to it.value }.toMap()
        return ElementGSNode(tagName(), parent, attributes) { node ->
            this.childNodes().map { it.toNode(node) }
        }
    }

    private fun TextNode.toNode(parent: GSNode?): TextGSNode {
        return TextGSNode(text(), parent)
    }

    private fun Comment.toNode(parent: GSNode?): CommentGSNode {
        return CommentGSNode(data, parent)
    }

    private fun DataNode.toNode(parent: GSNode?): TextGSNode {
        return TextGSNode(wholeData, parent)
    }
}

