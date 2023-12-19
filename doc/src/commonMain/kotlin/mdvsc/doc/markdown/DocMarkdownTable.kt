package mdvsc.doc.markdown

import mdvsc.doc.DocBlock
import mdvsc.doc.DocElement
import mdvsc.doc.DocParser

object DocMarkdownTable: DocBlock {

    private val tableRowRegex = """^\|?.*(\|.*)+\|?$""".toRegex()
    private val tableHeaderRegex = """^\|?\s*:?---+:?\s*(\|\s*:?---+:?\s*)+\|?$""".toRegex()

    override val startLineSize = 2

    override fun DocParser.start(lines: List<String>, updateCache: (List<String>) -> Unit): DocElement? {
        val line1 = lines[lines.lastIndex - 1].trimEnd()
        val line2 = lines.last().trimEnd()
        return if (tableRowRegex.matches(line1) && tableHeaderRegex.matches(line2)) {
            val line1s = line1.rowElements
            val line2s = line2.rowElements
            if (line1s.size != line2s.size) null else {
                val headers = line2s.mapIndexed { index, s ->
                    MarkdownTableCell(s.width,
                        align = s.align,
                        children = parse(line1s[index].trim()))
                }
                MarkdownTable(listOf(MarkdownTableRow(header = true, children = headers)))
            }
        } else if (tableHeaderRegex.matches(line1) && tableRowRegex.matches(line2)) {
            val line1s = line1.rowElements
            val line2s = line2.rowElements
            if (line1s.size != line2s.size) null else {
                val headers = line1s.map { MarkdownTableCell(
                    it.width,
                    align = it.align) }
                MarkdownTable(listOf(
                    MarkdownTableRow(children = headers.mapIndexed { index, cell ->
                        cell.copy(children = parse(line2s[index].trim()))
                    })
                ))
            }
        } else null
    }

    override fun DocParser.process(
        current: DocElement,
        line: String,
        cacheLines: List<String>,
        update: (cache: List<String>?, finish: Boolean) -> Unit
    ) = if (current is MarkdownTable && tableRowRegex.matches(line)) {
        val last = current.children.last().children
        val lines = line.rowElements
        current.copy(children = current.children + MarkdownTableRow(children =
            lines.mapIndexedNotNull { index, s -> last.getOrNull(index)?.copy(children = parse(s.trim())) }
        ))
    } else {
        update(cacheLines, true)
        null
    }

    private val String.width get() = count { e -> e == '-' }

    private val String.align: Int? get() {
        val t = trim()
        val c = ':'
        return when {
            t.startsWith(c) && t.endsWith(c) -> 0
            t.startsWith(c) -> -1
            t.endsWith(c) -> 1
            else -> null
        }
    }

    private val String.rowElements get() = removeSurrounding("|").split('|')

}

data class MarkdownTable(override val children: List<MarkdownTableRow>): DocElement
data class MarkdownTableRow(val header: Boolean = false, override val children: List<MarkdownTableCell>): DocElement
data class MarkdownTableCell(val width: Int,
                             val align: Int? = null,
                             override val children: List<DocElement> = emptyList()
): DocElement
