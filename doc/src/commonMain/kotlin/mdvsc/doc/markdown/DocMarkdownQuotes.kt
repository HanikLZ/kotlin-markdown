package mdvsc.doc.markdown

import mdvsc.doc.DocBlock
import mdvsc.doc.DocElement
import mdvsc.doc.DocParser

object DocMarkdownQuotes: DocBlock {

    private val quotesRegex = """(^>+\s|^>+$)""".toRegex()

    override val startLineSize = 1

    override fun DocParser.start(lines: List<String>, updateCache: (List<String>) -> Unit): DocElement? {
        val line = lines.last()
        return quotesRegex.find(line)?.run {
            MarkdownQuotes().also {
                var quotes = it
                var level = value.level - 1
                while (level-- > 0) quotes = MarkdownQuotes().also(quotes.children::add)
                quotes.children.add(line.substring(value.length).asTextLine())
            }
        }
    }

    override fun DocParser.process(
        current: DocElement,
        line: String,
        cacheLines: List<String>,
        update: (cache: List<String>?, finish: Boolean) -> Unit) = if (line.isBlank()) {
        update(cacheLines, true)
        current
    } else if (current is MarkdownQuotes) quotesRegex.find(line)?.run {
        var level = value.level - 1
        var quotes: MarkdownQuotes = current
        val content = line.substring(value.length)
        var last = quotes.children.lastOrNull()
        var insertQuote = quotes
        while (last is MarkdownQuotes) {
            quotes = last
            last = last.children.lastOrNull()
            if (level-- > 0) insertQuote = quotes
        }
        if (level > 0) {
            convertLast(quotes, cacheLines)
            do insertQuote = MarkdownQuotes().also(insertQuote.children::add) while (--level >  0) }
        last = insertQuote.children.lastOrNull()
        (last as? TextLineElement)?.lines?.add(content) ?: run {
            (last as? MarkdownQuotes)?.apply { convertLast(findLastQuotes(), cacheLines) }
            insertQuote.children.add(content.asTextLine())
        }
        current
    } else null

    override fun DocParser.end(current: DocElement, lines: List<String>): DocElement? {
        val quotes = (current as? MarkdownQuotes)?.findLastQuotes() ?: return current
        convertLast(quotes, lines)
        return current
    }

    private fun MarkdownQuotes.findLastQuotes(): MarkdownQuotes {
        var quotes = this
        var last = quotes.children.lastOrNull()
        while (last is MarkdownQuotes) {
            quotes = last
            last = last.children.lastOrNull()
        }
        return quotes
    }

    private fun DocParser.convertLast(quotes: MarkdownQuotes, caches: List<String>, last: DocElement? = quotes.children.lastOrNull()) {
        (last as? TextLineElement)?.run {
            if (lines.isNotEmpty()) lines.addAll(caches)
            quotes.children.run {
                removeLast()
                addAll(parseLines(lines))
            }
        } ?: caches.ifEmpty { null }?.let(::parseLines)?.let(quotes.children::addAll)
    }

    private val String.level get() = count { it == '>' }

}

data class MarkdownQuotes(override val children: MutableList<DocElement> = mutableListOf()): DocElement