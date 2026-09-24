package bettersnowvillages.world.gen.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class VillageNBTPieceWeight extends StructureVillagePieces.PieceWeight {

    public static final String RESOURCE_LOCATION_NBT = "ResourceLocation";
    public static final String Y_OFFSET_NBT = "YOffset";

    public final ResourceLocation resourceLocation;
    public final int bbXMax;
    public final int bbYMax;
    public final int bbZMax;
    public final int xOffset;
    public final int yOffset;
    public final int zOffset;

    public VillageNBTPieceWeight(ResourceLocation resourceLocation, int villagePieceWeight, int villagePiecesLimit, int bbXMax, int bbYMax, int bbZMax) {
        this(resourceLocation, villagePieceWeight, villagePiecesLimit, bbXMax, bbYMax, bbZMax, 0, 0, 0);
    }

    public VillageNBTPieceWeight(ResourceLocation resourceLocation, int villagePieceWeight, int villagePiecesLimit, int bbXMax, int bbYMax, int bbZMax, int xOffset, int yOffset, int zOffset) {
        super(VillageNBTComponent.class, villagePieceWeight, villagePiecesLimit);
        this.resourceLocation = resourceLocation;

        this.bbXMax = bbXMax;
        this.bbYMax = bbYMax;
        this.bbZMax = bbZMax;

        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        if(tagCompound != null) {
            tagCompound.setString(RESOURCE_LOCATION_NBT, this.resourceLocation.toString());

            tagCompound.setInteger(Y_OFFSET_NBT, this.yOffset);
        }
        return tagCompound;
    }

    public static VillageNBTPieceWeight recreateFromNBT(NBTTagCompound tagCompound) {
        if(tagCompound.hasKey(RESOURCE_LOCATION_NBT) && tagCompound.hasKey(Y_OFFSET_NBT)) {
            return new VillageNBTPieceWeight(
                    new ResourceLocation(tagCompound.getString(RESOURCE_LOCATION_NBT)), 0 , 0,
                    0, 0, 0,
                    0, tagCompound.getInteger(Y_OFFSET_NBT), 0
            );
        }
        return null;
    }
}
