package mdvsc.doc.markdown

import mdvsc.doc.DocBlock
import mdvsc.doc.DocElement
import mdvsc.doc.DocParser

object DocMarkdownCode: DocBlock {

    private val codeBlockRegex = "^`{3}".toRegex()
    private val codeBlockEndRegex = """^`{3}\s*$""".toRegex()

    override val startLineSize = 1

    override fun DocParser.start(lines: List<String>, updateCache: (List<String>) -> Unit): DocElement? {
        val line = lines.last()
        return codeBlockRegex.find(line)?.run { MarkdownCode(code = line.substring(value.length),
            children = listOf(TextLineElement())) }
    }

    override fun DocParser.process(current: DocElement,
                                   line: String,
                                   cacheLines: List<String>,
                                   update: (cache: List<String>?, finish: Boolean) -> Unit) = if (codeBlockEndRegex.matches(line)) {
        update(null, true)
        current
    } else (current.children?.lastOrNull() as? TextLineElement)?.lines?.run {
        if (cacheLines.isNotEmpty()) addAll(cacheLines)
        add(line)
        current
    }

    override fun DocParser.end(current: DocElement, lines: List<String>) = (current as? MarkdownCode)?.run {
        val actualLines = (children?.lastOrNull() as? TextLineElement)?.lines?.apply {
           if (lines.isNotEmpty()) addAll(lines)
        } ?: lines
        copy(lines = actualLines, children = null)
    }

}


object DocMarkdownCode2: DocBlock {

    private val codeBlockRegex = "^\\s{3}".toRegex()

    override val startLineSize = 1

    override fun DocParser.start(lines: List<String>, updateCache: (List<String>) -> Unit): DocElement? {
        val line = lines.last()
        return codeBlockRegex.find(line)?.run { TextLineElement(line.substring(value.length)) }
    }

    override fun DocParser.process(current: DocElement,
                                   line: String,
                                   cacheLines: List<String>,
                                   update: (cache: List<String>?, finish: Boolean) -> Unit) = codeBlockRegex.find(line)?.run {
        val l = current as? TextLineElement ?: return null
        l.lines.add(line.substring(value.length))
        current
    } ?: run {
        update(cacheLines, true)
        null
    }

    override fun DocParser.end(current: DocElement, lines: List<String>) = (current as? TextLineElement)?.let {
        if (lines.isNotEmpty()) it.lines.addAll(lines)
        MarkdownCode("", it.lines)
    }

}

data class MarkdownCode(val code: String,
                        val lines: List<String> = emptyList(),
                        override val children: List<DocElement>? = null): DocElement
