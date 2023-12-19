package mdvsc.doc

class DocParser(doc: List<DocBlock>) {

    private val docs: List<DocBlock>
    private val nonSizeDocs: List<DocBlock>

    init {
        val ds = mutableListOf<DocBlock>()
        val nds = mutableListOf<DocBlock>()
        doc.forEach { (if (it.startLineSize == null) nds else ds).add(it) }
        docs = ds
        nonSizeDocs = nds
    }

    fun parse(content: String) = parseLines(content.lineSequence())
    fun parseLines(lines: Collection<String>) = parseLines(lines.iterator())
    fun parseLines(lines: Sequence<String>) = parseLines(lines.iterator())
    fun parseLines(lines: Iterator<String>): List<DocElement> = buildList {
        lines.parseLines(docs, ::add) { it.iterator().parseLines(nonSizeDocs, ::add) }
    }

    private fun Iterator<String>.parseLines(docs: List<DocBlock>,
                                            onAdd: (DocElement) -> Unit,
                                            extraLines: ((List<String>) -> Unit)? = null) {
        val processor = Processor(docs, extraLines = extraLines, finish = onAdd)
        for (line in this@parseLines) processor.process(line)
        processor.end()
    }

    inner class Processor(val docs: List<DocBlock>,
                          val extraLines: ((List<String>) -> Unit)? = null,
                          val finish: (DocElement) -> Unit) {

        private val processLines = mutableListOf<String>()
        private var processElement: DocElement? = null
        private var lastDoc: DocBlock? = null

        private fun finishElement(lines: List<String> = processLines) = lastDoc?.run {
            processElement?.let { end(it, lines) }?.let(finish)
            processElement = null
            lastDoc = null
        } != null

        fun process(l: String) = lastDoc?.run {
            var finished: Boolean? = null
            var updateCacheLines: List<String>? = null
            val result = processElement?.let {
                process(it, l, processLines.toList()) { ls, finish ->
                    finished = finish
                    updateCacheLines = ls
                }
            }?.also { processElement = it }
            if (result != null || updateCacheLines != null) {
                processLines.clear()
                updateCacheLines?.let(processLines::addAll)
                updateCacheLines = null
            }
            if (finished == false) result ?: processLines.add(l) else {
                if (finished == true) {
                    finishElement()
                    processLines.clear()
                } else if (result != null) processLines.clear()
                result
            }
        } ?: run {
            processLines.add(l)
            val lineSize = processLines.size
            for (d in docs) {
                if (d.startLineSize == null && d == lastDoc) continue
                val size = d.startLineSize ?: 0
                val element = if (size <= lineSize) d.run { start(processLines) {} } else null
                if (element != null) {
                    val leftLines = if (size in 1..< lineSize) processLines.take(lineSize - size) else emptyList()
                    processLines.clear()
                    if (!finishElement(leftLines) && leftLines.isNotEmpty()) extraLines?.invoke(leftLines)
                    lastDoc = d
                    processElement = element
                    break
                }
            }
        }

        fun end() {
            if (!finishElement() && processLines.isNotEmpty()) extraLines?.invoke(processLines)
        }

    }

}
