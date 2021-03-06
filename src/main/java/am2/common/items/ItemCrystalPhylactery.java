package am2.common.items;

import am2.common.LogHelper;
import am2.common.utils.EntityUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class ItemCrystalPhylactery extends ItemArsMagica{

	public final HashMap<String, Integer> spawnableEntities;

	public static final int META_EMPTY = 0;
	public static final int META_QUARTER = 1;
	public static final int META_HALF = 2;
	public static final int META_FULL = 3;


	public ItemCrystalPhylactery(){
		super();
		this.spawnableEntities = new HashMap<>();
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation ( ItemStack par1ItemStack , @Nullable World world , List <String> par3List , ITooltipFlag par4 ) {
        if (par1ItemStack.hasTagCompound()){
			String className = par1ItemStack.getTagCompound().getString("SpawnClassName");
			par3List.add(I18n.format("am2.tooltip.phyEss", I18n.format("entity." + className + ".name")));
			float pct = par1ItemStack.getTagCompound().getFloat("PercentFilled");
			par3List.add(I18n.format("am2.tooltip.pctFull", pct));
		}else{
			par3List.add(I18n.format("am2.tooltip.empty"));
		}
	}
	
	public void addFill(ItemStack stack){
		if (stack.hasTagCompound()){
			float pct = stack.getTagCompound().getFloat("PercentFilled");
			pct += itemRand.nextFloat() * 5;
			if (pct > 100) pct = 100;
			stack.getTagCompound().setFloat("PercentFilled", pct);
			if (pct == 100)
				stack.setItemDamage(META_FULL);
			else if (pct > 50)
				stack.setItemDamage(META_HALF);
			else if (pct > 25)
				stack.setItemDamage(META_QUARTER);
			else
				stack.setItemDamage(META_EMPTY);

		}
	}

	public void addFill(ItemStack stack, float amt){
		if (stack.hasTagCompound()){
			float pct = stack.getTagCompound().getFloat("PercentFilled");
			pct += amt;
			if (pct > 100) pct = 100;
			stack.getTagCompound().setFloat("PercentFilled", pct);
			if (pct == 100)
				stack.setItemDamage(META_FULL);
			else if (pct > 50)
				stack.setItemDamage(META_HALF);
			else if (pct > 25)
				stack.setItemDamage(META_QUARTER);
			else
				stack.setItemDamage(META_EMPTY);

		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack){
		return par1ItemStack.getItemDamage() == META_FULL;
	}

	public void setSpawnClass(ItemStack stack, Class<? extends Entity> clazz){

		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

        String s = EntityList.getTranslationName ( EntityList.getKey ( clazz ) );
        if (s != null)
			stack.getTagCompound().setString("SpawnClassName", s);
	}

	public boolean canStore(ItemStack stack, EntityLiving entity){
		if (!entity.isNonBoss()) return false;
		if (stack.getItemDamage() == META_FULL)
			return false;
		if (!stack.hasTagCompound())
			return true;

		String e = stack.getTagCompound().getString("SpawnClassName");
        String s = EntityList.getEntityString ( entity );

		return e.equals(s);
	}

	public boolean isFull(ItemStack stack){
		return stack.getItemDamage() == META_FULL;
	}

	public String getSpawnClass(ItemStack stack){
		if (!stack.hasTagCompound())
			return null;
		return stack.getTagCompound().getString("SpawnClassName");
	}
	
	public void getSpawnableEntities(World world){
        for ( ResourceLocation claz : EntityList.getEntityNameList ( ) ) {
            Class <? extends Entity> clazz = EntityList.getClass ( claz );
            LogHelper.info ( "Getting Entity of: " + claz + " / Found: " + clazz );
            if ( clazz != null && clazz.isInstance ( EntityLiving.class ) ) //Cannot check for EntityLiving
                if (EntityCreature.class.isAssignableFrom(clazz)){
                    try{
                        EntityCreature temp = (EntityCreature)clazz.getConstructor(World.class).newInstance(world);
                        if (EntityUtils.isAIEnabled(temp) && temp.isNonBoss()){
                            int color = 0;
                            boolean found = false;
                            //look for entity egg
                            for (Object info : EntityList.ENTITY_EGGS.values()){
                                EntityEggInfo eei = (EntityEggInfo)info;
                                Class<? extends Entity> spawnClass = ForgeRegistries.ENTITIES.getValue(eei.spawnedID).getEntityClass();
                                if (spawnClass == clazz){
                                    color = eei.primaryColor;
                                    found = true;
                                    break;
                                }
                            }
                            if (!found){
                                //no spawn egg...pick random color?
                                color = world.rand.nextInt();
                            }
                            this.spawnableEntities.put ( EntityList.getTranslationName ( EntityList.getKey ( clazz ) ) , color );
                        }
                    }catch (Throwable e){
                        //e.printStackTrace();
                    }
                }
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems ( CreativeTabs par2CreativeTabs , NonNullList <ItemStack> par3List ) {
        par3List.add(new ItemStack(this));
		for (String s : this.spawnableEntities.keySet()){
			if (s == null)
				continue;
			ItemStack stack = new ItemStack(this, 1, META_FULL);
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("SpawnClassName", s);
			stack.getTagCompound().setFloat("PercentFilled", 100);
			par3List.add(stack);
		}
	}
}
