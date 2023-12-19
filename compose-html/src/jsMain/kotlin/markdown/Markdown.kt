package markdown

import androidx.compose.runtime.*
import mdvsc.doc.markdown.MarkdownFootnote
import mdvsc.doc.markdown.markdown
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLDivElement

@Composable
fun Markdown(content: String, attrs: AttrBuilderContext<HTMLDivElement>? = null) {
    val items by remember(content) { mutableStateOf(content.markdown()) }
    val refers by remember(items) { mutableStateOf(items.filterIsInstance<MarkdownFootnote>()) }
    Div(attrs) {
        CompositionLocalProvider(LocalMarkdownRefer provides refers) { items.forEach { RenderElement(it) } }
        if (refers.isNotEmpty()) {
            Hr()
            Ol {
                refers.forEachIndexed { index, t ->
                    Li({ id(t.referId(index + 1)) }) { t.RenderChildren() }
                }
            }
        }
    }
}

val LocalMarkdownRefer = staticCompositionLocalOf<List<MarkdownFootnote>> { error("no refer") }
