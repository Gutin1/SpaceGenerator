package gutin.spacegenerator

import gutin.spacegenerator.generation.SpaceChunkGenerator
import gutin.spacegenerator.generation.SpaceBiomeProvider
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.java.JavaPlugin

class SpaceGenerator : JavaPlugin() {

    init { SpaceGenerator = this }

    companion object {
        lateinit var SpaceGenerator: SpaceGenerator private set
    }

    override fun onLoad() {}

    override fun onEnable() {}

    override fun onDisable() {}

    override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator? {
        return SpaceChunkGenerator()
    }

    override fun getDefaultBiomeProvider(worldName: String, id: String?): BiomeProvider? {
        return SpaceBiomeProvider()
    }
}