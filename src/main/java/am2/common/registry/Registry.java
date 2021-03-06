package am2.common.registry;

import am2.ArsMagica2;
import am2.common.LogHelper;
import am2.common.world.BiomeWitchwoodForest;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

@GameRegistry.ObjectHolder(ArsMagica2.MODID)
@Mod.EventBusSubscriber(modid = ArsMagica2.MODID)
public class Registry {

    private static List<Item> itemsToRegister;
    private static List<Block> blocksToRegister;
    private static List<SoundEvent> soundsToRegister;
    private static List<Potion> potionsToRegister;
    private static List <Biome> biomesToRegister;
    private static List <Enchantment> enchantmentsToRegister;

    public static List<Item> GetItemsToRegister() {
        if (itemsToRegister == null) itemsToRegister = new ArrayList<>();
        return itemsToRegister;
    }

    public static List<Block> GetBlocksToRegister() {
        if (blocksToRegister == null) blocksToRegister = new ArrayList<>();
        return blocksToRegister;
    }

    public static List<SoundEvent> GetSoundsToRegister() {
        if (soundsToRegister == null) soundsToRegister = new ArrayList<>();
        return soundsToRegister;
    }

    public static List<Potion> GetPotionsToRegister() {
        if (potionsToRegister == null) potionsToRegister = new ArrayList<>();
        return potionsToRegister;
    }

    public static List <Biome> GetBiomesToRegister ( ) {
        if ( biomesToRegister == null ) biomesToRegister = new ArrayList <> ( );
        return biomesToRegister;
    }

    public static List <Enchantment> GetEnchantmentsToRegister ( ) {
        if ( enchantmentsToRegister == null ) enchantmentsToRegister = new ArrayList <> ( );
        return enchantmentsToRegister;
    }

    @SubscribeEvent
    public static void registerBlocks ( RegistryEvent.Register <Block> event ) {

        for ( Block block : Registry.GetBlocksToRegister ( ) ) {
            LogHelper.info("Registering Block:" + block.getRegistryName() + "/ UnLoc: " + block.getUnlocalizedName());
            event.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public static void registerItems ( RegistryEvent.Register <Item> event ) {
        LogHelper.info("Registered Items");
        for ( Item item : Registry.GetItemsToRegister ( ) ) {
            LogHelper.info("Registering Item:" + item.getRegistryName() + "/ UnLoc: " + item.getUnlocalizedName());
            if (item.getRegistryName() == null)
                    LogHelper.error("NULL REGISTRY FOUND HERE: " + item.getDefaultInstance());
            event.getRegistry().register(item);
        }
    }

    @SubscribeEvent
    public static void registerPotions ( RegistryEvent.Register <Potion> event ) {
        for ( Potion potion : Registry.GetPotionsToRegister ( ) )
            event.getRegistry ( ).register ( potion );
    }

    @SubscribeEvent
    public static void registerBiomes ( RegistryEvent.Register <Biome> event ) {
        for ( Biome biome : Registry.GetBiomesToRegister ( ) )
            event.getRegistry ( ).register ( biome );
        RegisterCustomBiomes ( );
    }

    @SubscribeEvent
    public static void registerSoundEvents ( RegistryEvent.Register <SoundEvent> event ) {
        for ( SoundEvent sound : Registry.GetSoundsToRegister ( ) ) {
            LogHelper.info ( "Registering Sound:" + sound.getRegistryName ( ) + "/ UnLoc: " + sound.getSoundName ( ) );
            event.getRegistry ( ).register ( sound );
        }
    }

    @SubscribeEvent
    public void registerEnchantments ( RegistryEvent.Register <Enchantment> event ) {
        for ( Enchantment encha : Registry.GetEnchantmentsToRegister ( ) )
            event.getRegistry ( ).register ( encha );
    }

    private static void RegisterCustomBiomes ( ) {
        if ( ArsMagica2.config.getEnableWitchwoodForest ( ) ) {
            BiomeDictionary.addTypes ( GameRegistry.findRegistry ( Biome.class ).getValue ( BiomeWitchwoodForest.REGISTRY.getNameForObject ( BiomeWitchwoodForest.instance ) ) , BiomeDictionary.Type.FOREST , BiomeDictionary.Type.MAGICAL );
        }
    }
}
