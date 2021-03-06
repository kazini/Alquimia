package vazkii.alquimia.common.lib;

import java.lang.management.ManagementFactory;

public final class LibMisc {

	// Mod Constants
	public static final String MOD_ID = "alquimia";
	public static final String MOD_NAME = "Alquimia";
	public static final String BUILD = "GRADLE:BUILD";
	public static final String VERSION = "GRADLE:VERSION-" + BUILD;
	public static final String DEPENDENCIES = "required-before:autoreglib;";
	public static final String PREFIX_MOD = MOD_ID + ":";
	
	// Proxy Constants
	public static final String PROXY_COMMON = "vazkii.alquimia.common.base.CommonProxy";
	public static final String PROXY_CLIENT = "vazkii.alquimia.client.base.ClientProxy";
	public static final String GUI_FACTORY = "vazkii.alquimia.client.gui.GuiFactory";

}
