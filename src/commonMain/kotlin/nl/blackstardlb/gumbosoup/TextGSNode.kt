package nl.blackstardlb.gumbosoup

data class TextGSNode(override val text: String, override val parent: GSNode?) : GSNode {
    override val children: List<GSNode> = emptyList()
    override val descendants: List<GSNode> by lazy { fetchDescendants() }
    override val ancestors: List<GSNode> by lazy { fetchAncestors() }
    override val attributes: Map<String, String> = emptyMap()
    override fun toString(): String {
        return text.trim()
    }
}
