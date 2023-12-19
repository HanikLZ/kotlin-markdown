package mdvsc.doc.markdown

import mdvsc.doc.DocBlock
import mdvsc.doc.DocElement
import mdvsc.doc.DocParser

object DocMarkdownList: DocBlock {

    private val listRegex = """^[\t\s]*([-+*]|\d+\.|\[[\sXx]\])(\s|$)""".toRegex()
    private val listItemCheckRegex = """^\[[\sXx]\]\s""".toRegex()

    private fun String.itemOrdered() = trimStart().first().run { if (isDigit()) true else if (this == '[') null else false }

    override val startLineSize = 1
    override fun DocParser.start(lines: List<String>, updateCache: (List<String>) -> Unit): DocElement? {
        val line = lines.last()
        return listRegex.find(line)?.run {
            val ordered = value.itemOrdered()
            val item = if (ordered == null) line.trimStart() else line.substring(value.length)
            MarkdownList(ordered,
                indent = line.indentSize(),
                children = mutableListOf(parseListItem(item)))
        }
    }

    override fun DocParser.process(
        current: DocElement,
        line: String,
        cacheLines: List<String>,
        update: (cache: List<String>?, finish: Boolean) -> Unit
    ): DocElement? {
        val list = current as? MarkdownList ?: return null
        if (line.isBlank()) {
            list.lastItemText { lines.add(line.trim()) }
            return list
        }
        val nline = line.removeIndentSize(list.indent)
        if (nline.indentSize() > 0) {
            list.lastItemText { lines.add(nline.removeIndentSize(1)) }
            return list
        }
        val matcher = listRegex.find(nline)
        return if (matcher != null) {
            val value = matcher.value
            val ordered = value.itemOrdered()
            if (ordered != list.ordered) {
                update(cacheLines, true)
                null
            } else {
                list.lastItemText { l ->
                    val children = (l ?: list).children
                    children[children.lastIndex] = MarkdownListItem(
                        checked = checked,
                        children = parseLines(lines))
                }
                list.children.add(parseListItem(if (ordered == null) nline.trimStart() else nline.substring(value.length)))
                list
            }
        } else null
    }

    override fun DocParser.end(current: DocElement, lines: List<String>): DocElement? {
        val list = current as? MarkdownList ?: return null
        list.lastItemText { l ->
            if (lines.isNotEmpty()) lines.forEach { this.lines.add(it.trimStart()) }
            val children = (l ?: list).children
            children[children.lastIndex] = MarkdownListItem(
                checked = checked,
                children = parseLines(this.lines))
        } ?: MarkdownListItem(children = parseLines(lines)).let(list.children::add)
        return list
    }

    private fun parseListItem(content: String): DocElement {
        val checkResult = listItemCheckRegex.find(content)?.value
        val checked = checkResult?.lowercase()?.startsWith("[x")
        val actualContent = if (checkResult != null) content.substring(checkResult.length) else content
        return actualContent.ifEmpty { null }.asTextLine(checked)
    }

    private fun<T> DocElement.lastItemText(block: TextLineElement.(MarkdownList?) -> T): T? = if (this is TextLineElement) block(this, null) else if (this is MarkdownList) {
        var p = children.lastOrNull()
        var pl: MarkdownList = this
        while (p !is TextLineElement) {
            if (p is MarkdownList) {
                pl = p
                p = p.children.lastOrNull()
            } else break
        }
        (p as? TextLineElement)?.let { block(it, pl) }
    } else null

    private fun String.indentSize(tabCount: Int = 2): Int {
        var count = 0
        for (cr in this) count += when (cr) {
            '\t' -> tabCount
            ' ' -> 1
            else -> break
        }
        return count / tabCount
    }


    private fun String.removeIndentSize(size: Int, tabCount: Int = 2): String {
        if (size <= 0) return this
        var removeSize = size
        var line = this
        while (removeSize-- > 0) {
            line = when {
                line.startsWith('\t') -> line.drop(1)
                else -> {
                    val ls = line.take(tabCount)
                    if (ls.isBlank()) line.drop(tabCount) else break
                }
            }
        }
        return line
    }

}

data class MarkdownList(val ordered: Boolean? = null,
                        val indent: Int = 0,
                        override val children: MutableList<DocElement> = mutableListOf()): DocElement

data class MarkdownListItem(val checked: Boolean? = null,
                            override val children: List<DocElement>): DocElement
