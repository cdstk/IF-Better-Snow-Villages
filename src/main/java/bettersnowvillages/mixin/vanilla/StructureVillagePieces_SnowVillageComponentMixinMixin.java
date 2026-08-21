package bettersnowvillages.mixin.vanilla;

import bettersnowvillages.util.IStructurePiecesVillagePieces_SnowVillageComponentMixin;
import bettersnowvillages.wrapper.BetterSnowVillagesWrapper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(StructureVillagePieces.Village.class)
public abstract class StructureVillagePieces_SnowVillageComponentMixinMixin extends StructureComponent implements IStructurePiecesVillagePieces_SnowVillageComponentMixin {

    @Shadow private int villagersSpawned;

    @Shadow protected boolean isZombieInfested;

    @Unique
    @Override
    public void bettersnowvillages$spawnSnowVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count) {
        if (this.villagersSpawned < count) {
            for (int i = this.villagersSpawned; i < count; ++i) {
                int j = this.getXWithOffset(x + i, z);
                int k = this.getYWithOffset(y);
                int l = this.getZWithOffset(x + i, z);

                if (!structurebb.isVecInside(new BlockPos(j, k, l))) {
                    break;
                }

                ++this.villagersSpawned;

                if (this.isZombieInfested) {
                    EntityLiving infestedMob = BetterSnowVillagesWrapper.newInfestedMobInstance(worldIn);
                    infestedMob.setLocationAndAngles((double) j + 0.5D, k, (double) l + 0.5D, 0.0F, 0.0F);
                    infestedMob.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(infestedMob)), null);
                    infestedMob.enablePersistence();
                    worldIn.spawnEntity(infestedMob);
                } else {
                    EntityVillager entityVillager = BetterSnowVillagesWrapper.newSnowVillagerInstance(worldIn);
                    entityVillager.setLocationAndAngles((double) j + 0.5D, k, (double) l + 0.5D, 0.0F, 0.0F);
                    entityVillager.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityVillager)), null);
                    BetterSnowVillagesWrapper.setRandomSnowProfession(entityVillager, worldIn.rand);
                    worldIn.spawnEntity(entityVillager);
                }
            }
        }
    }
}
