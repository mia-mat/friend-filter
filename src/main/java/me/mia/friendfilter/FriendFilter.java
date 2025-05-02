package me.mia.friendfilter;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.File;

@Mod(modid = FriendFilter.MODID, version = FriendFilter.VERSION)
public class FriendFilter {
    public static final String MODID = "friend-filter";
    public static final String VERSION = "1.1";

    public static FriendConfiguration CONFIG;
    private static boolean onHypixel = false;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (FriendFilter.CONFIG == null) {
            FriendFilter.CONFIG = FriendConfiguration.create();
        }

        MinecraftForge.EVENT_BUS.register(new FriendEventHandler());

        ClientCommandHandler.instance.registerCommand(new FriendFilterCommand());
    }


    public static File getMinecraftFolder() {
        return Minecraft.getMinecraft().mcDataDir;
    }

    protected static void setOnHypixel(boolean value) {
        onHypixel = value;
    }

    public static boolean isOnHypixel() {
        return onHypixel;
    }

}
