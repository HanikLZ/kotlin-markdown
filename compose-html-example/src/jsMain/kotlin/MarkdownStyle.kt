import org.jetbrains.compose.web.css.*

object MarkdownStyle: StyleSheet() {
    val style by style {
        "a" style {
            textDecoration("none")
        }
        "img" style {
            maxWidth(100.percent)
        }
        "hr" style {
            opacity(0.2)
        }
        "table" style {
            width(100.percent)
            property("border-collapse", "collapse")
            border {
                color(rgba(0, 0,0, 0.1))
                style(LineStyle.Solid)
                width(1.px)
            }
        }
        "tr > td, th" style {
            val hPadding = 14.px
            val vPadding = 10.px
            paddingLeft(hPadding)
            paddingRight(hPadding)
            paddingTop(vPadding)
            paddingBottom(vPadding)
            border {
                color(rgba(0, 0,0, 0.1))
                style(LineStyle.Solid)
                width(1.px)
            }
        }
        "th" style {
            backgroundColor(rgba(0,0,0,0.05))
        }
        "tr:nth-child(even)" style {
            backgroundColor(rgba(0,0,0,0.02))
        }
        "sup" style {
            val margin = 2.px
            marginLeft(margin)
            marginRight(margin)
        }
        "sub" style {
            val margin = 2.px
            marginLeft(margin)
            marginRight(margin)
        }
        "blockquote" style {
            background("#F9F9F9")
            border(style = LineStyle.Solid, color = Color("#CCC"))
            borderWidth(left = 4.px, right = 0.px, top = 0.px, bottom = 0.px)
            margin(10.px)
            padding(10.px)
        }
        "pre" style {
            backgroundColor(rgba(0, 0, 0, 0.1))
            borderRadius(6.px)
            padding(16.px)
        }
        "p > code" style {
            backgroundColor(rgba(0, 0, 0, 0.05))
            val radius = 4.px
            borderRadius(radius)
            paddingLeft(radius)
            paddingRight(radius)
        }
        "li > input[type=checkbox]" style { marginRight(4.px) }
    }
}
