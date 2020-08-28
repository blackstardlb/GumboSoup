package nl.blackstardlb.kumbo

interface GSNode {
    val parent: GSNode?
    val children: List<GSNode>
    val attributes: Map<String, String>
    val descendants: List<GSNode>
    val ancestors: List<GSNode>
    fun getAttribute(attributeName: String): String? = attributes[attributeName]
    fun hasAttribute(attributeName: String): Boolean = getAttribute(attributeName) != null
    val hasChildren: Boolean
        get() = children.isNotEmpty()
    val hasParent: Boolean
        get() = parent != null
    val root: GSNode
        get() {
            var root = this
            while (root.parent != null) {
                root = root.parent!!
            }
            return root
        }
    val document: DocumentGSNode?
        get() = root as? DocumentGSNode

    fun getDescendantById(id: String): ElementGSNode? {
        return this.descendants.elements().firstOrNull { it.id == id }
    }

    fun getDescendantsByTag(tag: String): List<ElementGSNode> {
        return this.descendants.elements().filter { it.tag == tag }
    }

    fun getAncestorsByTag(tag: String): List<ElementGSNode> {
        return this.ancestors.elements().filter { it.tag == tag }
    }

    fun getAncestorsById(id: String): ElementGSNode? {
        return this.ancestors.elements().firstOrNull { it.id == id }
    }

    fun asDocument(): DocumentGSNode? = this as? DocumentGSNode
    fun asElement(): ElementGSNode? = this as? ElementGSNode
    fun asText(): TextGSNode? = this as? TextGSNode
    fun asComment(): CommentGSNode? = this as? CommentGSNode
}

fun List<GSNode>.elements(): List<ElementGSNode> {
    return this.mapNotNull { it as? ElementGSNode }
}

internal fun GSNode.fetchDescendants(): List<GSNode> {
    return children.flatMap { child -> mutableListOf(child).also { it.addAll(child.descendants) } }
}

internal fun GSNode.fetchAncestors(): List<GSNode> {
    return mutableListOf(parent).also { parent?.descendants?.let { desc -> it.addAll(desc) } }.filterNotNull()
}

