package markdown

import androidx.compose.runtime.Composable
import mdvsc.doc.markdown.MarkdownTable
import mdvsc.doc.markdown.MarkdownTableRow
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLElement

fun StyleScope.textAlign(align: Int) {
    textAlign(if (align == 0) "center" else if (align < 0) "left" else "right")
}

@Composable
fun MarkdownTable.Component() {
    val list = children.groupBy { it.header }
    Table {
        list[true]?.RenderRow(true)
        list[false]?.RenderRow(false)
    }
}

@Composable
fun List<MarkdownTableRow>.RenderRow(header: Boolean) {
    if (header) Thead { forEach { it.Component() } } else Tbody { forEach { it.Component() } }
}

@Composable
fun MarkdownTableRow.Component() {
    Tr {
        fun AttrsScope<HTMLElement>.align(align: Int) = style { textAlign(align) }
        if (header) children.forEach {
            Th(it.align?.let{{ align(it) }}) { it.RenderChildren() }
        } else children.forEach {
            Td(it.align?.let{{ align(it) }}) { it.RenderChildren() }
        }
    }
}
