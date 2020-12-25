package com.fuzs.fivefeetsmall;

import net.minecraftforge.fml.common.Mod;

@Mod(
        modid = FiveFeetSmall.MODID,
        name = FiveFeetSmall.NAME,
        version = FiveFeetSmall.VERSION,
        acceptedMinecraftVersions = FiveFeetSmall.RANGE,
        dependencies = FiveFeetSmall.DEPENDENCIES
)
public class FiveFeetSmall {

    public static final String MODID = "fivefeetsmall";
    public static final String NAME = "Five Feet Small";
    public static final String VERSION = "1.1";
    public static final String RANGE = "[1.12.2]";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2779,)";

}
