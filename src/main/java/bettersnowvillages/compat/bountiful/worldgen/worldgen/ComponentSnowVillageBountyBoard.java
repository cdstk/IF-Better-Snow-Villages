package bettersnowvillages.compat.bountiful.worldgen.worldgen;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.compat.IceAndFireForksUtil;
import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.compat.QuarkUtil;
import bettersnowvillages.world.gen.structure.MapGenBetterSnowVillage;
import com.github.alexthe666.iceandfire.world.village.SnowVillagePieces;
import ejektaflex.bountiful.worldgen.VillageBoardComponent;
import ejektaflex.bountiful.worldgen.VillageBoardProcessor;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
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
//    public static final ResourceLocation VANILlA_BOARD_ID = new ResourceLocation("bountiful", "village_board");
//    public static final ResourceLocation QUARK_BOARD_ID = new ResourceLocation("bountiful", "village_board");
    public static final ResourceLocation VANILlA_BOARD_ID = new ResourceLocation(BetterSnowVillages.MODID, "snow_village_board_vanilla");
    public static final ResourceLocation QUARK_BOARD_ID = new ResourceLocation(BetterSnowVillages.MODID, "snow_village_board_quark");

    private Rotation boardRotation = Rotation.NONE;
    private BlockPos boardShift = new BlockPos(0,0,0);

    public ComponentSnowVillageBountyBoard() {}

    public ComponentSnowVillageBountyBoard(SnowVillagePieces.Start start, int type, StructureBoundingBox boundingBox, EnumFacing facing) {
        super(start, type);
        this.boundingBox = boundingBox;
        setCoordBaseMode(facing);

        //Template rotation and position tweaking
        if (facing != null) {
            switch (facing) {
                case SOUTH:
                    this.boardRotation = Rotation.CLOCKWISE_180;
                    this.boardShift = new BlockPos(6,0,6);
                    break;
                case WEST:
                    this.boardRotation = Rotation.COUNTERCLOCKWISE_90;
                    this.boardShift = new BlockPos(0,0,6);
                    break;
                case EAST:
                    this.boardRotation = Rotation.CLOCKWISE_90;
                    this.boardShift = new BlockPos(6,0,0);
                    break;
            }
        }
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
            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - 1, 0);
        }

        if(IceAndFireForksUtil.isStructureInsideMausoleum(world, this.boundingBox)) {
            return true;
        }

        BlockPos pos = new BlockPos(this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ);
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = (new PlacementSettings()).setReplacedBlock(Blocks.STRUCTURE_VOID).setBoundingBox(structureBoundingBoxIn).setRotation(this.boardRotation)  ;

        Template template = templateManager.getTemplate(world.getMinecraftServer(), this.getTemplateResourceLocation(world, pos));

        if(ModLoadedUtil.versionInRange(ModLoadedUtil.BOUNTIFUL, ModLoadedUtil.BOUNTIFUL_LATEST)) {
            IBlockState pathState = this.getBiomeSpecificBlockState(IceAndFireForksUtil.getFrozenGrassPath().getDefaultState());
            IBlockState planksState = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE));
            IBlockState gravelState = this.getBiomeSpecificBlockState(Blocks.GRAVEL.getDefaultState());
            IBlockState cobbleState = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            VillageBoardProcessor processor = new VillageBoardProcessor(pos, settings, pathState, planksState, gravelState, cobbleState);
            template.addBlocksToWorld(world, pos.add(this.boardShift), processor, settings, 2);
        }
        else {
            template.addBlocksToWorld(world, pos.add(this.boardShift), settings);
        }
        return true;
    }

    public ResourceLocation getTemplateResourceLocation(World world, BlockPos pos) {
        if(world.getBiomeProvider().areBiomesViable(pos.getX(), pos.getZ(), 0, MapGenBetterSnowVillage.SNOW_VILLAGE_SPAWN_BIOMES)) { // Can't do snow well check bc idk how to kotlin mixin
            if (ModLoadedUtil.QUARK.isLoaded() && QuarkUtil.isSnowBrickEnabled()) {
                Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(world.getBiome(pos));
                return types.contains(BiomeDictionary.Type.FOREST)
                        ? VANILlA_BOARD_ID
                        : QUARK_BOARD_ID;
            }
            else {
                return  VANILlA_BOARD_ID;
            }
        }
        return VillageBoardComponent.Companion.getVILLAGE_BOARD_ID();
    }

    public static ComponentSnowVillageBountyBoard createPiece(SnowVillagePieces.Start start, List<StructureComponent> structureComponentList, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(
                structureMinX, structureMinY, structureMinZ,
                0, 0, 0,
                7, 6, 7,
                facing
        );
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(structureComponentList, structureboundingbox) == null
                ? new ComponentSnowVillageBountyBoard(start, type, structureboundingbox, facing)
                : null;
    }
}
