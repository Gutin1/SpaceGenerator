package gutin.spacegenerator.generation.populators

import gutin.spacegenerator.SpaceGenerator
import gutin.spacegenerator.generation.configuration.AsteroidConfiguration
import gutin.spacegenerator.generation.configuration.Ore
import gutin.spacegenerator.loadConfiguration
import net.minecraft.core.BlockPos
import org.bukkit.Material
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.LimitedRegion
import org.bukkit.generator.WorldInfo
import java.util.Random

open class OrePopulator() : BlockPopulator() {
    // default asteroid configuration values
    private val configuration: AsteroidConfiguration =
        loadConfiguration(SpaceGenerator.SpaceGenerator.dataFolder.resolve("asteroids"), "asteroid_configuration.conf")

    private val asteroidBlocks: MutableSet<Material> = mutableSetOf()
    private val weightedOres = oreWeights()

    init {
        configuration.blockPalettes.forEach { asteroidBlocks.addAll((it.materials.keys)) }
    }


    override fun populate(
		worldInfo: WorldInfo,
		random: Random,
		chunkX: Int,
		chunkZ: Int,
		limitedRegion: LimitedRegion,
	) {
        val worldX = chunkX * 16
        val worldZ = chunkZ * 16

        if (weightedOres.isEmpty()) return

        for (count in configuration.orePlacementsPerChunk downTo 0) {
            val origin = BlockPos(
                random.nextInt(worldX, worldX + 16),
                random.nextInt(worldInfo.minHeight + 10, worldInfo.maxHeight - 10),
                random.nextInt(worldZ, worldZ + 16)
            )

            if (!asteroidBlocks.contains(limitedRegion.getType(origin.x,
                    origin.y,
                    origin.z))
            ) {
                continue
            } // Future proofing, Return if it's not trying to replace an asteroid block

            val ore = weightedOres[random.nextInt(0, weightedOres.size - 1)]

            val blobSize = random.nextInt(1, ore.maxBlobSize)

            val oreBlocks = getSphereBlocks(blobSize, origin = origin)

			for (block in oreBlocks) {
                if (!asteroidBlocks.contains(limitedRegion.getType(block.x,
                        block.y,
                        block.z))
                ) continue

				limitedRegion.setType(block.x, block.y, block.z, ore.material)
			}
        }
    }

	private fun oreWeights(): List<Ore> {
        val weightedList = mutableListOf<Ore>()

        for (ore in configuration.ores) {
            for (occurrence in ore.rolls downTo 0) {
                weightedList.add(ore)
            }
        }

        return weightedList
    }

    private fun getSphereBlocks(radius: Int, origin: BlockPos): List<BlockPos> {
        if (radius == 1) return listOf(origin) // bypass the rest of this if it's useless

        val circleBlocks = mutableListOf<BlockPos>()
        val upperBoundSquared = radius * radius

        for (x in origin.x - radius..origin.x + radius) {
            for (y in origin.y - radius..origin.y + radius) {
                for (z in origin.z - radius..origin.z + radius) {
                    val distance =
                        ((origin.x - x) * (origin.x - x) + (origin.z - z) * (origin.z - z) + (origin.y - y) * (origin.y - y)).toDouble()

                    if (distance < upperBoundSquared) {
                        circleBlocks.add(BlockPos(x, y, z))
                    }
                }
            }
        }

        return circleBlocks
    }
}