package am2.common.blocks;

import am2.ArsMagica2;
import am2.common.blocks.tileentity.TileEntityOcculus;
import am2.common.defs.CreativeTabsDefs;
import am2.common.defs.IDDefs;
import am2.common.extensions.EntityExtension;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static am2.common.registry.Registry.GetBlocksToRegister;
import static am2.common.registry.Registry.GetItemsToRegister;

public class BlockOcculus extends BlockContainer {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	
	public BlockOcculus() {
		super(Material.ROCK);
		setCreativeTab(CreativeTabsDefs.tabAM2Blocks);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		setHardness(3.0F);
		setResistance(5.0F);
		setHarvestLevel("pickaxe", -1);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}


	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking())
			return false;
		if (EntityExtension.For(playerIn).getCurrentLevel() == 0 && !Minecraft.getMinecraft().player.isCreative()) {
			if (worldIn.isRemote)
				Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Mythical forces prevent you from using this device!"));
			return true;
		}
		playerIn.openGui(ArsMagica2.instance, IDDefs.GUI_OCCULUS, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	public BlockOcculus registerAndName(ResourceLocation rl) {
        this.setUnlocalizedName(rl.toString());
        this.setRegistryName(rl.toString());
        GetBlocksToRegister().add(this);
        GetItemsToRegister().add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        return this;
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityOcculus();
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityOcculus();
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState,
			IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).ordinal() - 2;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta + 2]);
	}
	
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}
}
