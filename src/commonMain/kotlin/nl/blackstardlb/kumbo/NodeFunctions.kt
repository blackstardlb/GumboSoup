package nl.blackstardlb.GumboSoup

fun Node.getDescendantById(id: String): Node? {
    return this.descendants.firstOrNull { it.id == id }
}

fun Node.getDescendantsByTag(tag: String): List<Node> {
    return this.descendants.filter { it.tag == tag }
}

fun Node.getAncestorsByTag(tag: String): List<Node> {
    return this.ancestors.filter { it.tag == tag }
}

fun Node.getAncestorsById(id: String): Node? {
    return this.ancestors.firstOrNull { it.id == id }
}

fun Node.toTextNode(parent: BaseNode?): TextNode {
    return TextNode(this.text!!, parent)
}

fun Node.toElementNode(parent: BaseNode?): ElementNode {
    val children = mutableListOf<BaseNode>()
    val elementNode = ElementNode(
        this.tag!!,
        attributes,
        children,
        parent
    )
    children.addAll(this.children.map { it.toBaseNode(elementNode) })
    return elementNode
}

fun Node.toBaseNode(parent: BaseNode? = null): BaseNode {
    return if (isTextNode) toTextNode(parent) else toElementNode(parent)
}
