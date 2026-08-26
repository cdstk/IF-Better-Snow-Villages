package bettersnowvillages.mixin.vanilla;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.structure.StructureComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureComponent.class)
public interface StructureComponent_AccessorMixin {

    @Accessor(value = "mirror")
    Mirror betterSnowVillages$accessorMirror();

    @Accessor(value = "rotation")
    Rotation betterSnowVillages$accessorRotation();
}
