package vazkii.alquimia.common.base;

import net.minecraft.advancements.AdvancementManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import vazkii.alquimia.common.Alquimia;
import vazkii.alquimia.common.handler.AdvancementSyncHandler;
import vazkii.alquimia.common.item.ModItems;
import vazkii.alquimia.common.network.GuiHandler;
import vazkii.alquimia.common.network.ModPackets;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ModItems.preInit();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(Alquimia.instance, new GuiHandler());
		
		MinecraftForge.EVENT_BUS.register(AdvancementSyncHandler.class);
	}
	
	public void init(FMLInitializationEvent event) {
		ModPackets.registerPackets();
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
}
