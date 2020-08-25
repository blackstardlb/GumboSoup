package nl.blackstardlb.GumboSoup

data class Node(
    val tag: String?,
    val text: String?,
    val attributes: Map<String, String>,
    val parent: Node?,
    val children: List<Node>
) {
    val id: String? = attributes["id"]
    val isTextNode: Boolean = text != null
    val descendants: List<Node> by lazy { children.flatMap { child -> mutableListOf(child).also { it.addAll(child.descendants) } } }
    val ancestors: List<Node> by lazy {
        mutableListOf(parent).also { parent?.descendants?.let { desc -> it.addAll(desc) } }.filterNotNull()
    }

    override fun toString(): String {
        return toBaseNode().toString()
    }
}

data class TextNode(val text: String, override val parent: BaseNode?) : BaseNode {
    override fun toString(): String {
        return text
    }
}

data class ElementNode(
    val tag: String,
    val attributes: Map<String, String>,
    val children: List<BaseNode>,
    override val parent: BaseNode?
) : BaseNode {
    override fun toString(): String {
        var attributesString = attributes.map { "${it.key}=\"${it.value}\"" }.joinToString(" ")
        attributesString = if (attributesString.isNotBlank()) " $attributesString" else ""
        val childrenString = children.joinToString("\n") { it.toString() }
        if (childrenString.isEmpty()) {
            return "<$tag$attributesString/>"
        }
        return "<$tag$attributesString>$childrenString</$tag>"
    }
}

interface BaseNode {
    val parent: BaseNode?
}
