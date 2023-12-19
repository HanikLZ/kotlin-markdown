package mdvsc.doc.markdown

import mdvsc.doc.DocBlock
import mdvsc.doc.DocElement
import mdvsc.doc.DocParser

private val headingIdRegex = """\s\{#.*\}$""".toRegex()

object DocMarkdownHeadings: DocBlock {

    private val headingRegex = """^#{1,6}\s""".toRegex()
    override val startLineSize = 1

    override fun DocParser.process(current: DocElement, line: String, cacheLines: List<String>,
                                   update: (cache: List<String>?, finish: Boolean) -> Unit): DocElement? {
        update(cacheLines, true)
        return null
    }

    override fun DocParser.start(lines: List<String>, updateCache: (List<String>) -> Unit): DocElement? {
        val line = lines.last()
        return headingRegex.find(line)?.run {
            val vl = value.length
            makeElement(vl - 1, line.substring(vl))
        }
    }

}

object DocMarkdownHeadings2: DocBlock {

    private val h1Regex = "^==+$".toRegex()
    private val h2Regex = "^--+$".toRegex()

    override val startLineSize = 2

    override fun DocParser.start(lines: List<String>, updateCache: (List<String>) -> Unit): DocElement? {
        val content = lines[lines.lastIndex - 1].ifBlank { null } ?: return null
        val line = lines.last()
        val level = if (h1Regex.matches(line)) 1 else if (h2Regex.matches(line)) 2 else null
        return level?.let { makeElement(it, content) }
    }

    override fun DocParser.process(current: DocElement,
                                   line: String,
                                   cacheLines: List<String>,
                                   update: (cache: List<String>?, finish: Boolean) -> Unit): DocElement? {
        update(cacheLines, true)
        return null
    }

}

private fun DocParser.makeElement(level: Int, line: String): DocElement {
    val idResult = headingIdRegex.find(line)
    val content = if (idResult != null) line.substring(0, idResult.range.first) else line
    val element = parse(content)
    return MarkdownHeading(level = level,
        id = idResult?.value?.substringAfter('#')?.substringBefore('}')?.trim(),
        children = element)
}

data class MarkdownHeading(val level: Int, val id: String? = null, override val children: List<DocElement>): DocElement
