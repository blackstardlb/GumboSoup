package nl.blackstardlb.gumbosoup

import gumbo.*
import kotlinx.cinterop.*

class GumboParser {
    fun parse(html: String): GSNode {
        val output = gumbo_parse(html)
        val toGumboSoupNode = output!!.pointed.document?.pointed?.toGSNode(null)!!
        gumbo_destroy_output(output)
        return toGumboSoupNode
    }

    private fun GumboNode.toGSNode(parent: GSNode?): GSNode {
        return when (this.type) {
            GumboNodeType.GUMBO_NODE_ELEMENT -> this.toElement(parent)
            GumboNodeType.GUMBO_NODE_DOCUMENT -> this.toDocument()
            GumboNodeType.GUMBO_NODE_WHITESPACE -> this.toText(parent)
            GumboNodeType.GUMBO_NODE_TEXT -> this.toText(parent)
            GumboNodeType.GUMBO_NODE_CDATA -> this.toText(parent)
            GumboNodeType.GUMBO_NODE_COMMENT -> this.toComment(parent)
            GumboNodeType.GUMBO_NODE_TEMPLATE -> this.toElement(parent)
        }
    }

    private fun GumboNode.toElement(parent: GSNode?): ElementGSNode {
        val attributes = this.v.element
            .attributes
            .toList<GumboAttribute>()
            .map { (it.name?.toKString() ?: "") to (it.value?.toKString() ?: "") }
            .toMap()
        val tag = this.v.element.let { gumbo_normalized_tagname(it.tag) }?.toKString() ?: ""
        return ElementGSNode(tag, parent, attributes) { node ->
            this.v.element.children.toList<GumboNode>().map { it.toGSNode(node) }
        }
    }

    private fun GumboNode.toText(parent: GSNode?): TextGSNode {
        val text = this.v.text.text?.toKString() ?: ""
        return TextGSNode(text, parent)
    }

    private fun GumboNode.toComment(parent: GSNode?): CommentGSNode {
        val comment = this.v.text.text?.toKString() ?: ""
        return CommentGSNode(comment, parent)
    }

    private fun GumboNode.toDocument(): DocumentGSNode {
        val name = this.v.document.name?.toKString() ?: ""
        val publicId = this.v.document.public_identifier?.toKString()
        val systemId = this.v.document.system_identifier?.toKString()
        return DocumentGSNode(name, systemId, publicId) { node ->
            this.v.document.children.toList<GumboNode>().map { it.toGSNode(node) }
        }
    }

    inline fun <reified T : CPointed> GumboVector.toList(): List<T> {
        val length = this.length.toInt()
        return (0 until length).mapNotNull { this.data?.get(it)?.reinterpret<T>()?.pointed }
            .toList()
    }
}
