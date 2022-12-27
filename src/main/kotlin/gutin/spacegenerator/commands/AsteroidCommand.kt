package gutin.spacegenerator.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import gutin.spacegenerator.generation.populators.AsteroidPopulator
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.WorldGenLevel
import org.bukkit.World
import org.bukkit.craftbukkit.v1_19_R2.generator.CraftLimitedRegion
import org.bukkit.entity.Player
import java.util.Random

@CommandAlias("asteroid")
class AsteroidCommand : BaseCommand() {

    @Suppress("unused")
    @CommandPermission("spacegenerator.regenerate")
    @Subcommand("regenerate chunk")
    fun onRegenerateSingle(sender: Player) {
        val chunkPos = ChunkPos(sender.chunk.x, sender.chunk.z)

        postGenerateAsteroids(sender.world, chunkPos)
    }

    @Suppress("unused")
    @CommandPermission("spacegenerator.regenerate")
    @Subcommand("regenerate range")
    fun onRegenerateRange(sender: Player, range: Int) {
        for (x in sender.chunk.x - range..sender.chunk.x + range) {
            for (z in sender.chunk.z - range..sender.chunk.z + range) {
                postGenerateAsteroids(sender.world, ChunkPos(x, z))
            }
        }
    }

    @Suppress("unused")
    @Subcommand("create asteroid")
    fun onCreateAsteroid(sender: Player) {
        val chunkPos = ChunkPos(sender.chunk.x, sender.chunk.z)
        val world = sender.world

        val asteroidRandom = Random(chunkPos.x + (chunkPos.z) + world.seed)

        val craftWorld =  (world as ServerLevel).level

        val populator: AsteroidPopulator =
            craftWorld.generator.getDefaultPopulators(world)
                .find { it is AsteroidPopulator } as? AsteroidPopulator ?: return

        val asteroid = with(populator) {
            this.generateAsteroid(
                BlockPos(
                    sender.location.x,
                    sender.location.y,
                    sender.location.z
                ),
                asteroidRandom
            )
        }

        val limitedRegion = CraftLimitedRegion(craftWorld, chunkPos)

        populator.apply {
            this.placeAsteroid(
                craftWorld.seed,
                chunkPos.x,
                chunkPos.z,
                limitedRegion,
                asteroid
            )
        }

    }

    private fun postGenerateAsteroids(world: World, chunkPos: ChunkPos) {
        val asteroids = getChunkAsteroids(chunkPos.x, chunkPos.z)

        if (asteroids.isEmpty()) return

        val craftWorld = (world as WorldGenLevel)

        val populator: AsteroidPopulator =
            (world as ServerLevel).level.generator.getDefaultPopulators(world)
                .find { it is AsteroidPopulator } as? AsteroidPopulator ?: return

        val limitedRegion = CraftLimitedRegion(craftWorld, chunkPos)

        for (asteroid in asteroids) {
            populator.apply {
                this.placeAsteroid(
                    craftWorld.seed,
                    chunkPos.x,
                    chunkPos.z,
                    limitedRegion,
                    asteroid
                )
            }
        }
    }

    private fun getChunkAsteroids(chunkX: Int, chunkZ: Int): List<AsteroidPopulator.Asteroid> {
        return listOf()
    }
}