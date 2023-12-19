package mdvsc.doc.markdown

import mdvsc.doc.DocBlock
import mdvsc.doc.DocElement
import mdvsc.doc.DocParser

object DocMarkdownDefinitionList: DocBlock {

    private val definitionRegex = """^:\s""".toRegex()

    override val startLineSize = 2

    override fun DocParser.start(lines: List<String>, updateCache: (List<String>) -> Unit): DocElement? {
        val line = lines.last()
        return definitionRegex.find(line)?.run {
            val index = lines.indexOfLast { it.isBlank() }
            val itemLines = if (index < 0) lines.dropLast(1) else lines.subList(index + 1, lines.lastIndex)
            if (itemLines.isNotEmpty()) {
                if (index > 0) updateCache(lines.subList(0, index))
                MarkdownDefinitionList(
                    children = listOf(
                        MarkdownDefinitionListItem(
                            label = true,
                            children = parseLines(itemLines)),
                        line.substring(value.length).asItem(false)))
            }
            else null
        }
    }

    override fun DocParser.process(
        current: DocElement,
        line: String,
        cacheLines: List<String>,
        update: (cache: List<String>?, finish: Boolean) -> Unit
    ): DocElement? {
        val list = current as? MarkdownDefinitionList ?: return current
        return if (line.isBlank()) {
            val nl = convertLast(list, cacheLines)
            if (nl == null) update(cacheLines, true)
            nl
        } else definitionRegex.find(line)?.run {
            val item = line.substring(value.length).asItem(false)
            val nl = convertLast(list, cacheLines)
            if (nl == null && cacheLines.isNotEmpty()) list.copy(children = list.children + listOf(
                MarkdownDefinitionListItem(true, parseLines(cacheLines)),
                item
            )) else (nl ?: list).run { copy(children = children + item) }
        }
    }

    override fun DocParser.end(current: DocElement, lines: List<String>) = (current as? MarkdownDefinitionList)?.let {
        convertLast(it, lines)
    } ?: current

    private fun String.asItem(label: Boolean = false) =  MarkdownDefinitionListItem(label = label,
        children = mutableListOf(asTextLine())
    )

    private fun DocParser.convertLast(list: MarkdownDefinitionList, lines: List<String>): MarkdownDefinitionList? {
        val lastItem = list.children.lastOrNull()
        return if (lastItem != null) {
            val text = lastItem.children.lastOrNull()
            if (text is TextLineElement) {
                if (lines.isNotEmpty()) text.lines.addAll(lines)
                val items = parseLines(text.lines)
                list.copy(children = list.children.dropLast(1) + lastItem.copy(children = items))
            } else null
        } else null
    }

}


data class MarkdownDefinitionList(override val children: List<MarkdownDefinitionListItem>): DocElement

data class MarkdownDefinitionListItem(val label: Boolean = false, override val children: List<DocElement>): DocElement