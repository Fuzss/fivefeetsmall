package com.fuzs.aquaacrobatics.config;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = AquaAcrobatics.MODID)
@Mod.EventBusSubscriber
public class ConfigHandler {

	@Config.Name("Sneaking Height")
	@Config.Comment("Set the height while sneaking. Default value is 1.65 in Minecraft 1.12, and 1.5 in Minecraft 1.14.\n\u00A7cBe careful changing this as it might cause issues and possibly even world corruption.")
	@Config.RangeDouble(min = 0, max = 24)
	public static double sneakingHeight = 1.5;

	@Config.Name("Sneaking Width")
	@Config.Comment("Set the width while sneaking. Default value is 0.6 in vanilla.\n\u00A7cBe careful changing this as it might cause issues and possibly even world corruption.")
	@Config.RangeDouble(min = 0, max = 24)
	public static double sneakingWidth = 0.6;

	@Config.Name("Sneaking Eyes")
	@Config.Comment("Set the eye height while sneaking. Default value is 1.57 in Minecraft 1.12, and 1.27 in Minecraft 1.14.")
	@Config.RangeDouble(min = 0, max = 24)
	public static double sneakingEyes = 1.27;

	@Config.Name("Adjust Size")
	@Config.Comment("Force the player to stay in sneaking position when the available space is less than 1.8 blocks (default player height) tall.")
	public static boolean adjustSize = true;

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent evt) {

		if (evt.getModID().equals(AquaAcrobatics.MODID)) {

			ConfigManager.sync(AquaAcrobatics.MODID, Type.INSTANCE);
		}
	}
	
}