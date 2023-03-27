package aries

import java.io.*
import java.net.URISyntaxException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

abstract class AssetLoader(private val assetManager: AssetManager) {
    private val queue: Queue<Pair<String, Class<*>>> = ArrayDeque()
    protected val deserializers = mutableMapOf<Class<*>, Deserializer<*>>()

    abstract fun load()

    protected fun queueAsset(name: String, assetType: Class<*>) {
        queue.add(Pair(name, assetType))
    }

    protected fun queueAll(path: String, assetType: Class<*>) {
        for (file in getFiles(path)) {
            queueAsset(file.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0], assetType)
        }
    }

    fun loadAssets() {
        val n = queue.size
        for (i in 0 until n) {
            val pair = queue.poll()

            val deserializer = deserializers[pair.second] ?: error("Can't find deserializer for asset of type ${pair.second}")
            val asset = deserializer.deserialize(pair.first) ?: error("Couldn't deserialize asset ${pair.first}")

            assetManager.register(pair.second, pair.first, asset)
            println("Loaded asset " + (i + 1) + " of " + n)
        }
    }

    fun getFiles(path: String): List<String> {
        val results = mutableListOf<String>()
        var files = arrayOf<File>()

        try {
            files = File(getResource(path).toURI()).listFiles() ?: error("Can't find any files in $path")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        for (file in files) {
            if (file.isFile) {
                results.add(file.name)
            }
        }

        return results
    }

    fun getTextFile(path: String): String {
        try {
            InputStreamReader(getFileStream(path), StandardCharsets.UTF_8).use { streamReader ->
                BufferedReader(streamReader).use { reader ->
                    val builder = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        builder.append(line).append("\n")
                    }
                    return builder.toString()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        throw IllegalArgumentException("Can't read file! ($path)")
    }

    fun getFileStream(path: String): InputStream {
        var path = path

        if (!path.startsWith("/")) {
            path = "/$path"
        }

        val url = getResource(path)

        return url.openStream() ?: error("Failed opening input stream for file $path")
    }

    fun getResource(path: String): URL {
        return AssetLoader::class.java.getResource(path) ?: error("Can't find any resource in $path")
    }

    abstract class Deserializer<T> {
        abstract fun deserialize(name: String): T
    }
}