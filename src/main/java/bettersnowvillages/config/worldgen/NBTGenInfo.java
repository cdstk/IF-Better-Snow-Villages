package bettersnowvillages.config.worldgen;

public class NBTGenInfo {

    public final int weight;
    public final int min;
    public final int max;
    public final int bbXMax;
    public final int bbYMax;
    public final int bbZMax;
    public final int xOffset;
    public final int yOffset;
    public final int zOffset;

    public NBTGenInfo(int weight, int min, int max, int bbXMax, int bbYMax, int bbZMax) {
        this(weight, min, max, bbXMax, bbYMax, bbZMax, 0, 0, 0);
    }

    public NBTGenInfo(int weight, int min, int max, int bbXMax, int bbYMax, int bbZMax, int xOffset, int yOffset, int zOffset) {
        this.weight = weight;
        this.min = min;
        this.max = max;

        this.bbXMax = bbXMax;
        this.bbYMax = bbYMax;
        this.bbZMax = bbZMax;

        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }
}
