package am2.common.bosses.ai;

import am2.common.bosses.BossActions;
import am2.common.bosses.EntityNatureGuardian;
import am2.common.entity.EntityThrownSickle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIPlantGuardianThrowSickle extends EntityAIBase{
	private final EntityNatureGuardian host;
	private final float moveSpeed;
	private EntityLivingBase target;
	private int cooldownTicks = 0;

	public EntityAIPlantGuardianThrowSickle(EntityNatureGuardian host, float moveSpeed){
		this.host = host;
		this.moveSpeed = moveSpeed;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute(){
		if (cooldownTicks-- > 0 || host.getCurrentAction() != BossActions.IDLE) return false;
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget == null || AITarget.isDead) return false;
		this.target = AITarget;
		return true;
	}

	@Override
	public void updateTask(){
		host.getLookHelper().setLookPositionWithEntity(target, 30, 30);
		if (host.getDistanceSq(target) > 100){
			host.getNavigator().tryMoveToEntityLiving(target, moveSpeed);
		}else{
			host.getNavigator().clearPath();
			if (host.getCurrentAction() != BossActions.THROWING_SICKLE)
				host.setCurrentAction(BossActions.THROWING_SICKLE);

			if (host.getTicksInCurrentAction() == 12){
				host.faceEntity(target, 180, 180);
				if (!host.world.isRemote){
					EntityThrownSickle projectile = new EntityThrownSickle(host.world, host, 2.0f);
					projectile.setThrowingEntity(host);
					projectile.setProjectileSpeed(2.0);
					host.world.spawnEntity(projectile);
				}
			}
		}
	}
}
