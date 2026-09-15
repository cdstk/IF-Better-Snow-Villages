package bettersnowvillages.compat.charm;

import bettersnowvillages.compat.IceAndFireForksUtil;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnowBlock;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import svenhjol.charm.world.decorator.outer.Erosion;
import svenhjol.charm.world.feature.VillageDecorations;

import java.util.List;
import java.util.Random;

public class SnowVillageErosion extends Erosion {

    public SnowVillageErosion(World world, BlockPos pos, Random rand, List<ChunkPos> chunks) {
        super(world, pos, rand, chunks);
    }

    @Override
    public void generate() {
        int max = VillageDecorations.erosionDamage + this.rand.nextInt(60);

        for(int i = 0; i < max; ++i) {
            int xx = this.rand.nextInt(16) + 8;
            int zz = this.rand.nextInt(16) + 8;
            BlockPos posToErode = this.world.getHeight(this.pos.add(xx, 0, zz)).down(1 + this.rand.nextInt(12));
            IBlockState state = this.world.getBlockState(posToErode);
            IBlockState newState = null;

            // Snow Pallet
            // Cobble to Frozen Cobble
            // Plank -> Snow
            // Log -> Packed Ice
            if (state.getBlock() instanceof BlockSnowBlock
                    || state.getBlock() instanceof BlockPlanks
                    || state.getBlock() instanceof BlockStairs
                    || state.getBlock() instanceof BlockFence)
            {
                if (this.rand.nextFloat() < 0.92F) {
                    newState = Blocks.AIR.getDefaultState();
                } else {
                    newState = IceAndFireForksUtil.getDragonIceSpikes().getDefaultState();
                }
            }

            if (state.getBlock() instanceof BlockSlab) {
                newState = Blocks.AIR.getDefaultState();
            }

            if (state.getBlock() == Blocks.COBBLESTONE) {
                if (this.rand.nextFloat() < 0.5F) {
                    newState = IceAndFireForksUtil.getFrozenCobblestone().getDefaultState();
                } else {
                    newState = IceAndFireForksUtil.getFrozenGravel().getDefaultState();
                }
            }

            if (state.getBlock() == IceAndFireForksUtil.getFrozenCobblestone() || newState == IceAndFireForksUtil.getFrozenCobblestone().getDefaultState()) {
                if (this.rand.nextFloat() < 0.5F) {
                    newState = IceAndFireForksUtil.getDragonIceSpikes().getDefaultState();
                } else {
                    newState = IceAndFireForksUtil.getFrozenGravel().getDefaultState();
                }
            }

            if (newState != null) {
                this.world.setBlockState(posToErode, newState);
            }
        }

    }
}
