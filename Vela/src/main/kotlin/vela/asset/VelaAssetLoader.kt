package vela.asset

import ain.mesh.MeshBuilder
import ain.mesh.Vertex
import ain.mesh.VertexMeshBuilder
import aries.AssetLoader
import asset.VelaAssetManager
import org.lwjgl.BufferUtils
import javax.imageio.ImageIO


class VelaAssetLoader : AssetLoader(VelaAssetManager) {

    init {
        deserializers[Texture::class.java] = object : Deserializer<Texture>() {
            override fun deserialize(name: String): Texture {
                val image = ImageIO.read(getFileStream("/textures/$name.png"))

                val width = image.width
                val height = image.height

                val pixelsRaw = image.getRGB(0, 0, width, height, null, 0, height)

                val pixels = BufferUtils.createByteBuffer(width * height * 4)

                try {
                    for (i in 0 until width) {
                        for (j in 0 until height) {
                            val pixel = pixelsRaw[i * width + j]
                            pixels.put((pixel shr 16 and 0xFF).toByte())
                            pixels.put((pixel shr 8 and 0xFF).toByte())
                            pixels.put((pixel and 0xFF).toByte())
                            pixels.put((pixel shr 24 and 0xFF).toByte())
                        }
                    }
                } catch (e: ArrayIndexOutOfBoundsException) {
                    pixels.put(0x88.toByte())
                    pixels.put(0x88.toByte())
                    pixels.put(0x88.toByte())
                    pixels.put(0x00.toByte())
                }

                pixels.flip()

                return Texture(width, height, pixels)
            }
        }

        deserializers[String::class.java] = object : Deserializer<String>() {
            override fun deserialize(name: String): String {
                return getTextFile("/shaders/$name.glsl")
            }
        }
    }

    override fun load() {
        queueAsset("empty", String::class.java)
        queueAsset("depth", String::class.java)
        queueAsset("gbuffer", String::class.java)
        queueAsset("default", String::class.java)
        queueAsset("texture", Texture::class.java)
    }
}