package am2.common.utils;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Quaternion;

import javax.vecmath.Vector3f;
import java.lang.reflect.Type;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class ModelUtils {
	
	public static final Type mapType = new TypeToken<Map<String, String>>() {}.getType();
	public static final Gson GSON = new GsonBuilder().registerTypeAdapter(mapType, ModelTextureDeserializer.INSTANCE).create();
	public static final IModelState NULL_STATE;
	public static final IModelState DEFAULT_ITEM_STATE;
	public static final IModelState DEFAULT_BLOCK_STATE;
	public static final IModelState DEFAULT_SHIELD_STATE;
	public static final IModelState BLOCKING_SHIELD_STATE;

	static {
		ImmutableMap.Builder<IModelPart, TRSRTransformation> builder = ImmutableMap.builder();
		builder.put(ItemCameraTransforms.TransformType.GROUND, get(0, 2, 0, 0, 0, 0, 0.5f));
		builder.put(ItemCameraTransforms.TransformType.HEAD, get(0, 13, 7, 0, 180, 0, 1));
		builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, get(0, 3, 1, 0, 0, 0, 0.55f));
		builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, get(0, 3, 1, 0, 0, 0, 0.55f));
		builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, get(1.13f, 3.2f, 1.13f, 0, -90, 25, 0.68f));
		builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, get(1.13f, 3.2f, 1.13f, 0, 90, -25, 0.68f));
		DEFAULT_ITEM_STATE = new SimpleModelState(builder.build());

		ImmutableMap.Builder<IModelPart, TRSRTransformation> builderBlock = ImmutableMap.builder();
		builderBlock.put(ItemCameraTransforms.TransformType.GUI, get(0, 0, 0, 30, 225, 0, 0.625f));
		builderBlock.put(ItemCameraTransforms.TransformType.GROUND, get(0, 3, 0, 0, 0, 0, 0.25f));
		builderBlock.put(ItemCameraTransforms.TransformType.FIXED, get(0, 0, 0, 0, 0, 0, 0.5f));
		builderBlock.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, get(0, 2.5f, 0, 75, 45, 0, 0.375f));
		builderBlock.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, get(0, 2.5f, 0, 75, 225, 0, 0.375f));
		builderBlock.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, get(0, 0, 0, 0, 45, 0, 0.40f));
		builderBlock.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, get(0, 0, 0, 0, 225, 0, 0.40f));
		DEFAULT_BLOCK_STATE = new SimpleModelState(builderBlock.build());

		ImmutableMap.Builder<IModelPart, TRSRTransformation> builderShield = ImmutableMap.builder();
		builderShield.put(ItemCameraTransforms.TransformType.GUI, get(-2, -2, 0, 15, -25, -5, 0.65f));
		builderShield.put(ItemCameraTransforms.TransformType.GROUND, get(0, 0, 0, 0, 0, 0, 0.25f));
		builderShield.put(ItemCameraTransforms.TransformType.FIXED, get(-16, -1, 1, 0, 0, 90, 0.5f));
		builderShield.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, get(10.51f, 6, -20, 0, 90, 0, 1));
		builderShield.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, get(-26.51f, 6, 12, 0, -90, 0, 1));
		builderShield.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, get(-27.1f, 4.82f, -28, 0, 180, 5, 1.25f));
		builderShield.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, get(-28.88f, 1.0f, -28, 0, 180, -5, 1.25f));
		DEFAULT_SHIELD_STATE = new SimpleModelState(builderShield.build());
		
		ImmutableMap.Builder<IModelPart, TRSRTransformation> builderBlockingShield = ImmutableMap.builder();
		builderBlockingShield.put(ItemCameraTransforms.TransformType.GUI, get(-2, -2, 0, 15, -25, -5, 0.65f));
		builderBlockingShield.put(ItemCameraTransforms.TransformType.GROUND, get(0, 0, 0, 0, 0, 0, 0.25f));
		builderBlockingShield.put(ItemCameraTransforms.TransformType.FIXED, get(-16, -1, 1, 0, 0, 90, 0.5f));
		builderBlockingShield.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, get(-7, 18, -12, 45, 135, 0, 1));
		builderBlockingShield.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, get(-32, 3, 4, 45, -135, 0, 1));
		builderBlockingShield.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, get(-34f, 5, -32, 0, 180, -5, 1.25f));
		builderBlockingShield.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, get(-26.88f, 7f, -30, 0, 180, 5, 1.25f));
		BLOCKING_SHIELD_STATE = new SimpleModelState(builderBlockingShield.build());
		
		ImmutableMap.Builder<IModelPart, TRSRTransformation> builderNull = ImmutableMap.builder();
		builderNull.put(ItemCameraTransforms.TransformType.GUI, get(0, 0, 0, 0, 0, 0, 1));
		builderNull.put(ItemCameraTransforms.TransformType.GROUND, get(0, 0, 0, 0, 0, 0, 1));
		builderNull.put(ItemCameraTransforms.TransformType.FIXED, get(0, 0, 0, 0, 0, 0, 1));
		builderNull.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, get(0, 0, 0, 0, 0, 0, 1));
		builderNull.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, get(0, 0, 0, 0, 0, 0, 1));
		builderNull.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, get(0, 0, 0, 0, 0, 0, 1));
		builderNull.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, get(0, 0, 0, 0, 0, 0, 1));
		NULL_STATE = new SimpleModelState(builderBlockingShield.build());
	}
	
	private static TRSRTransformation get(float tx, float ty, float tz,
			float ax, float ay, float az, float s) {
		return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
				new Vector3f(tx / 16, ty / 16, tz / 16), TRSRTransformation
						.quatFromXYZDegrees(new Vector3f(ax, ay, az)),
				new Vector3f(s, s, s), null));
	}
	
	public static void renderShield(ItemStack stack, boolean isBlocking, ItemCameraTransforms.TransformType type, EntityLivingBase entity) {
		GlStateManager.pushMatrix();
		
		ModelUtils.transform(isBlocking ? BLOCKING_SHIELD_STATE : DEFAULT_SHIELD_STATE , entity.getHorizontalFacing(), false);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		GlStateManager.scale(1.0F, -1.0F, -1.0F);
		new ModelShield().render();
		GlStateManager.popMatrix();
	}
	
	public static void transform(IModelState state, EnumFacing type, boolean leftHand) {
		TRSRTransformation transform = TRSRTransformation.from(type);
		if (transform != null) {
			GlStateManager.translate(transform.getTranslation().x, transform.getTranslation().y, transform.getTranslation().z);
			GlStateManager.scale(transform.getScale().x, transform.getScale().y, transform.getScale().z);
			GlStateManager.rotate(new Quaternion(transform.getLeftRot().x, transform.getLeftRot().y, transform.getLeftRot().z, transform.getLeftRot().w));
		}
//		if (transform != null) {
//			Matrix4f flipX = new Matrix4f();
//			flipX.setIdentity();
//			flipX.m00 = -1;
//			Matrix4f matrix = TRSRTransformation.blockCornerToCenter(transform).getMatrix();
//			if (!leftHand) {
//				matrix.mul(flipX, matrix);
//				matrix.mul(matrix, flipX);
//			}
//			ForgeHooksClient.multiplyCurrentGlMatrix(matrix);
//		}
	}
	
	public static class ModelTextureDeserializer implements JsonDeserializer<Map<String, String>> {

		public static final ModelTextureDeserializer INSTANCE = new ModelTextureDeserializer();

		private static final Gson GSON = new Gson();

		@Override
		public Map<String, String> deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {

			JsonObject obj = json.getAsJsonObject();
			JsonElement texElem = obj.get("textures");

			if (texElem == null) {
				throw new JsonParseException("Missing textures entry in json");
			}

			return GSON.fromJson(texElem, mapType);
		}
	}


}
