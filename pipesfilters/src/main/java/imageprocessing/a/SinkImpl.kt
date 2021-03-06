package imageprocessing.a

import imageprocessing.dataobjects.Line
import imageprocessing.framework.pmp.filter.Sink
import java.io.File
import java.io.PrintWriter
import java.util.*

class SinkImpl(outputFile: File) : Sink<LinkedList<Line>>() {

    private val writer: PrintWriter

    init {
        outputFile.printWriter().write("")
        writer = outputFile.printWriter()
    }

    override fun write(value: LinkedList<Line>?) {
        if (value != null) {
            for (line in value) {
                writer.println("${line.content}\t: ${line.number}")
                writer.flush()
            }
        }
        //writer.flush()

    }
}
