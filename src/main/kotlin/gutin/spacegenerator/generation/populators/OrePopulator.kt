package gutin.spacegenerator.generation.populators

import gutin.spacegenerator.SpaceGenerator
import java.util.Random
import gutin.spacegenerator.generation.configuration.AsteroidConfiguration
import gutin.spacegenerator.loadConfiguration
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.LimitedRegion
import org.bukkit.generator.WorldInfo

class OrePopulator: BlockPopulator() {
	private val configuration: AsteroidConfiguration = loadConfiguration(SpaceGenerator.SpaceGenerator.dataFolder.resolve("asteroids"), "asteroid_configuration.conf")

	// need to redo this
	override fun populate(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, limitedRegion: LimitedRegion) {
//		val randomSource = RandomSource.create(worldInfo.seed)
//
//		val worldX = chunkX * 16
//		val worldZ = chunkZ * 16
//
//		for (count in 1..128) {
//			val ore = configuration.ores.random()
//
//			val replaceSphereConfiguration: ReplaceSphereConfiguration =
//				ReplaceSphereConfiguration(
//				)
//
//			val startLocationX = random.nextInt(worldX,worldX + 16)
//			val startLocationY = random.nextInt(worldInfo.minHeight, worldInfo.maxHeight)
//			val startLocationZ = random.nextInt(worldZ,worldZ + 16)
//
//			val asBlockPos = BlockPos(startLocationX, startLocationY, startLocationZ)
//
//			val i: Int = replaceSphereConfiguration.radius().sample()
//			val j: Int = replaceSphereConfiguration.radius().sample(randomSource)
//			val k: Int = replaceSphereConfiguration.radius().sample(randomSource)
//			val l = i.coerceAtLeast(j.coerceAtLeast(k))
//
//			for (blockPos2 in BlockPos.withinManhattan(asBlockPos, i, j, k)) {
//				if (blockPos2.distManhattan(asBlockPos) > l) {
//					break
//				}
//
//				limitedRegion.setType(blockPos2.x, blockPos2.y, blockPos2.z, ore)
//			}
//		}
	}

//	private fun oreWeights(): List<Material> {
//		val weightedList = mutableListOf<Material>()
//
////		for (ore in configuration.oreWeights) {
////			for (occurrence in ore.weight downTo 0)
////				weightedList.add(ore.materials)
////		} TODO
//
//		return weightedList
//	}
}