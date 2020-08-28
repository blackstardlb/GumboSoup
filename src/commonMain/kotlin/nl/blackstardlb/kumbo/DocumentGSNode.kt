package nl.blackstardlb.kumbo

data class DocumentGSNode(
    val name: String?,
    val systemId: String?,
    val publicId: String?,
    val childBuilder: (DocumentGSNode) -> List<GSNode>,
) : GSNode {
    override val parent: GSNode? = null
    override val children: List<GSNode> = childBuilder(this)
    override val attributes: Map<String, String> = emptyMap()
    override val descendants: List<GSNode> by lazy { fetchDescendants() }
    override val ancestors: List<GSNode> by lazy { fetchAncestors() }

    override fun toString(): String {
        val childrenString = children.joinToString("\n") { it.toString() }
        val fields = listOf("!DOCTYPE", name, publicId, systemId).filterNotNull().filter { !it.isBlank() }
        return "<${fields.joinToString(" ")}>\n$childrenString"
    }
}
