package mdvsc.doc.markdown

import mdvsc.doc.DocBlock
import mdvsc.doc.DocElement
import mdvsc.doc.DocParser

object DocMarkdownFootnotes: DocBlock {

    private val footnoteRegex = """^\[\^.+?\]:\s""".toRegex()

    override val startLineSize = 1
    override fun DocParser.start(lines: List<String>, updateCache: (List<String>) -> Unit): DocElement? {
        val last = lines.last()
        return footnoteRegex.find(last)?.run {
            MarkdownFootnote(value.substringAfter('^').substringBeforeLast(']'), listOf(last.substring(value.length).asTextLine()))
        }
    }

    override fun DocParser.process(
        current: DocElement,
        line: String,
        cacheLines: List<String>,
        update: (cache: List<String>?, finish: Boolean) -> Unit) = null

    override fun DocParser.end(current: DocElement, lines: List<String>): DocElement? {
        val foot = current as? MarkdownFootnote ?: return null
        return (foot.children.lastOrNull() as? TextLineElement)?.run {
            if (lines.isNotEmpty()) this.lines.addAll(lines)
            parseLines(this.lines)
        }?.let { foot.copy(children = foot.children.dropLast(1) + it) } ?: foot
    }

}

data class MarkdownFootnote(val id: String, override val children: List<DocElement>): DocElement