package highlightjs

fun String.highlight(language: String? = null, ignoreIllegals: Boolean = true) = language?.ifBlank { null }?.let {
    highlight(this, object: HighlightOptions {
        override val language = it
        override val ignoreIllegals = ignoreIllegals
    })
} ?: highlightAuto(this)
