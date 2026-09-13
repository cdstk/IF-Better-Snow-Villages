package bettersnowvillages.world.gen.structure;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.compat.IceAndFireForksUtil;
import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.config.ForgeConfigProvider;
import bettersnowvillages.util.IStructurePiecesVillagePieces_SnowVillageComponentMixin;
import bettersnowvillages.world.gen.componenthandlers.VillageNBTCreationHandler;
import bettersnowvillages.world.gen.componenthandlers.VillageSnowExtraTorchCreationHandler;
import bettersnowvillages.world.gen.componenthandlers.VillageSnowHouse3CreationHandler;
import bettersnowvillages.world.gen.componenthandlers.VillageSnowHouse4CreationHandler;
import bettersnowvillages.world.gen.componenthandlers.VillageSnowPathCreationHandler;
import bettersnowvillages.world.gen.componenthandlers.VillageSnowWellCreationHandler;
import bettersnowvillages.world.gen.componenthandlers.VillageSnowWoodHutCreationHandler;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
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
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;
import java.util.Random;

public class BetterSnowVillagePieces {

    public static void registerVillagePieces() {
//        MapGenStructureIO.registerStructureComponent(BetterSnowVillagePieces.SnowWell.class, "BSViW");
//        MapGenStructureIO.registerStructureComponent(BetterSnowVillagePieces.SnowPath.class, "BSViSR");
//        MapGenStructureIO.registerStructureComponent(BetterSnowVillagePieces.ExtraTorch.class, "BSViL");
//        MapGenStructureIO.registerStructureComponent(BetterSnowVillagePieces.SnowWoodHut.class, "BSViSmH");
//        MapGenStructureIO.registerStructureComponent(BetterSnowVillagePieces.SnowHouse3.class, "BSViTRH");
//        MapGenStructureIO.registerStructureComponent(BetterSnowVillagePieces.SnowHouse4.class, "BSViSH");

        VillagerRegistry.instance().registerVillageCreationHandler(new VillageSnowWellCreationHandler());
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageSnowPathCreationHandler());
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageSnowExtraTorchCreationHandler());
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageSnowWoodHutCreationHandler());
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageSnowHouse3CreationHandler());
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageSnowHouse4CreationHandler());

        VillagerRegistry.instance().registerVillageCreationHandler(new VillageNBTCreationHandler()); // Prevent a crash if mixin fails
    }

    public static List<StructureVillagePieces.PieceWeight> getStructureVillageWeightedPieceList(Random random, int size) {
        List<StructureVillagePieces.PieceWeight> list = Lists.newArrayList();

        ForgeConfigProvider.getSVClassGen().forEach((clazz, coreGenInfo) -> {
            int randomCount;
            if(coreGenInfo.min < 0) {
                randomCount = MathHelper.getInt(random, size, coreGenInfo.max + size - coreGenInfo.min);
                randomCount = MathHelper.clamp(randomCount + coreGenInfo.min, 0, coreGenInfo.max);
            }
            else {
                randomCount = MathHelper.getInt(random, coreGenInfo.min + size, coreGenInfo.max + size);
            }
            list.add(new StructureVillagePieces.PieceWeight(clazz, coreGenInfo.weight, randomCount));
        });

        ForgeConfigProvider.getSVNBTGen().forEach((structureID, nbtGenInfo) -> {
            int randomCount;
            if(nbtGenInfo.min < 0) {
                randomCount = MathHelper.getInt(random, size, nbtGenInfo.max + size - nbtGenInfo.min);
                randomCount = MathHelper.clamp(randomCount + nbtGenInfo.min, 0, nbtGenInfo.max);
            }
            else {
                randomCount = MathHelper.getInt(random, nbtGenInfo.min + size, nbtGenInfo.max + size);
            }
            list.add(new VillageNBTPieceWeight(structureID, nbtGenInfo.weight, randomCount,
                    nbtGenInfo.bbXMax, nbtGenInfo.bbYMax, nbtGenInfo.bbZMax,
                    nbtGenInfo.xOffset, nbtGenInfo.yOffset, nbtGenInfo.zOffset)
            );
        });

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
            IBlockState commonPathBlock = this.getBiomeSpecificBlockState(IceAndFireForksUtil.getFrozenGrassPath().getDefaultState());
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
                                blockpos = blockpos.down();
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

            IBlockState commonPath = this.getBiomeSpecificBlockState(IceAndFireForksUtil.getFrozenGrassPath().getDefaultState());
            IBlockState ice = this.getBiomeSpecificBlockState(Blocks.PACKED_ICE.getDefaultState());
            IBlockState wellWater = Blocks.FLOWING_WATER.getDefaultState();
//            IBlockState wellWater = ModLoadedUtil.CHARM.isLoaded() ? Blocks.AIR.getDefaultState() : Blocks.FLOWING_WATER.getDefaultState();
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 4, 12, 4, ice, Blocks.FLOWING_WATER.getDefaultState(), false);
            this.setBlockState(worldIn, wellWater, 2, 12, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, wellWater, 3, 12, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, wellWater, 2, 12, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, wellWater, 3, 12, 3, structureBoundingBoxIn);

            // Charm injects carpet so just make vanilla well
