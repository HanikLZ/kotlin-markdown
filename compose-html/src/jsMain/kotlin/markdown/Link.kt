package markdown

import androidx.compose.runtime.Composable
import mdvsc.doc.markdown.MarkdownLink
import mdvsc.doc.markdown.MarkdownText
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Img
import org.w3c.dom.HTMLElement

@Composable
fun MarkdownLink.Component() {
    val attrs: AttrBuilderContext<HTMLElement>? = tooltip?.let { { title(it) } }
    if (renderLink == true) {
        link?.let { src ->
            val single = children.singleOrNull() as? MarkdownText
            val text = if (single != null) buildString { text(single) } else ""
            Img(src, alt = text, attrs = attrs)
        } ?: RenderChildren()
    } else A(link, attrs) { RenderChildren() }
}

private fun StringBuilder.text(text: MarkdownText) {
    if (text.text.isNotEmpty()) append(text.text)
    text.children?.forEach { if (it is MarkdownText) text(it) }
}
