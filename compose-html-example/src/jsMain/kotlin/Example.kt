import androidx.compose.runtime.*
import kotlinx.browser.window
import kotlinx.coroutines.await
import markdown.Markdown
import org.jetbrains.compose.web.renderComposable
import org.jetbrains.compose.web.css.*

external fun <T> require(module: String): T

fun main() {
    require<Unit>("highlight.js/styles/github.css")
    renderComposable(rootElementId = "root") {
        Style(MarkdownStyle)
        var content by remember { mutableStateOf<String?>(null) }
        LaunchedEffect(Unit) {
            val response = window.fetch("/help.md").await()
            content = response.text().await()
        }
        content?.let { Markdown(it) { classes(MarkdownStyle.style) } }
    }
}

