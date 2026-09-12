package bettersnowvillages.item.base;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemUnknownEnchantedBook extends ItemBase {

    public ItemUnknownEnchantedBook(String modid, String name) {
        super(modid, name);
        this.setMaxStackSize(1);
        this.setMaxDamage(32768);
    }

    public abstract void spawnEnchantedBook(ItemStack stack, World worldIn, EntityLivingBase entityIn, int itemSlot, boolean isSelected);

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof EntityLivingBase && stack.getCount() > 0) {
            stack.setCount(0);
            this.spawnEnchantedBook(stack, worldIn, (EntityLivingBase) entityIn, itemSlot, isSelected);
        }
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
