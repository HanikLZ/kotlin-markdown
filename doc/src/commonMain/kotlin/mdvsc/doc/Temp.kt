package mdvsc.doc


// typealias PickDoc = List<String>.(current: DocBlock?, updateCache: (List<String>) -> Unit) -> Pair<DocBlock, DocElement>?
/*

    /*
        private fun List<DocBlock>.pickDoc(lines: List<String>, updateCache: (List<String>) -> Unit, check: (DocBlock.() -> Boolean)? = null): Pair<DocBlock, DocElement>? {
            for (b in this) {
                if (check == null || check(b)) {
                    val e = b.run { start(lines, updateCache) }
                    if (e != null) return b to e
                }
            }
            return null
        }

        private fun Iterator<String>.processLine(pickDoc: PickDoc,
                                                 add: (DocElement) -> Unit,
                                                 extraLineProcessor: ((List<String>) -> Unit)? = null) {
            val processLines = mutableListOf<String>()
            var processElement: DocElement? = null
            var lastDoc: DocBlock? = null
            fun finishElement(lines: List<String>) = lastDoc?.run {
                processElement?.let { end(it, lines) }?.let(add)
                processElement = null
                lastDoc = null
            } != null
            fun pickDoc() = pickDoc(processLines, lastDoc) { if (processLines != it) processLines.addAll(it) }?.run {
                val leftLines = first.startLineSize?.let {
                    if (it > 0 && it < processLines.size) processLines.take(processLines.size - it) else null
                } ?: emptyList()
                if (!finishElement(leftLines) && leftLines.isNotEmpty()) extraLineProcessor?.invoke(leftLines)
                lastDoc = first
                processElement = second
                processLines.clear()
            }
            for (l in this) lastDoc?.run {
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
                        finishElement(processLines)
                        processLines.clear()
                    } else if (result != null) processLines.clear()
                    result
                }
            } ?: run {
                processLines.add(l)
                pickDoc()
            }
            if (!finishElement(processLines) && processLines.isNotEmpty()) extraLineProcessor?.invoke(processLines)
        } */
 */
/*       lines.processLine({ _, updateCache ->
            val lss = size
            docs.pickDoc(this, updateCache) { startLineSize?.let { s -> s <= lss } == true }
        }, ::add) {
            extraLineProcessor?.invoke(it)?.let(::add)
                ?: it.iterator().processLine({ current, updateCache ->
                    nonSizeDocs.filter { e -> e != current }.pickDoc(this, updateCache)
                }, ::add)
        }*/

