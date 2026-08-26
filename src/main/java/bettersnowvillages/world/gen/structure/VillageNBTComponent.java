package bettersnowvillages.world.gen.structure;

import bettersnowvillages.compat.IceAndFireForksUtil;
import bettersnowvillages.mixin.vanilla.StructureComponent_AccessorMixin;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.Random;

public class VillageNBTComponent extends StructureVillagePieces.Village {

    public final ResourceLocation resourceLocation;
    public final VillageNBTPieceWeight nbtPieceWeight;

    public VillageNBTComponent(@Nonnull ResourceLocation resourceLocation, VillageNBTPieceWeight nbtPieceWeight, StructureVillagePieces.Start start, int type, StructureBoundingBox structureBBIn, EnumFacing facing) {
        super(start, type);
        setCoordBaseMode(Rotation.CLOCKWISE_180.rotate(facing));
        this.resourceLocation = resourceLocation;
        this.nbtPieceWeight = nbtPieceWeight;

        this.boundingBox = structureBBIn;

        int xOffset = 0;
        int zOffset = 0;

        // Recenter BB for template rotation
        switch (this.getCoordBaseMode()) {
            case NORTH:
                xOffset = this.nbtPieceWeight.xOffset;
                zOffset = this.nbtPieceWeight.zOffset;
                break;
            case WEST:
                xOffset = this.nbtPieceWeight.zOffset;
                zOffset = this.nbtPieceWeight.xOffset;
                break;
            case SOUTH:
                xOffset = -this.nbtPieceWeight.xOffset;
                zOffset = -this.nbtPieceWeight.zOffset;
                this.boundingBox.offset(0, 0, this.boundingBox.maxZ - structureBBIn.minZ);
                break;
            case EAST:
                xOffset = -this.nbtPieceWeight.zOffset;
                zOffset = -this.nbtPieceWeight.xOffset;
                this.boundingBox.offset(this.boundingBox.maxX - structureBBIn.minX, 0, 0);
                break;
        }

        // Blending config
        this.boundingBox.offset(
                xOffset,
                0,
                zOffset
        );
    }

    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox villageBB) {
        if (averageGroundLvl < 0) {
            averageGroundLvl = getAverageGroundLevel(world, villageBB);
            if (averageGroundLvl < 0) {
                return true;
            }

            this.boundingBox.offset(0, averageGroundLvl - this.boundingBox.minY, 0);
        }

        // Blending config
        this.boundingBox.offset(
                0,
                this.nbtPieceWeight.yOffset,
                0
        );

        BlockPos templatePosition = new BlockPos(this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ);
        if(IceAndFireForksUtil.isBlockInsideMausoleum(world, templatePosition)) {
            return true;
        }

        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();

        PlacementSettings placeSettings = (new PlacementSettings()).setReplacedBlock(Blocks.STRUCTURE_VOID).setBoundingBox(villageBB);
        if(this.getCoordBaseMode() != null && this instanceof StructureComponent_AccessorMixin) {
            StructureComponent_AccessorMixin faceSettings = (StructureComponent_AccessorMixin) this;

            Rotation rotation = faceSettings.betterSnowVillages$accessorRotation();
            placeSettings.setRotation(rotation);
            placeSettings.setMirror(faceSettings.betterSnowVillages$accessorMirror());
        }

        Template template = templateManager.getTemplate(world.getMinecraftServer(), this.resourceLocation);
        template.addBlocksToWorld(world, templatePosition, placeSettings, 18);
        return true;
    }
}
