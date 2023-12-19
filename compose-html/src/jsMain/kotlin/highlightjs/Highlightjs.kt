@file:JsModule("highlight.js")
@file:JsNonModule
package highlightjs

external interface HighlightResult {
    val value: String
    val relevance: Int
    val code: String?
    val language: String?
    val illegal: Boolean
    val top: String
}

external interface HighlightOptions {
    val language: String
    val ignoreIllegals: Boolean
}

external fun highlight(code: String, options: HighlightOptions): HighlightResult

external fun highlightAuto(code: String, languageSubset: List<String> = definedExternally): HighlightResult
