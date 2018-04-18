package am2.common.enchantments;

import am2.ArsMagica2;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class AMEnchantments{
	public static EnchantMagicResist magicResist = new EnchantMagicResist(Rarity.COMMON);
	public static EnchantmentSoulbound soulbound = new EnchantmentSoulbound(Rarity.RARE);

	public static void Init(){
        GameRegistry.findRegistry ( Enchantment.class ).registerAll ( magicResist.setRegistryName ( ArsMagica2.MODID , "ench_magicresist" ) ,
                soulbound.setRegistryName ( ArsMagica2.MODID , "ench_soulbound" ) );
    }

	public static int GetEnchantmentLevelSpecial(Enchantment ench, ItemStack stack){
		int baseEnchLvl = EnchantmentHelper.getEnchantmentLevel(ench, stack);
		/*if (enchID == imbuedArmor.effectId || enchID == imbuedBow.effectId || enchID == imbuedWeapon.effectId){
			if (baseEnchLvl > 3)
				return (baseEnchLvl & 0x6000) >> 13;
		}*/
		return baseEnchLvl;
	}
}
