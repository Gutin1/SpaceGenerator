package gutin.spacegenerator.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import gutin.spacegenerator.SpaceGenerator
import gutin.spacegenerator.generation.configuration.AsteroidConfiguration
import gutin.spacegenerator.generation.populators.AsteroidPopulator
import gutin.spacegenerator.loadConfiguration
import net.minecraft.core.BlockPos
import net.minecraft.world.level.ChunkPos
import org.bukkit.World
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld
import org.bukkit.craftbukkit.v1_19_R2.generator.CraftLimitedRegion
import org.bukkit.entity.Player
import java.util.Random

@CommandAlias("asteroid")
class AsteroidCommand : BaseCommand() {
    private val configuration: AsteroidConfiguration =
        loadConfiguration(SpaceGenerator.SpaceGenerator.dataFolder.resolve("asteroids"), "asteroid_configuration.conf")

    @Suppress("unused")
    @CommandPermission("spacegenerator.regenerate")
    @Subcommand("regenerate single")
    fun onRegenerateSingle(sender: Player) {
        val chunkPos = ChunkPos(sender.chunk.x, sender.chunk.z)

        postGenerateAsteroids(sender.world, chunkPos)

        sender.sendRichMessage("<#7fff7f>Success!")
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

        sender.sendRichMessage("<#7fff7f>Success!")
    }

    @Suppress("unused")
    @CommandPermission("spacegenerator.regenerate")
    @Subcommand("create custom")
    @CommandCompletion("size index octaves")
    fun onCreateCustom(sender: Player, size: Double, index: Int, octaves: Int) {
        if (!IntRange(0, configuration.blockPalettes.size).contains(index)) {
            sender.sendRichMessage("<red>ERROR: index out of range: 0..${configuration.blockPalettes.size}")
            return
        }

        val chunkPos = ChunkPos(sender.chunk.x, sender.chunk.z)
        val world = sender.world

        val craftWorld = (world as CraftWorld).handle

        val populator: AsteroidPopulator =
            craftWorld.generator.getDefaultPopulators(world)
                .find { it is AsteroidPopulator } as? AsteroidPopulator ?: return

        val asteroid = AsteroidPopulator.Asteroid(
            BlockPos(
                sender.location.x,
                sender.location.y,
                sender.location.z
            ),
            configuration.blockPalettes[index],
            size,
            octaves
        )

        populator.apply {
            this.postGenerateAsteroid(
                world,
                chunkPos.x,
                chunkPos.z,
                asteroid
            )
        }

        sender.sendRichMessage("<#7fff7f>Success!")
    }

    @Suppress("unused")
    @Subcommand("create random")
    fun onCreateAsteroid(sender: Player) {
        val chunkPos = ChunkPos(sender.chunk.x, sender.chunk.z)
        val world = sender.world

        val asteroidRandom = Random(chunkPos.x + (chunkPos.z) + world.seed)

        val craftWorld = (world as CraftWorld).handle

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

        sender.sendRichMessage("<#7fff7f>Success!")
    }

    private fun postGenerateAsteroids(world: World, chunkPos: ChunkPos) {
        val asteroids = getChunkAsteroids(chunkPos.x, chunkPos.z)

        if (asteroids.isEmpty()) return

        val craftWorld = (world as CraftWorld).handle

        val populator: AsteroidPopulator =
            craftWorld.generator.getDefaultPopulators(world)
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