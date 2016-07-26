package am2.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import am2.ArsMagica2;
import am2.entity.ai.EntityAISummonFollowOwner;
import am2.entity.ai.selectors.SummonEntitySelector;
import am2.extensions.EntityExtension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityUtils {
	
	private static final HashMap<Integer, ArrayList<EntityAITasks.EntityAITaskEntry>> storedTasks = new HashMap<>();
	private static final HashMap<Integer, ArrayList<EntityAITasks.EntityAITaskEntry>> storedAITasks = new HashMap<>();
	private static final String isSummonKey = "AM2_Entity_Is_Made_Summon";
//	private static final String summonEntityIDs = "AM2_Summon_Entity_IDs";
	private static final String summonDurationKey = "AM2_Summon_Duration";
	private static final String summonOwnerKey = "AM2_Summon_Owner";
//	private static final String summonTileXKey = "AM2_Summon_Tile_X";
//	private static final String summonTileYKey = "AM2_Summon_Tile_Y";
//	private static final String summonTileZKey = "AM2_Summon_Tile_Z";
	
	
	public static int getLevelFromXP(float totalXP){
		int level = 0;
		int xp = (int)Math.floor(totalXP);
		do{
			int cap = xpBarCap(level);
			xp -= cap;
			if (xp < 0)
				break;
			level++;
		}while (true);

		return level;
	}
	
	public static Entity getPointedEntity(World world, EntityLivingBase entityplayer, double range, double collideRadius, boolean nonCollide, boolean targetWater){
		Entity pointedEntity = null;
		double d = range;
		Vec3d vec3d = new Vec3d(entityplayer.posX, entityplayer.posY + entityplayer.getEyeHeight(), entityplayer.posZ);
		Vec3d vec3d1 = entityplayer.getLookVec();
		Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d);
		double f1 = collideRadius;
		List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.getEntityBoundingBox().addCoord(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d).expand(f1, f1, f1));

		double d2 = 0.0D;
		for (int i = 0; i < list.size(); i++){
			Entity entity = (Entity)list.get(i);
			RayTraceResult mop = world.rayTraceBlocks(
					new Vec3d(entityplayer.posX, entityplayer.posY + entityplayer.getEyeHeight(), entityplayer.posZ),
					new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ),
					targetWater, !targetWater, false);
			if (((entity.canBeCollidedWith()) || (nonCollide)) && mop == null){
				float f2 = Math.max(0.8F, entity.getCollisionBorderSize());
				AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(f2, f2, f2);
				RayTraceResult movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d2);
				if (axisalignedbb.isVecInside(vec3d)){
					if ((0.0D < d2) || (d2 == 0.0D)){
						pointedEntity = entity;
						d2 = 0.0D;
					}

				}else if (movingobjectposition != null){
					double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
					if ((d3 < d2) || (d2 == 0.0D)){
						pointedEntity = entity;
						d2 = d3;
					}
				}
			}
		}
		return pointedEntity;
	}
	
	public static int getXPFromLevel(int level){
		int totalXP = 0;
		for(int i = 0; i < level; i++){
			totalXP += xpBarCap(i);
		}
		return totalXP;
	}
	
	public static int xpBarCap(int experienceLevel){
		return experienceLevel >= 30 ? 62 + (experienceLevel - 30) * 7 : (experienceLevel >= 15 ? 17 + (experienceLevel - 15) * 3 : 17);
	}
	
	public static boolean isAIEnabled(EntityCreature ent){
		return !ent.isAIDisabled();
	}
	
	public static void makeSummon_PlayerFaction(EntityCreature entityliving, EntityPlayer player, boolean storeForRevert){
		if (isAIEnabled(entityliving) && EntityExtension.For(player).getCurrentSummons() < EntityExtension.For(player).getMaxSummons()){
			if (storeForRevert)
				storedTasks.put(entityliving.getEntityId(), new ArrayList<EntityAITasks.EntityAITaskEntry>(entityliving.targetTasks.taskEntries));

			boolean addMeleeAttack = false;
			ArrayList<EntityAITaskEntry> toRemove = new ArrayList<EntityAITaskEntry>();
			for (Object task : entityliving.tasks.taskEntries){
				EntityAITaskEntry base = (EntityAITaskEntry)task;
				if (base.action instanceof EntityAIAttackMelee){
					toRemove.add(base);
					addMeleeAttack = true;
				}
			}

			entityliving.tasks.taskEntries.removeAll(toRemove);

			if (storeForRevert)
				storedAITasks.put(entityliving.getEntityId(), toRemove);

			if (addMeleeAttack){
				float speed = entityliving.getAIMoveSpeed();
				if (speed <= 0) speed = 1.0f;
				entityliving.tasks.addTask(3, new EntityAIAttackMelee(entityliving, speed, true));
			}
			
			entityliving.targetTasks.taskEntries.clear();

			entityliving.targetTasks.addTask(1, new EntityAIHurtByTarget(entityliving, true));
			entityliving.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityMob>(entityliving, EntityMob.class, 0, true, false, SummonEntitySelector.instance));
			entityliving.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntitySlime>(entityliving, EntitySlime.class, true));
			entityliving.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityGhast>(entityliving, EntityGhast.class, true));
			entityliving.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityShulker>(entityliving, EntityShulker.class, true));

			if (!entityliving.worldObj.isRemote && entityliving.getAttackTarget() != null && entityliving.getAttackTarget() instanceof EntityPlayer)
				ArsMagica2.proxy.addDeferredTargetSet(entityliving, null);

			if (entityliving instanceof EntityTameable){
				((EntityTameable)entityliving).setTamed(true);
				((EntityTameable)entityliving).setOwnerId(player.getPersistentID());
			}

			entityliving.getEntityData().setBoolean(isSummonKey, true);
			EntityExtension.For(player).addSummon(entityliving);
		}
	}
	
	public static boolean isSummon(EntityLivingBase entityliving){
		return entityliving.getEntityData().getBoolean(isSummonKey);
	}
	
	public static void makeSummon_MonsterFaction(EntityCreature entityliving, boolean storeForRevert){
		if (isAIEnabled(entityliving)){
			if (storeForRevert)
				storedTasks.put(entityliving.getEntityId(), new ArrayList<EntityAITasks.EntityAITaskEntry>(entityliving.targetTasks.taskEntries));
			entityliving.targetTasks.taskEntries.clear();
			entityliving.targetTasks.addTask(1, new EntityAIHurtByTarget(entityliving, true));
			entityliving.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(entityliving, EntityPlayer.class, true));
			if (!entityliving.worldObj.isRemote && entityliving.getAttackTarget() != null && entityliving.getAttackTarget() instanceof EntityMob)
				ArsMagica2.proxy.addDeferredTargetSet(entityliving, null);

			entityliving.getEntityData().setBoolean(isSummonKey, true);
		}
	}
	
	public static void setOwner(EntityLivingBase entityliving, EntityLivingBase owner){
		if (owner == null){
			entityliving.getEntityData().removeTag(summonOwnerKey);
			return;
		}

		entityliving.getEntityData().setInteger(summonOwnerKey, owner.getEntityId());
		if (entityliving instanceof EntityCreature){
			float speed = entityliving.getAIMoveSpeed();
			if (speed <= 0) speed = 1.0f;
			((EntityCreature)entityliving).tasks.addTask(1, new EntityAISummonFollowOwner((EntityCreature)entityliving, speed, 10, 20));
		}
	}
	
	public static void setSummonDuration(EntityLivingBase entity, int duration){
		entity.getEntityData().setInteger(summonDurationKey, duration);
	}
	
	public static int getOwner(EntityLivingBase entityliving){
		if (!isSummon(entityliving)) return -1;
		Integer ownerID = entityliving.getEntityData().getInteger(summonOwnerKey);
		return ownerID == null ? -1 : ownerID;
	}

	public static boolean revertAI(EntityCreature entityliving){

		int ownerID = getOwner(entityliving);
		Entity owner = entityliving.worldObj.getEntityByID(ownerID);
		if (owner != null && owner instanceof EntityLivingBase){
			EntityExtension.For((EntityLivingBase)owner).removeSummon();
			if (EntityExtension.For((EntityLivingBase)owner).isManaLinkedTo(entityliving)){
				EntityExtension.For((EntityLivingBase)owner).updateManaLink(entityliving);
			}
		}

		entityliving.getEntityData().setBoolean(isSummonKey, false);
		setOwner(entityliving, null);

		if (storedTasks.containsKey(entityliving.getEntityId())){
			entityliving.targetTasks.taskEntries.clear();
			entityliving.targetTasks.taskEntries.addAll(storedTasks.get(entityliving.getEntityId()));
			storedTasks.remove(entityliving.getEntityId());

			if (storedAITasks.get(entityliving.getEntityId()) != null){
				ArrayList<EntityAITaskEntry> toRemove = new ArrayList<EntityAITaskEntry>();
				for (Object task : entityliving.tasks.taskEntries){
					EntityAITaskEntry base = (EntityAITaskEntry)task;
					if (base.action instanceof EntityAIAttackMelee || base.action instanceof EntityAISummonFollowOwner){
						toRemove.add(base);
					}
				}

				entityliving.tasks.taskEntries.removeAll(toRemove);

				entityliving.tasks.taskEntries.addAll(storedAITasks.get(entityliving.getEntityId()));
				storedAITasks.remove(entityliving.getEntityId());
			}
			if (!entityliving.worldObj.isRemote && entityliving.getAttackTarget() != null)
				ArsMagica2.proxy.addDeferredTargetSet(entityliving, null);
			if (entityliving instanceof EntityTameable){
				((EntityTameable)entityliving).setTamed(false);
			}
			return true;
		}

		return false;
	}

	public static boolean canBlockDamageSource(EntityLivingBase living, DamageSource damageSourceIn) {
		if (!damageSourceIn.isUnblockable()) {
			Vec3d vec3d = damageSourceIn.getDamageLocation();

			if (vec3d != null) {
				Vec3d vec3d1 = living.getLook(1.0F);
				Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(living.posX, living.posY, living.posZ)).normalize();
				vec3d2 = new Vec3d(vec3d2.xCoord, 0.0D, vec3d2.zCoord);

				if (vec3d2.dotProduct(vec3d1) < 0.0D) {
					return true;
				}
			}
		}
		return false;
	}
}