//            if(ModLoadedUtil.CHARM.isLoaded()) {
//                IBlockState fence = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
//                IBlockState roof = this.getBiomeSpecificBlockState(IceAndFireForksUtil.getFrozenCobblestone().getDefaultState());
//                this.setBlockState(worldIn, fence, 1, 13, 1, structureBoundingBoxIn);
//                this.setBlockState(worldIn, fence, 1, 14, 1, structureBoundingBoxIn);
//                this.setBlockState(worldIn, fence, 4, 13, 1, structureBoundingBoxIn);
//                this.setBlockState(worldIn, fence, 4, 14, 1, structureBoundingBoxIn);
//                this.setBlockState(worldIn, fence, 1, 13, 4, structureBoundingBoxIn);
//                this.setBlockState(worldIn, fence, 1, 14, 4, structureBoundingBoxIn);
//                this.setBlockState(worldIn, fence, 4, 13, 4, structureBoundingBoxIn);
//                this.setBlockState(worldIn, fence, 4, 14, 4, structureBoundingBoxIn);
//                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 15, 1, 4, 15, 4, roof, roof, false);
//            }
             {
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

    public static class ExtraTorch extends StructureVillagePieces.Torch {

        public static ExtraTorch createPiece(StructureVillagePieces.Start start, List<StructureComponent> structureComponentList, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(
                    structureMinX, structureMinY, structureMinZ,
                    0, 0, 0,
                    3, 4, 2,
                    facing
            );
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(structureComponentList, structureboundingbox) == null
                    ? new ExtraTorch(start, type, rand, structureboundingbox, facing)
                    : null;
        }

        public ExtraTorch() {}

        public ExtraTorch(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing) {
            super(start, type, rand, structurebb, facing);
        }

        @Override
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            boolean result = super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
            if (this.averageGroundLvl < 0) {
                return true;
            }

            // Lamp Color
            this.setBlockState(worldIn, Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLACK), 1, 3, 0, structureBoundingBoxIn);
            return result;
        }
    }

    public static class SnowWoodHut extends StructureVillagePieces.WoodHut {

        public static SnowWoodHut createPiece(StructureVillagePieces.Start start, List<StructureComponent> structureComponentList, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(
                    structureMinX, structureMinY, structureMinZ,
                    0, 0, 0,
                    4, 6, 5,
                    facing
            );
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(structureComponentList, structureboundingbox) == null
                    ? new SnowWoodHut(start, type, rand, structureboundingbox, facing)
                    : null;
        }

        public SnowWoodHut() {}

        public SnowWoodHut(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing) {
            super(start, type, rand, structurebb, facing);
        }

        @Override
        protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count) {
            if(this instanceof IStructurePiecesVillagePieces_SnowVillageComponentMixin) {
                ((IStructurePiecesVillagePieces_SnowVillageComponentMixin) this).bettersnowvillages$spawnSnowVillagers(worldIn, boundingBox, x, y, z, count);
            }
            else {
                this.spawnVillagers(worldIn, structurebb, x, y, z, count);
                BetterSnowVillages.LOGGER.warn("Failed to spawn Ice and Fire Snow Villager in custom Structure Component");
            }
        }

        @Override
        protected IBlockState getBiomeSpecificBlockState(IBlockState blockstateIn) {
            blockstateIn = WorldGenBetterSnowVillage.getBasicSnowyPalletSwap(blockstateIn);
            return super.getBiomeSpecificBlockState(blockstateIn);
        }

        @Override
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            boolean result = super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
            if (this.averageGroundLvl < 0) {
                return true;
            }

            // Window Color
            this.setBlockState(worldIn, Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.LIGHT_BLUE), 0, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.LIGHT_BLUE), 3, 2, 2, structureBoundingBoxIn);

            // Add Torch above door
            this.placeTorch(worldIn, EnumFacing.NORTH, 1, 3, 1, structureBoundingBoxIn);

            if (this.getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getBlock() == IceAndFireForksUtil.getFrozenGrassPath()) {
                this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), 1, -1, -1, structureBoundingBoxIn);
            }
            return result;
        }
    }

    public static class SnowHouse3 extends StructureVillagePieces.House3 {

        public static SnowHouse3 createPiece(StructureVillagePieces.Start start, List<StructureComponent> structureComponentList, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(
                    structureMinX, structureMinY, structureMinZ,
                    0, 0, 0,
                    9, 7, 12,
                    facing
            );
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(structureComponentList, structureboundingbox) == null
                    ? new SnowHouse3(start, type, rand, structureboundingbox, facing)
                    : null;
        }

        public SnowHouse3() {}

        public SnowHouse3(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing) {
            super(start, type, rand, structurebb, facing);
        }

//        @Override
//        protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count) {
//            if(this instanceof IStructurePiecesVillagePieces_SnowVillageComponentMixin) {
//                ((IStructurePiecesVillagePieces_SnowVillageComponentMixin) this).bettersnowvillages$spawnSnowVillagers(worldIn, boundingBox, x, y, z, count);
//            }
//            else {
//                this.spawnVillagers(worldIn, structurebb, x, y, z, count);
//                BetterSnowVillages.LOGGER.warn("Failed to spawn Ice and Fire Snow Villager in custom Structure Component");
//            }
//        }

        @Override
        protected IBlockState getBiomeSpecificBlockState(IBlockState blockstateIn) {
            if(blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                IBlockState sideLog = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
                switch (blockstateIn.getValue(BlockStairs.FACING)) {
                    case NORTH: case SOUTH: sideLog = sideLog.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X); break;
                    case EAST: case WEST: sideLog = sideLog.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z); break;
                }
                blockstateIn = sideLog;
            }

            return super.getBiomeSpecificBlockState(blockstateIn);
        }

        @Override
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            boolean result = super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
            if (this.averageGroundLvl < 0) {
                return true;
            }

            IBlockState sideLogZ = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z));

            // Replace planks in stair roof with logs
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 6, 3, 5, 6, 10, sideLogZ, sideLogZ, false);
            this.setBlockState(worldIn, sideLogZ, 6, 6, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, sideLogZ, 7, 5, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, sideLogZ, 6, 6, 4, structureBoundingBoxIn);
            for (int k = 4; k >= 1; --k) {
                this.setBlockState(worldIn, sideLogZ, k, 2 + k, 7 - k, structureBoundingBoxIn);
            }

            // Keep stairs
            if (this.getBlockStateFromPos(worldIn, 2, 0, -1, structureBoundingBoxIn).getBlock() == Blocks.LOG) {
                this.setBlockState(worldIn, super.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH)),
                        2, 0, -1, structureBoundingBoxIn);
            }

            if (this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getBlock() == IceAndFireForksUtil.getFrozenGrassPath()) {
                this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), 1, -1, -1, structureBoundingBoxIn);
            }
            return result;
        }
    }

    public static class SnowHouse4 extends StructureVillagePieces.House4Garden {

        public static SnowHouse4 createPiece(StructureVillagePieces.Start start, List<StructureComponent> structureComponentList, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(
                    structureMinX, structureMinY, structureMinZ,
                    0, 0, 0,
                    5, 6, 5,
                    facing
            );
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(structureComponentList, structureboundingbox) == null
                    ? new SnowHouse4(start, type, rand, structureboundingbox, facing)
                    : null;
        }

        public SnowHouse4() {}

        public SnowHouse4(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing) {
            super(start, type, rand, structurebb, facing);
        }

        @Override
        protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count) {
            if(this instanceof IStructurePiecesVillagePieces_SnowVillageComponentMixin) {
                ((IStructurePiecesVillagePieces_SnowVillageComponentMixin) this).bettersnowvillages$spawnSnowVillagers(worldIn, boundingBox, x, y, z, count);
            }
            else {
                this.spawnVillagers(worldIn, structurebb, x, y, z, count);
                BetterSnowVillages.LOGGER.warn("Failed to spawn Ice and Fire Snow Villager in custom Structure Component");
            }
        }

        @Override
        protected IBlockState getBiomeSpecificBlockState(IBlockState blockstateIn) {
            blockstateIn = WorldGenBetterSnowVillage.getBasicSnowyPalletSwap(blockstateIn);
            return super.getBiomeSpecificBlockState(blockstateIn);
        }

        @Override
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            boolean result = super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
            if (this.averageGroundLvl < 0) {
                return true;
            }

            // Window Color
            this.setBlockState(worldIn, Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.LIGHT_BLUE), 0, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.LIGHT_BLUE), 2, 2, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.LIGHT_BLUE), 4, 2, 2, structureBoundingBoxIn);

            // Add Door below torch
            this.createVillageDoor(worldIn, structureBoundingBoxIn, randomIn, 2, 1, 0, EnumFacing.NORTH);

            if (this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getBlock() == IceAndFireForksUtil.getFrozenGrassPath()) {
                this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), 1, -1, -1, structureBoundingBoxIn);
            }


            return result;
        }
    }
}
