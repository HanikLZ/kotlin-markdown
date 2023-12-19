package mdvsc.doc.markdown

import mdvsc.doc.DocBlock
import mdvsc.doc.DocElement
import mdvsc.doc.DocParser

object DocMarkdownBr: DocBlock {

    override val startLineSize = null
    override fun DocParser.start(lines: List<String>, updateCache: (List<String>) -> Unit)
    = if (lines.last().isBlank()) MarkdownBr else null
    override fun DocParser.process(
        current: DocElement,
        line: String,
        cacheLines: List<String>,
        update: (cache: List<String>?, finish: Boolean) -> Unit
    ) = if (line.isBlank()) current else null
}

object MarkdownBr: DocElement { override val children = null }
