package am2.common.items;

import am2.api.extensions.ISpellCaster;
import am2.common.entity.EntityBoundArrow;
import am2.common.registry.Registry;
import am2.common.spell.SpellCaster;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemBoundArrow extends ItemArrow {
	
	public ItemBoundArrow() {
		setCreativeTab(null);
	}

	@Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
		EntityBoundArrow arrow = new EntityBoundArrow(worldIn, shooter);

		ISpellCaster caster = stack.getCapability(SpellCaster.INSTANCE, null);
		if (caster != null)
			arrow.setSpellStack(caster.createSpellData(stack.copy()));
		return arrow;
	}
	
	public ItemBoundArrow registerAndName(String name) {
		this.setUnlocalizedName(new ResourceLocation("arsmagica2", name).toString());
		this.setRegistryName(new ResourceLocation("arsmagica2", name));
		Registry.GetItemsToRegister().add(this);
		return this;
	}
}
