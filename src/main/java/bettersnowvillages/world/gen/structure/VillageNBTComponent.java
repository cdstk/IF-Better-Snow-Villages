package bettersnowvillages.world.gen.structure;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.compat.IceAndFireForksUtil;
import bettersnowvillages.mixin.vanilla.StructureComponent_AccessorMixin;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import javax.annotation.Nullable;
import java.util.Random;

public class VillageNBTComponent extends StructureVillagePieces.Village {

    private VillageNBTPieceWeight nbtPieceWeight = null;

    public static void alignBoundingBox(VillageNBTPieceWeight nbtPieceWeight, StructureBoundingBox boundingBox, EnumFacing facing) {
        int xOffset = 0;
        int zOffset = 0;

        // Recenter BB for template rotation
        switch (Rotation.CLOCKWISE_180.rotate(facing)) {
            case NORTH:
                xOffset = nbtPieceWeight.xOffset;
                zOffset = nbtPieceWeight.zOffset;
                break;
            case WEST:
                xOffset = nbtPieceWeight.zOffset;
                zOffset = nbtPieceWeight.xOffset;
                break;
            case SOUTH:
                xOffset = -nbtPieceWeight.xOffset;
                zOffset = -nbtPieceWeight.zOffset;
                boundingBox.offset(0, 0, boundingBox.maxZ - boundingBox.minZ);
                break;
            case EAST:
                xOffset = -nbtPieceWeight.zOffset;
                zOffset = -nbtPieceWeight.xOffset;
                boundingBox.offset(boundingBox.maxX - boundingBox.minX, 0, 0);
                break;
        }

        // Blending config
        boundingBox.offset(
                xOffset,
                nbtPieceWeight.yOffset,
                zOffset
        );
    }

    // DO NOT USE, prevents java.lang.InstantiationException
    public VillageNBTComponent() {
        super();
    }

    // DO NOT USE, prevents java.lang.InstantiationException
    protected VillageNBTComponent(StructureVillagePieces.Start start, int type) {
        super(start, type);
    }

    public VillageNBTComponent(VillageNBTPieceWeight nbtPieceWeight, StructureVillagePieces.Start start, int type, StructureBoundingBox structureBBIn, EnumFacing facing) {
        super(start, type);
        setCoordBaseMode(Rotation.CLOCKWISE_180.rotate(facing));
        this.nbtPieceWeight = nbtPieceWeight;

        this.boundingBox = structureBBIn;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        if(this.nbtPieceWeight != null) {
            this.nbtPieceWeight.writeToNBT(tagCompound);
        }
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
        super.readStructureFromNBT(tagCompound, templateManager);
        this.nbtPieceWeight = VillageNBTPieceWeight.recreateFromNBT(tagCompound);
    }

