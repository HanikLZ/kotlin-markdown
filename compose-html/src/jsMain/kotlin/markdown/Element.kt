package markdown

import androidx.compose.runtime.*
import mdvsc.doc.DocElement
import mdvsc.doc.markdown.*
import org.jetbrains.compose.web.attributes.readOnly
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLElement

@Composable
fun RenderElement(element: DocElement) {
    when (element) {
        is MarkdownHeading -> element.Component()
        is MarkdownText -> element.Component()
        is MarkdownLink -> element.Component()
        is MarkdownQuotes -> element.Component()
        is MarkdownList -> element.Component()
        is MarkdownListItem -> element.Component()
        is MarkdownCode -> element.Component()
        is MarkdownDefinitionList -> element.Component()
        is MarkdownDefinitionListItem -> element.Component()
        is MarkdownTable -> element.Component()
        is MarkdownRefer -> element.Component()
        is MarkdownBr -> Br()
        is MarkdownDivider -> Hr()
        else -> Unit
    }
}

@Composable
fun DocElement.RenderChildren() {
    children?.forEach { RenderElement(it) }
}

@Composable
fun MarkdownHeading.Component() {
    TagElement<HTMLElement>("h$level", id?.let { { id(it) } }) { RenderChildren() }
}

@Composable
fun MarkdownQuotes.Component() {
    TagElement<HTMLElement>("blockquote", null) { RenderChildren() }
}

@Composable
fun MarkdownDefinitionList.Component() {
    TagElement<HTMLElement>("dl", null) { RenderChildren() }
}

@Composable
fun MarkdownDefinitionListItem.Component() {
    TagElement<HTMLElement>(if (label) "dt" else "dd", null) { RenderChildren() }
}


@Composable
fun MarkdownList.Component() {
    when (ordered) {
        true -> Ol { RenderChildren() }
        else -> Ul(if (ordered == null) {{
            style { listStyle("none") }
        }} else null) { RenderChildren() }
    }
}

@Composable
fun MarkdownListItem.Component() {
    Li {
        checked?.let { CheckboxInput(it) { readOnly() } }
        RenderChildren()
    }
}
