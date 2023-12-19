package mdvsc.doc.markdown

import mdvsc.doc.DocBlock
import mdvsc.doc.DocElement
import mdvsc.doc.DocParser

object DocMarkdownDivider: DocBlock {
    private val horizontalRegex = """^(---+|___+|\*\*\*+)$""".toRegex()
    override val startLineSize = null
    override fun DocParser.start(lines: List<String>, updateCache: (List<String>) -> Unit) = if (horizontalRegex.matches(lines.last())) MarkdownDivider else null
    override fun DocParser.process(
        current: DocElement,
        line: String,
        cacheLines: List<String>,
        update: (cache: List<String>?, finish: Boolean) -> Unit
    ): DocElement? {
        update(null, true)
        return null
    }
}

object MarkdownDivider: DocElement { override val children: List<DocElement>? = null }
