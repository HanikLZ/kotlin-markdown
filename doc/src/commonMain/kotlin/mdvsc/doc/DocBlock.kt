package mdvsc.doc

interface DocBlock {

    val startLineSize: Int?

    fun DocParser.start(lines: List<String>, updateCache: (List<String>) -> Unit): DocElement?

    fun DocParser.end(current: DocElement, lines: List<String> = emptyList()): DocElement? = current

    fun DocParser.process(current: DocElement, line: String,
                          cacheLines: List<String>,
                          update: (cache: List<String>?, finish: Boolean) -> Unit): DocElement? = current

}

interface DocElement { val children: List<DocElement>? }
