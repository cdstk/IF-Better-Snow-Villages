package bettersnowvillages.item;

import bettersnowvillages.config.ForgeConfigHandler;
import bettersnowvillages.item.base.ItemUnknownEnchantedBook;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemEnchantedWithLevelsBook extends ItemUnknownEnchantedBook {

    public final boolean isTreasure;

    public ItemEnchantedWithLevelsBook(String modid, String name, boolean treasure) {
        super(modid, name);
        this.isTreasure = treasure;
    }

    public void spawnEnchantedBook(ItemStack stack, World worldIn, EntityLivingBase entityIn, int itemSlot, boolean isSelected) {
        ItemStack bookStack = new ItemStack(Items.BOOK);
        int withLevels = this.getDamage(stack);
        IAttributeInstance luck = entityIn.getEntityAttribute(SharedMonsterAttributes.LUCK);
        if(luck != null) withLevels += (int) (luck.getAttributeValue() * ForgeConfigHandler.item.enchantedWithLevelsLuck);

        bookStack = EnchantmentHelper.addRandomEnchantment(
                entityIn.getRNG(),
                bookStack,
                withLevels,
                this.isTreasure
        );
        entityIn.replaceItemInInventory(itemSlot, bookStack);
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return this.isTreasure ? EnumRarity.RARE : super.getForgeRarity(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TextComponentTranslation("item.bettersnowvillages.with_levels.tooltip", this.getDamage(stack)).getFormattedText());
    }
}