    @Override
    protected int getAverageGroundLevel(World worldIn, StructureBoundingBox structurebb) {
        int yOffset = this.nbtPieceWeight != null ? this.nbtPieceWeight.yOffset : 0;
        return super.getAverageGroundLevel(worldIn, structurebb) + yOffset;
    }

    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox villageBB) {
        if(this.nbtPieceWeight == null) {
            BetterSnowVillages.LOGGER.warn("Tried to generate a NBT Village Component without a template at: /tp @s {} {} {}", this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.minZ);
            return true;
        }

        if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = getAverageGroundLevel(world, villageBB);
            if (this.averageGroundLvl < 0) {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY, 0);
        }

        if(IceAndFireForksUtil.isStructureInsideMausoleum(world, this.boundingBox, villageBB)) {
            return true;
        }

        BlockPos templatePosition = new BlockPos(this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ);
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();

        PlacementSettings placeSettings = (new PlacementSettings()).setReplacedBlock(Blocks.STRUCTURE_VOID).setBoundingBox(villageBB);
        if(this.getCoordBaseMode() != null && this instanceof StructureComponent_AccessorMixin) {
            StructureComponent_AccessorMixin faceSettings = (StructureComponent_AccessorMixin) this;

            Rotation rotation = faceSettings.betterSnowVillages$accessorRotation();
            placeSettings.setRotation(rotation);
            placeSettings.setMirror(faceSettings.betterSnowVillages$accessorMirror());
        }

        Template template = templateManager.getTemplate(world.getMinecraftServer(), this.nbtPieceWeight.resourceLocation);
        template.addBlocksToWorld(world, templatePosition, placeSettings, 2);

        for (int zz = 0; zz < this.boundingBox.getZSize(); ++zz) {
            for (int xx = 0; xx < this.boundingBox.getXSize(); ++xx) {
                BlockPos relativePos = Template.transformedBlockPos(placeSettings, new BlockPos(xx, 0, zz));

                this.clearCurrentPositionBlocksUpwards(world, relativePos.getX(), this.boundingBox.getYSize() + relativePos.getY() + 1, relativePos.getZ(), villageBB);

                IBlockState targetBlock = this.getBlockStateFromPos(world, relativePos.getX(), relativePos.getY(), relativePos.getZ(), villageBB);
                IBlockState supportBlock = this.getAquaticSupportBlock(world, relativePos.getX(), relativePos.getY(), relativePos.getZ(), villageBB);
                // Over Water
                if(supportBlock != null) {
                    int count;
                    if(supportBlock.getMaterial().equals(Material.ICE) || supportBlock.getMaterial().equals(Material.PACKED_ICE)) {
                        count = this.boundingBox.getYSize() / 2;
                        this.replaceAirAndLiquidDownwards(world, supportBlock, relativePos.getX(), relativePos.getY() - 1, relativePos.getZ(), villageBB, count);
                    }
                    else if(supportBlock.getMaterial().equals(Material.SNOW) || supportBlock.getMaterial().equals(Material.CRAFTED_SNOW)) {
                        count = this.boundingBox.getYSize() / 4;
                        this.replaceAirAndLiquidDownwards(world, supportBlock, relativePos.getX(), relativePos.getY() - 1, relativePos.getZ(), villageBB, count);
                    }
                    else {
                        count = this.boundingBox.getYSize() / 3;
                        this.replaceAirAndLiquidDownwards(world, supportBlock, relativePos.getX(), relativePos.getY() - 1, relativePos.getZ(), villageBB, count);
                    }
                }
                // Over Ground
                else {
                    supportBlock = this.getGroundSupportBlock(world, relativePos.getX(), relativePos.getY(), relativePos.getZ(), villageBB);
                    if(supportBlock != null) {
                        this.replaceAirAndLiquidDownwards(world, supportBlock, relativePos.getX(), relativePos.getY() - 1, relativePos.getZ(), villageBB);
                    }
                }

                // Some structures have water at y0
                if(targetBlock.getMaterial().isLiquid() && !this.getBlockStateFromPos(world, relativePos.getX(), relativePos.getY() - 1, relativePos.getZ(), villageBB).getMaterial().isLiquid()) {
                    this.setBlockState(world, Blocks.ICE.getDefaultState(), relativePos.getX(),  relativePos.getY() - 1, relativePos.getZ(), villageBB);
                }
            }
        }

        return true;
    }

    @Nullable
    public IBlockState getAquaticSupportBlock(World world, int x, int y, int z, StructureBoundingBox villageBB) {
        IBlockState belowBlock = this.getBlockStateFromPos(world, x, y - 1, z, villageBB);
        if(belowBlock.getMaterial().isLiquid()) {
            IBlockState targetBlock = this.getBlockStateFromPos(world, x, y, z, villageBB);
            Material material = targetBlock.getMaterial();

            if (material.equals(Material.WOOD)) {
                return Blocks.PLANKS.getDefaultState();
            }

            if (material.equals(Material.SNOW) || material.equals(Material.CRAFTED_SNOW)) {
                return Blocks.SNOW.getDefaultState();
            }

            if (!material.isLiquid() && !material.equals(Material.AIR)) {
                return Blocks.PACKED_ICE.getDefaultState();
            }
        }
        return null;
    }

    @Nullable
    public IBlockState getGroundSupportBlock(World world, int x, int y, int z, StructureBoundingBox villageBB) {
        IBlockState targetBlock = this.getBlockStateFromPos(world, x, y, z, villageBB);
        Material material = targetBlock.getMaterial();

        if (material.equals(Material.ROCK)) {
            return IceAndFireForksUtil.getFrozenCobblestone().getDefaultState();
        }

        if (material.equals(Material.PACKED_ICE)) {
            return Blocks.PACKED_ICE.getDefaultState();
        }

        return material.isSolid() ? Blocks.DIRT.getDefaultState() : null;
    }

    public void replaceAirAndLiquidDownwards(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn, int replaceCount) {
        int xx = this.getXWithOffset(x, z);
        int yy = this.getYWithOffset(y);
        int zz = this.getZWithOffset(x, z);

        if (boundingboxIn.isVecInside(new BlockPos(xx, yy, zz))) {
            IBlockState iblockstate = this.getBiomeSpecificBlockState(blockstateIn);
            while (replaceCount > 0 && (worldIn.isAirBlock(new BlockPos(xx, yy, zz))
                    || worldIn.getBlockState(new BlockPos(xx, yy, zz)).getMaterial().isLiquid())
                    && yy > 1)
            {
                worldIn.setBlockState(new BlockPos(xx, yy, zz), iblockstate, 2);
                --yy;
                replaceCount--;
            }
        }
    }
}
