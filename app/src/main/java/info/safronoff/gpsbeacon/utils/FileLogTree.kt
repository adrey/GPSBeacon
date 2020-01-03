package info.safronoff.gpsbeacon.utils

import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class FileLogTree(private val directory: File, private val fileName: String) : Timber.DebugTree() {


    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        message?.let {
            appendLog(it)
        }
    }

    private fun appendLog(text: String) {
        if (!directory.exists()) {
            directory.mkdirs()
            directory.mkdir()
        }
        val logFile = File(directory, fileName)
        if (!logFile.exists()) {
            try {
                logFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        try {
            val buf = BufferedWriter(FileWriter(logFile, true))
            val logTimeStamp = SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS", Locale.getDefault())
                .format(Date())
            buf.append("$logTimeStamp $text")
            buf.newLine()
            buf.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}