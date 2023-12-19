package mdvsc.doc.markdown

import mdvsc.doc.DocBlock
import mdvsc.doc.DocElement
import mdvsc.doc.DocParser

class DocMarkdownText(autoUrlProtocol: List<String> = listOf(
    "http",
    "https",
    "ws",
    "wss",
    "ftp",
    "ftps",
    "mailto",
    "tel",
    "sms",
    "file",
)): DocBlock {

    private val contentRegex = listOf(
        "\\*{3}".emphasis(),
        "\\*{2}".emphasis(),
        "\\*".emphasis(),
        "={2}".emphasis(),
        "_{3}".emphasis(),
        "_".emphasis(),
        "~{2}".emphasis(),
        "~".emphasis(),
        "`".emphasis(),
        "\\^".emphasis(),
        "<".emphasis(">"),
        "\\[\\^".emphasis("\\]"),
        "!?\\[".emphasis("\\]") + "\\(".emphasis("\\)"),
        "\\r\\n").joinToString("|").toRegex()

    private val tooltipRegex = """\s".+?"$""".toRegex()
    private val breakRegex = """\s{3,}$""".toRegex()
    private val urlRegex = if (autoUrlProtocol.isNotEmpty()) """(${autoUrlProtocol.joinToString("|") {
        "${it.uppercase()}|${it.lowercase()}"
    }})://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]""".toRegex() else null

    private fun String.emphasis(end: String = this, content: String = ".+?") = """$this$content$end"""

    override val startLineSize = null
    override fun DocParser.start(lines: List<String>, updateCache: (List<String>) -> Unit)
            = if (lines.last().isBlank()) null else TextLineElement(lines = lines.toMutableList())

    override fun DocParser.process(
        current: DocElement,
        line: String,
        cacheLines: List<String>,
        update: (cache: List<String>?, finish: Boolean) -> Unit
    ) = if (line.isBlank()) (current as? TextLineElement)?.apply {
        checked = true
        update(cacheLines, true)
    } else null

    override fun DocParser.end(current: DocElement, lines: List<String>): DocElement? {
        val element = current as? TextLineElement ?: return null
        if (lines.isNotEmpty()) element.lines.addAll(lines)
        val text = element.lines.joinToString("\n") { it.replace(breakRegex, "\r\n") }
        return MarkdownText(text, paragraph = element.checked).parseContent()
    }

    private fun MarkdownText.parseContent(): DocElement = contentRegex.find(text)?.let { l ->
        var r = l
        val pg = if (r.range.first == 0 && r.range.last == text.length - 1) paragraph else null
        val list = buildList {
            var endIndex = r.range.last + 1
            do {
                val found = r.value
                val p = when {
                    found.startsWith("***") || found.startsWith("___") -> copy(
                        text = found.substring(3, found.length - 3), bold = true, italic = true,
                        paragraph = pg
                    ).parseContent()
                    found.startsWith("**") || found.startsWith("__") -> copy(
                        text = found.substring(2, found.length - 2), bold = true, paragraph = pg
                    ).parseContent()
                    found.startsWith("==") -> copy(
                        text = found.substring(2, found.length - 2), highlight = true, paragraph = pg
                    ).parseContent()
                    found.startsWith("~~") -> copy(
                        text = found.substring(2, found.length - 2), strikeThrough = true, paragraph = pg
                    ).parseContent()
                    found.startsWith('~') -> copy(
                        text = found.substring(1, found.length - 1), subOrSuperScript = true, paragraph = pg
                    ).parseContent()
                    found.startsWith('^') -> copy(
                        text = found.substring(1, found.length - 1), subOrSuperScript = false, paragraph = pg
                    ).parseContent()
                    found.startsWith('*') || found.startsWith('_') -> copy(
                        text = found.substring(1, found.length - 1), italic = true, paragraph = pg
                    ).parseContent()
                    found.startsWith('`') -> copy(text = found.substring(1, found.length - 1), code = true, paragraph = pg)
                    found.startsWith('<') -> {
                        val link = found.substring(1, found.length - 1)
                        MarkdownLink(link = link,
                            children = listOf(copy(text = link, paragraph = pg)))
                    }
                    found.startsWith("[^") -> MarkdownRefer(found.substringAfter('^').substringBeforeLast(']'))
                    found.startsWith('\r') -> MarkdownBr
                    else -> {
                        val link = found.substringAfter('(').substringBefore(')')
                        val tooltipResult = tooltipRegex.find(link)
                        val tooltip = tooltipResult?.value?.trimStart()?.removeSurrounding("\"")
                        val url = tooltipResult?.range?.run { link.substring(0, first) } ?: link
                        val text = copy(
                            text = found.substringAfter('[').substringBefore(']'), paragraph = pg)
                            .parseContent()
                        MarkdownLink(link = url,
                            tooltip = tooltip,
                            renderLink = found.startsWith('!'),
                            children = listOf(text))
                    }
                }
                if (r.range.first > endIndex) add(copy(text = text.substring(endIndex, r.range.first), paragraph = pg))
                add(p)
                endIndex = r.range.last + 1
                r = r.next() ?: break
            } while (true)
            if (endIndex < text.length - 1) add(copy(text = text.substring(endIndex), paragraph = pg))
        }
        val content = text.substring(0, l.range.first)
        list.ifEmpty { null }?.let {
            val first = it.first()
            if (it.size > 1 || content.isNotEmpty() || first !is MarkdownText) copy(text = content, children = it)
            else first.copy(paragraph = paragraph)
        }
    } ?: urlRegex?.find(text)?.let {
        var s = it
        var r = it.range
        val pg = false
        val list = buildList {
            do {
                if (r.first > 0) add(copy(text = text.substring(0, r.first), paragraph = pg))
                val link = s.value.trim()
                add(MarkdownLink(link, children = listOf(copy(text = link, paragraph = pg))))
                s = s.next() ?: break
                r = s.range
            } while (true)
            if (r.last < text.length - 1) add(copy(text = text.substring(r.last), paragraph = pg))
        }
        val first = list.first()
        if (list.size > 1 || first !is MarkdownText) copy(text = "", children = list)
        else first.copy(paragraph = paragraph)
    } ?: this
}

data class MarkdownText(val text: String,
                        val code: Boolean? = null,
                        val bold: Boolean? = null,
                        val italic: Boolean? = null,
                        val highlight: Boolean? = null,
                        val subOrSuperScript: Boolean? = null,
                        val strikeThrough: Boolean? = null,
                        val paragraph: Boolean? = null,
                        override val children: List<DocElement>? = null): DocElement

data class MarkdownLink(val link: String? = null,
                        val tooltip: String? = null,
                        val renderLink: Boolean? = null,
                        override val children: List<DocElement>): DocElement

class MarkdownRefer(val referId: String): DocElement { override val children = null }
