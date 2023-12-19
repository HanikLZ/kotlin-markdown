package mdvsc.doc.markdown

import mdvsc.doc.DocElement

class TextLineElement(line: String? = null,
                      var checked: Boolean? = null,
                      val lines: MutableList<String> = if (line != null) mutableListOf(line) else mutableListOf(),
                      override val children: List<DocElement>? = null): DocElement
fun String?.asTextLine(checked: Boolean? = null) = TextLineElement(this, checked = checked)
