package mdvsc.doc.markdown

import mdvsc.doc.DocParser

fun markdownParser() = DocParser(listOf(
    DocMarkdownHeadings,
    DocMarkdownHeadings2,
    DocMarkdownCode,
    DocMarkdownCode2,
    DocMarkdownQuotes,
    DocMarkdownList,
    DocMarkdownDefinitionList,
    DocMarkdownTable,
    DocMarkdownFootnotes,
    DocMarkdownDivider,
    DocMarkdownText(),
    DocMarkdownBr
))

fun String.markdown() = markdownParser().parse(this)