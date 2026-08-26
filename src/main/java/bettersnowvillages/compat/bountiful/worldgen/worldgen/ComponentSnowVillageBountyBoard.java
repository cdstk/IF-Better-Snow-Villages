package bettersnowvillages.compat.bountiful.worldgen.worldgen;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.compat.QuarkUtil;
import com.github.alexthe666.iceandfire.world.village.SnowVillagePieces;
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
import java.util.Random;
import java.util.Set;

public class ComponentSnowVillageBountyBoard extends SnowVillagePieces.Village {
    public static final ResourceLocation VANILlA_BOARD_ID = new ResourceLocation(BetterSnowVillages.MODID, "snow_village_board_vanilla");
    public static final ResourceLocation QUARK_BOARD_ID = new ResourceLocation(BetterSnowVillages.MODID, "snow_village_board_quark");

    public ComponentSnowVillageBountyBoard() {}

    public ComponentSnowVillageBountyBoard(SnowVillagePieces.Start start, int type, StructureBoundingBox boundingBox, EnumFacing facing) {
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
        if (averageGroundLvl < 0) {
            averageGroundLvl = getAverageGroundLevel(world, structureBoundingBoxIn);
            if (averageGroundLvl < 0) {
                return true;
            }
            this.boundingBox.offset(0, averageGroundLvl - this.boundingBox.minY - 1, 0);
        }
        BlockPos pos = new BlockPos(this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ);
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = (new PlacementSettings()).setReplacedBlock(Blocks.STRUCTURE_VOID).setBoundingBox(structureBoundingBoxIn);

        ResourceLocation resourceLocation;
        if(ModLoadedUtil.QUARK.isLoaded() && QuarkUtil.isSnowBrickEnabled()) {
            Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(world.getBiome(pos));
            resourceLocation = types.contains(BiomeDictionary.Type.FOREST)
                    ? VANILlA_BOARD_ID
                    : QUARK_BOARD_ID;
        }
        else {
            resourceLocation = VANILlA_BOARD_ID;
        }
        Template template = templateManager.getTemplate(world.getMinecraftServer(), resourceLocation);
        template.addBlocksToWorldChunk(world, pos, settings);
        return true;
    }

    public static ComponentSnowVillageBountyBoard createPiece(SnowVillagePieces.Start start, List<StructureComponent> structureComponentList, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(
                structureMinX, structureMinY, structureMinZ,
                0, 0, 0,
                3, 3, 3,
                facing
        );
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(structureComponentList, structureboundingbox) == null
                ? new ComponentSnowVillageBountyBoard(start, type, structureboundingbox, facing)
                : null;
    }
}
