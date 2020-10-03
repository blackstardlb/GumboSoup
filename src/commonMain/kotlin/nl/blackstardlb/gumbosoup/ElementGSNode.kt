package nl.blackstardlb.gumbosoup

data class ElementGSNode(
    val tag: String,
    override val parent: GSNode?,
    override val attributes: Map<String, String>,
    val childBuilder: (ElementGSNode) -> List<GSNode>,
) : GSNode {
    val id = getAttribute("id")
    override val children: List<GSNode> = childBuilder(this)
    override val descendants: List<GSNode> by lazy { fetchDescendants() }
    override val ancestors: List<GSNode> by lazy { fetchAncestors() }
    override fun toString(): String {
        val attributesAsStrings = attributes
            .map { "${it.key}=\"${it.value}\"" }
        val childrenString = children.map { it.toString() }.filter { it.isNotBlank() }.joinToString("\n")
            .let {
                var output = it
                if (output.startsWith("<")) {
                    output = "\n" + output
                }
                if (output.endsWith(">")) {
                    output += "\n"
                }
                output
            }
        val fields = listOf(tag) + attributesAsStrings
        return if (childrenString.isEmpty()) {
            "<${fields.joinToString(" ")}/>"
        } else {
            "<${fields.joinToString(" ")}>$childrenString</$tag>"
        }
    }
}
