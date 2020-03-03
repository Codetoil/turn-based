package io.github.codetoil.turnbased.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ChunkGeneratorTurnBased implements IChunkGenerator {

	private final World world;

	public ChunkGeneratorTurnBased(World worldIn) {
		this.world = worldIn;
	}

	@Override
	public Chunk generateChunk(int x, int z) {
		ChunkPrimer chunkprimer = new ChunkPrimer();
		IBlockState bedrockDefaultState = Blocks.BEDROCK.getDefaultState();
		IBlockState dirtDefaultState = Blocks.DIRT.getDefaultState();
		WorldServer worldOver = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);

		for (int j = 0; j < 16; ++j) {
			for (int k = 0; k < 16; ++k) {
				chunkprimer.setBlockState(j, 0, k, bedrockDefaultState);
				chunkprimer.setBlockState(j, 1, k, dirtDefaultState);
				chunkprimer.setBlockState(j, 2, k, dirtDefaultState);
				BlockPos blockPos = worldOver.getTopSolidOrLiquidBlock(new BlockPos(16 * x + j, 0, 16 * z + k));
				if (worldOver.getBlockState(blockPos).getMaterial().isLiquid()) {
					chunkprimer.setBlockState(j, 3, k, worldOver.getBlockState(blockPos));
				} else {
					chunkprimer.setBlockState(j, 3, k, worldOver.getBlockState(blockPos.down()));
				}

			}
		}

		Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
		Biome[] abiome = this.world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16);
		byte[] abyte = chunk.getBiomeArray();

		for (int l = 0; l < abyte.length; ++l) {
			abyte[l] = (byte) Biome.getIdForBiome(abiome[l]);
		}

		chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public void populate(int x, int z) {

	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		return false;
	}

	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		return new ArrayList<>();
	}

	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {

	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
		return false;
	}
}
