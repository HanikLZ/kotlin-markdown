package markdown

import androidx.compose.runtime.Composable
import mdvsc.doc.markdown.MarkdownFootnote
import mdvsc.doc.markdown.MarkdownRefer
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.TagElement
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

fun MarkdownFootnote.referId(index: Int) = "$index:$id"

@Composable
fun MarkdownRefer.Component() {
    var index = 0
    val fn = LocalMarkdownRefer.current.find {
        index++
        it.id == referId
    }
    TagElement<HTMLElement>("sup", null) {
        A("#${fn?.referId(index) ?: ""}") {
            Text(index.toString())
            RenderChildren()
        }
    }
}
