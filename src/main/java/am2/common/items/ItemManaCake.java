package am2.common.items;

import am2.common.buffs.BuffEffectManaRegen;
import am2.common.registry.Registry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemManaCake extends ItemFood{

	public ItemManaCake(){
		super(3, 0.6f, false);
	}

	public ItemManaCake registerAndName(String name) {
		this.setUnlocalizedName(new ResourceLocation("arsmagica2", name).toString());
		this.setRegistryName(new ResourceLocation("arsmagica2", name));
		Registry.GetItemsToRegister().add(this);
		return this;
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		player.addPotionEffect(new BuffEffectManaRegen(600, 0));
		super.onFoodEaten(stack, worldIn, player);
	}

}
