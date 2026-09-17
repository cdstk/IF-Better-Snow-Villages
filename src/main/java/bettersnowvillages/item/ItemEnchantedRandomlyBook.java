package bettersnowvillages.item;

import bettersnowvillages.config.ForgeConfigHandler;
import bettersnowvillages.config.ForgeConfigProvider;
import bettersnowvillages.item.base.ItemUnknownEnchantedBook;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemEnchantedRandomlyBook extends ItemUnknownEnchantedBook {

    public ItemEnchantedRandomlyBook(String modid, String name) {
        super(modid, name);
    }

    public void spawnEnchantedBook(ItemStack stack, World worldIn, EntityLivingBase entityIn, int itemSlot, boolean isSelected) {
        ItemStack bookStack = new ItemStack(Items.ENCHANTED_BOOK);

        if(!worldIn.isRemote) {
            Random rand = entityIn.getRNG();
            List<Enchantment> configEnchantments = ForgeConfigProvider.getDefaultEnchantedRandomlyBookPool();
            Enchantment enchantment = null;

            // Whitelist behavior
            if(ForgeConfigHandler.item.enchantedRandomlyWhitelist && !configEnchantments.isEmpty()) {
                enchantment = configEnchantments.get(rand.nextInt(configEnchantments.size()));
            }
            else {
                List<Enchantment> possible = new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValuesCollection());
                if(!possible.isEmpty()) {
                    // Empty list behavior
                    if (configEnchantments.isEmpty()) {
                        enchantment = possible.get(rand.nextInt(possible.size()));
                    }
                    // Blacklist behavior
                    else {
                        possible.removeAll(configEnchantments);
                        enchantment = possible.get(rand.nextInt(possible.size()));
                    }
                }
            }

            if(enchantment == null)
                return;

            ItemEnchantedBook.addEnchantment(
                    bookStack,
                    new EnchantmentData(
                            enchantment,
                            MathHelper.getInt(rand, enchantment.getMinLevel(), enchantment.getMaxLevel()))
            );
        }

        entityIn.replaceItemInInventory(itemSlot, bookStack);
    }
}
