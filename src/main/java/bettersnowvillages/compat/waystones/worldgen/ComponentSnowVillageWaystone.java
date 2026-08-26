package bettersnowvillages.compat.waystones.worldgen;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.compat.QuarkUtil;
import com.github.alexthe666.iceandfire.world.village.SnowVillagePieces;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.block.BlockWaystone;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ComponentSnowVillageWaystone extends SnowVillagePieces.Village {
    public static final ResourceLocation VANILlA_WAYSTONE_ID = new ResourceLocation(BetterSnowVillages.MODID, "snow_village_waystone_vanilla");
    public static final ResourceLocation QUARK_WAYSTONE_ID = new ResourceLocation(BetterSnowVillages.MODID, "snow_village_waystone_quark");

    public ComponentSnowVillageWaystone() {}

    public ComponentSnowVillageWaystone(SnowVillagePieces.Start start, int type, StructureBoundingBox boundingBox, EnumFacing facing) {
        super(start, type);
        this.boundingBox = boundingBox;
        setCoordBaseMode(facing);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
        super.readStructureFromNBT(tagCompound);
    }

    @Override
    public boolean addComponentParts(World world, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(world, structureBoundingBoxIn);

            if (this.averageGroundLvl < 0) {
                return true;
            }

            this.boundingBox.offset(0, averageGroundLvl - this.boundingBox.minY, 0);
        }
        BlockPos pos = new BlockPos(this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ);
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = (new PlacementSettings()).setReplacedBlock(Blocks.STRUCTURE_VOID).setBoundingBox(structureBoundingBoxIn);

        ResourceLocation resourceLocation;
        if(ModLoadedUtil.QUARK.isLoaded() && QuarkUtil.isSnowBrickEnabled()) {
            Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(world.getBiome(pos));
            resourceLocation = types.contains(BiomeDictionary.Type.FOREST)
                    ? VANILlA_WAYSTONE_ID
                    : QUARK_WAYSTONE_ID;
        }
        else {
            resourceLocation = VANILlA_WAYSTONE_ID;
        }
        Template template = templateManager.getTemplate(world.getMinecraftServer(), resourceLocation);

        template.addBlocksToWorldChunk(world, pos, settings);
        Map<BlockPos, String> dataBlocks = template.getDataBlocks(pos, settings);
        for (Map.Entry<BlockPos, String> entry : dataBlocks.entrySet()) {
            if ("Waystone".equals(entry.getValue())) {
                world.setBlockState(entry.getKey(), Waystones.blockWaystone.getDefaultState().withProperty(BlockWaystone.BASE, true), 3);
                world.setBlockState(entry.getKey().up(), Waystones.blockWaystone.getDefaultState().withProperty(BlockWaystone.BASE, false), 3);
            }
        }
        return true;
    }

    public static ComponentSnowVillageWaystone createPiece(SnowVillagePieces.Start start, List<StructureComponent> structureComponentList, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(
                structureMinX, structureMinY, structureMinZ,
                0, 0, 0,
                7, 6, 7,
                facing
        );
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(structureComponentList, structureboundingbox) == null
                ? new ComponentSnowVillageWaystone(start, type, structureboundingbox, facing)
                : null;
    }
}
