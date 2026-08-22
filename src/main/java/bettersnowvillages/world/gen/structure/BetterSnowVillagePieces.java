package bettersnowvillages.world.gen.structure;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.handlers.ForgeConfigProvider;
import bettersnowvillages.util.IStructurePiecesVillagePieces_SnowVillageComponentMixin;
import bettersnowvillages.world.gen.componenthandlers.VillageSnowLimitedTorchCreationHandler;
import bettersnowvillages.world.gen.componenthandlers.VillageSnowPathCreationHandler;
import bettersnowvillages.world.gen.componenthandlers.VillageSnowWellCreationHandler;
import bettersnowvillages.world.gen.componenthandlers.VillageSnowWoodHutCreationHandler;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;
import java.util.Random;

public class BetterSnowVillagePieces {

    public static void registerVillagePieces() {
        MapGenStructureIO.registerStructureComponent(BetterSnowVillagePieces.SnowWell.class, "BSViW");
        MapGenStructureIO.registerStructureComponent(BetterSnowVillagePieces.SnowPath.class, "BSViSR");
        MapGenStructureIO.registerStructureComponent(BetterSnowVillagePieces.LimitedTorch.class, "BSViL");
        MapGenStructureIO.registerStructureComponent(BetterSnowVillagePieces.SnowWoodHut.class, "BSViSmH");

        VillagerRegistry.instance().registerVillageCreationHandler(new VillageSnowWellCreationHandler());
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageSnowPathCreationHandler());
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageSnowLimitedTorchCreationHandler());
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageSnowWoodHutCreationHandler());
    }

    public static List<StructureVillagePieces.PieceWeight> getStructureVillageWeightedPieceList(Random random, int size) {
        List<StructureVillagePieces.PieceWeight> list = Lists.newArrayList();
        list.add(new StructureVillagePieces.PieceWeight(LimitedTorch.class, 75, MathHelper.getInt(random, 5 + size, 6 + size)));
        list.add(new StructureVillagePieces.PieceWeight(SnowWoodHut.class, 65, MathHelper.getInt(random, 3 + size, 5 + size)));

        VillagerRegistry.addExtraVillageComponents(list, random, size);

        list.removeIf(pieceWeight -> (pieceWeight).villagePiecesLimit == 0);
        ForgeConfigProvider.removeDisabledSnowVillageComponents(list);

        return list;
    }

    private static StructureComponent generateAndAddSnowRoadPiece(StructureVillagePieces.Start start, List<StructureComponent> structureComponentList, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type) {
        if (type > 3 + start.terrainType) {
            return null;
        }
        else if (Math.abs(structureMinX - start.getBoundingBox().minX) <= 112 && Math.abs(structureMinZ - start.getBoundingBox().minZ) <= 112) {
            StructureBoundingBox structureboundingbox = StructureVillagePieces.Path.findPieceBox(start, structureComponentList, rand, structureMinX, structureMinY, structureMinZ, facing);

            if (structureboundingbox != null && structureboundingbox.minY > 10) {
                StructureComponent structurecomponent = new BetterSnowVillagePieces.SnowPath(start, type, rand, structureboundingbox, facing);
                structureComponentList.add(structurecomponent);
                start.pendingRoads.add(structurecomponent);
                return structurecomponent;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public static class SnowPath extends StructureVillagePieces.Road {

        private int length;

        public SnowPath() {}

        public SnowPath(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing) {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = structurebb;
            this.length = Math.max(structurebb.getXSize(), structurebb.getZSize());
        }

        protected void writeStructureToNBT(NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setInteger("Length", this.length);
        }

        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.length = tagCompound.getInteger("Length");
        }

        @Override
        public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
            boolean hasNext = false;
            StructureVillagePieces.Start start = (StructureVillagePieces.Start) componentIn;

            for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5)) {
                StructureComponent nextComponentNN = this.getNextComponentNN(start, listIn, rand, 0, i);

                if (nextComponentNN != null) {
                    i += Math.max(nextComponentNN.getBoundingBox().getXSize(), nextComponentNN.getBoundingBox().getZSize());
                    hasNext = true;
                }
            }

            for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5)) {
                StructureComponent nextComponentPP = this.getNextComponentPP(start, listIn, rand, 0, j);

                if (nextComponentPP != null) {
                    j += Math.max(nextComponentPP.getBoundingBox().getXSize(), nextComponentPP.getBoundingBox().getZSize());
                    hasNext = true;
                }
            }

            EnumFacing enumfacing = this.getCoordBaseMode();

            if (hasNext && rand.nextInt(3) > 0 && enumfacing != null) {
                switch (enumfacing) {
                    case NORTH:
                    default:
                        BetterSnowVillagePieces.generateAndAddSnowRoadPiece((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, this.getComponentType());
                        break;
                    case SOUTH:
                        BetterSnowVillagePieces.generateAndAddSnowRoadPiece((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
                        break;
                    case WEST:
                        BetterSnowVillagePieces.generateAndAddSnowRoadPiece((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                        break;
                    case EAST:
                        BetterSnowVillagePieces.generateAndAddSnowRoadPiece((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                }
            }

            if (hasNext && rand.nextInt(3) > 0 && enumfacing != null) {
                switch (enumfacing) {
                    case NORTH:
                    default:
                        BetterSnowVillagePieces.generateAndAddSnowRoadPiece((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
                        break;
                    case SOUTH:
                        BetterSnowVillagePieces.generateAndAddSnowRoadPiece((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
                        break;
                    case WEST:
                        BetterSnowVillagePieces.generateAndAddSnowRoadPiece((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                        break;
                    case EAST:
                        BetterSnowVillagePieces.generateAndAddSnowRoadPiece((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                }
            }
        }

        @Override
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            IBlockState commonPathBlock = this.getBiomeSpecificBlockState(IafBlockRegistry.frozenGrassPath.getDefaultState());
            IBlockState aquaticPathBlock = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            IBlockState sandyPathBlock = this.getBiomeSpecificBlockState(Blocks.GRAVEL.getDefaultState());
            IBlockState sandySupportBlock = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());

            for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
                for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
                    BlockPos blockpos = new BlockPos(i, 64, j);

                    if (structureBoundingBoxIn.isVecInside(blockpos)) {
                        blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos).down();

                        if (blockpos.getY() < worldIn.getSeaLevel()) {
                            blockpos = new BlockPos(blockpos.getX(), worldIn.getSeaLevel() - 1, blockpos.getZ());
                        }

                        while (blockpos.getY() >= worldIn.getSeaLevel() - 1) {
                            IBlockState replaceTarget = worldIn.getBlockState(blockpos);
                            if(replaceTarget.getBlock() == Blocks.SNOW_LAYER || replaceTarget.getMaterial() == Material.PLANTS) {
                                replaceTarget = worldIn.getBlockState(blockpos.down());
                            }

                            if (replaceTarget.getBlock() == Blocks.GRASS || replaceTarget.getBlock() == Blocks.SNOW) {
                                IBlockState aboveTarget = worldIn.getBlockState(blockpos.up());
                                if(aboveTarget.getBlock() == Blocks.SNOW_LAYER || aboveTarget.getMaterial() == Material.PLANTS)
                                    worldIn.setBlockToAir(blockpos.up());
                                worldIn.setBlockState(blockpos, commonPathBlock, 2);
                                break;
                            }

                            if (replaceTarget.getMaterial().isLiquid() || replaceTarget.getBlock() == Blocks.ICE) {
                                worldIn.setBlockState(blockpos, aquaticPathBlock, 2);
                                break;
                            }

                            if (replaceTarget.getBlock() == Blocks.SAND || replaceTarget.getBlock() == Blocks.SANDSTONE || replaceTarget.getBlock() == Blocks.RED_SANDSTONE) {
                                worldIn.setBlockState(blockpos, sandyPathBlock, 2);
                                worldIn.setBlockState(blockpos.down(), sandySupportBlock, 2);
                                break;
                            }

                            blockpos = blockpos.down();
                        }
                    }
                }
            }

            return true;
        }
    }

    public static class SnowWell extends StructureVillagePieces.Start {

        public SnowWell() {}

        /** Use Type 3 for Spruce Pallet **/
        public SnowWell(BiomeProvider biomeProviderIn, int type, Random rand, int x, int z, List<StructureVillagePieces.PieceWeight> pieceWeightList, int size) {
            super(biomeProviderIn, type, rand, x, z, pieceWeightList, size);
            if(this.structureType == 0)
                this.structureType = type;
        }

        @Override
        public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
            BetterSnowVillagePieces.generateAndAddSnowRoadPiece((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.WEST, this.getComponentType());
            BetterSnowVillagePieces.generateAndAddSnowRoadPiece((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.EAST, this.getComponentType());
            BetterSnowVillagePieces.generateAndAddSnowRoadPiece((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
            BetterSnowVillagePieces.generateAndAddSnowRoadPiece((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
        }

        @Override
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 3, 0);
            }

            IBlockState commonPath = this.getBiomeSpecificBlockState(IafBlockRegistry.frozenGrassPath.getDefaultState());
            IBlockState ice = this.getBiomeSpecificBlockState(Blocks.PACKED_ICE.getDefaultState());
            IBlockState wellWater = ModLoadedUtil.CHARM.isLoaded() ? Blocks.AIR.getDefaultState() : Blocks.FLOWING_WATER.getDefaultState();
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 4, 12, 4, ice, Blocks.FLOWING_WATER.getDefaultState(), false);
            this.setBlockState(worldIn, wellWater, 2, 12, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, wellWater, 3, 12, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, wellWater, 2, 12, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, wellWater, 3, 12, 3, structureBoundingBoxIn);

            // Charm injects carpet so just make vanilla well
            if(ModLoadedUtil.CHARM.isLoaded()) {
                IBlockState fence = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
                IBlockState roof = this.getBiomeSpecificBlockState(IafBlockRegistry.frozenCobblestone.getDefaultState());
                this.setBlockState(worldIn, fence, 1, 13, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, fence, 1, 14, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, fence, 4, 13, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, fence, 4, 14, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, fence, 1, 13, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, fence, 1, 14, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, fence, 4, 13, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, fence, 4, 14, 4, structureBoundingBoxIn);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 15, 1, 4, 15, 4, roof, roof, false);
            }
            else {
                IBlockState torch = this.getBiomeSpecificBlockState(Blocks.TORCH.getDefaultState());
                this.setBlockState(worldIn, torch, 1, 12, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, torch, 4, 12, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, torch, 1, 12, 4, structureBoundingBoxIn);
                this.setBlockState(worldIn, torch, 4, 12, 4, structureBoundingBoxIn);
            }

            for (int i = 0; i <= 5; ++i) {
                for (int j = 0; j <= 5; ++j) {
                    if (j == 0 || j == 5 || i == 0 || i == 5) {
                        this.setBlockState(worldIn, commonPath, j, 11, i, structureBoundingBoxIn);
                        this.clearCurrentPositionBlocksUpwards(worldIn, j, 12, i, structureBoundingBoxIn);
                    }
                }
            }

            return true;
        }
    }

    public static class LimitedTorch extends StructureVillagePieces.Village {

        public static LimitedTorch createPiece(StructureVillagePieces.Start start, List<StructureComponent> structureComponentList, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(
                    structureMinX, structureMinY, structureMinZ,
                    0, 0, 0, 4, 6, 5, facing);
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(structureComponentList, structureboundingbox) == null
                    ? new LimitedTorch(start, type, rand, structureboundingbox, facing)
                    : null;
        }

        public LimitedTorch() {}

        public LimitedTorch(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing)
        {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = structurebb;
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 5, 0);
            }

            IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.SPRUCE_FENCE.getDefaultState());
            this.setBlockState(worldIn, iblockstate, 1, 0, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 1, 1, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 1, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLACK), 1, 3, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.EAST, 2, 3, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.NORTH, 1, 3, 1, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.WEST, 0, 3, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.SOUTH, 1, 3, -1, structureBoundingBoxIn);
            return true;
        }
    }

    public static class SnowWoodHut extends StructureVillagePieces.Village {

        public static SnowWoodHut createPiece(StructureVillagePieces.Start start, List<StructureComponent> structureComponentList, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(
                    structureMinX, structureMinY, structureMinZ,
                    0, 0, 0, 4, 6, 5, facing);
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(structureComponentList, structureboundingbox) == null
                    ? new SnowWoodHut(start, type, rand, structureboundingbox, facing)
                    : null;
        }

        protected boolean isTallHouse;
        protected int tablePosition;

        public SnowWoodHut() {}

        public SnowWoodHut(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing) {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = structurebb;
            this.isTallHouse = rand.nextBoolean();
            this.tablePosition = rand.nextInt(3);
        }

        protected void writeStructureToNBT(NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setInteger("T", this.tablePosition);
            tagCompound.setBoolean("C", this.isTallHouse);
        }

        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
            super.readStructureFromNBT(tagCompound, templateManager);
            this.tablePosition = tagCompound.getInteger("T");
            this.isTallHouse = tagCompound.getBoolean("C");
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
            }

            IBlockState foundationBlock = this.getBiomeSpecificBlockState(IafBlockRegistry.frozenCobblestone.getDefaultState());
            IBlockState wallFaceBlock = this.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState());
            IBlockState stairBlock = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState wallCornerBlock = this.getBiomeSpecificBlockState(Blocks.PACKED_ICE.getDefaultState());
            IBlockState tableStandBlock = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 3, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 3, 0, 4, foundationBlock, foundationBlock, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 0, 3, Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false);

            if (this.isTallHouse) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 2, 4, 3, wallCornerBlock, wallCornerBlock, false);
            }
            else {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 1, 2, 5, 3, wallCornerBlock, wallCornerBlock, false);
            }

            this.setBlockState(worldIn, wallCornerBlock, 1, 4, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, wallCornerBlock, 2, 4, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, wallCornerBlock, 1, 4, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, wallCornerBlock, 2, 4, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, wallCornerBlock, 0, 4, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, wallCornerBlock, 0, 4, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, wallCornerBlock, 0, 4, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, wallCornerBlock, 3, 4, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, wallCornerBlock, 3, 4, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, wallCornerBlock, 3, 4, 3, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 3, 0, wallCornerBlock, wallCornerBlock, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 3, 3, 0, wallCornerBlock, wallCornerBlock, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 4, 0, 3, 4, wallCornerBlock, wallCornerBlock, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 4, 3, 3, 4, wallCornerBlock, wallCornerBlock, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 3, wallFaceBlock, wallFaceBlock, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 1, 3, 3, 3, wallFaceBlock, wallFaceBlock, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 2, 3, 0, wallFaceBlock, wallFaceBlock, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 4, 2, 3, 4, wallFaceBlock, wallFaceBlock, false);
            this.setBlockState(worldIn, Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.LIGHT_BLUE), 0, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.LIGHT_BLUE), 3, 2, 2, structureBoundingBoxIn);

            if (this.tablePosition > 0) {
                this.setBlockState(worldIn, tableStandBlock, this.tablePosition, 1, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), this.tablePosition, 2, 3, structureBoundingBoxIn);
            }

            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 1, 1, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 1, 2, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.NORTH, 1, 3, 1, structureBoundingBoxIn);
            this.createVillageDoor(worldIn, structureBoundingBoxIn, randomIn, 1, 1, 0, EnumFacing.NORTH);

            if (this.getBlockStateFromPos(worldIn, 1, 0, -1, structureBoundingBoxIn).getMaterial() == Material.AIR && this.getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getMaterial() != Material.AIR) {
                this.setBlockState(worldIn, stairBlock, 1, 0, -1, structureBoundingBoxIn);

                if (this.getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getBlock() == IafBlockRegistry.frozenGrassPath) {
                    this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), 1, -1, -1, structureBoundingBoxIn);
                }
            }

            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 4; ++j) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, j, 6, i, structureBoundingBoxIn);
                    this.replaceAirAndLiquidDownwards(worldIn, foundationBlock, j, -1, i, structureBoundingBoxIn);
                }
            }

            if(this instanceof IStructurePiecesVillagePieces_SnowVillageComponentMixin) {
                ((IStructurePiecesVillagePieces_SnowVillageComponentMixin) this).bettersnowvillages$spawnSnowVillagers(worldIn, structureBoundingBoxIn, 1, 1, 2, 1);
            }
            else {
                this.spawnVillagers(worldIn, structureBoundingBoxIn, 1, 1, 2, 1);
                BetterSnowVillages.LOGGER.warn("Failed to spawn Ice and Fire Snow Villager in custom Structure Component");
            }
            return true;
        }
    }
}
