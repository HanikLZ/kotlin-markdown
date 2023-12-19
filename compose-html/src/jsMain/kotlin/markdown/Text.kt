package markdown

import androidx.compose.runtime.*
import mdvsc.doc.markdown.MarkdownText
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLElement

@Composable
fun MarkdownText.Component() {
    if (paragraph == true) P { Content() } else Content()
}

@Composable
private fun MarkdownText.Content() {
    TextElement()
    RenderChildren()
}

@Composable
private fun MarkdownText.TextElement() {
    when {
        bold == true -> TagElement<HTMLElement>("strong", null) { copy(bold = null).TextElement() }
        italic == true -> TagElement<HTMLElement>("em", null) { copy(italic = null).TextElement() }
        strikeThrough == true -> TagElement<HTMLElement>("del", null) { copy(strikeThrough = null).TextElement() }
        code == true -> Code { copy(code = null).TextElement() }
        subOrSuperScript != null -> TagElement<HTMLElement>(if (subOrSuperScript == true) "sub" else "sup", null) { copy(subOrSuperScript = null).TextElement() }
        highlight == true -> TagElement<HTMLElement>("mark", null) { copy(highlight = null).TextElement() }
        else -> Text(text.replace('\n', ' '))
    }
}
