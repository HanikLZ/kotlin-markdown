package markdown

import androidx.compose.runtime.*
import highlightjs.highlight
import mdvsc.doc.markdown.MarkdownCode
import org.jetbrains.compose.web.dom.*

@Composable
fun MarkdownCode.Component() {
    val content by remember(lines) { mutableStateOf(lines.joinToString("\n")) }
    var renderContent by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(content, code) {
        renderContent = code.ifBlank { null }?.let { content.highlight(it) }?.value
    }
    Pre {
        Code {
            DisposableEffect(renderContent) {
                renderContent?.also { scopeElement.innerHTML = it } ?: run { scopeElement.innerText = content }
                onDispose {  }
            }
        }
    }
}
