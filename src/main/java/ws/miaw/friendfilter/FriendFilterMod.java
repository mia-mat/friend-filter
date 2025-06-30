package ws.miaw.friendfilter;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = FriendFilterMod.MODID, version = FriendFilterMod.VERSION)
public class FriendFilterMod {
    public static final String MODID = "friendfilter";
    public static final String VERSION = "1.2";

    private static FriendConfiguration config;

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (FriendFilterMod.config == null) {
            FriendFilterMod.config = FriendConfiguration.create();
        }

        MinecraftForge.EVENT_BUS.register(new FriendEventHandler());

        ClientCommandHandler.instance.registerCommand(new FriendFilterCommand());
    }


    public static FriendConfiguration getConfig() {
        return config;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static File getMinecraftFolder() {
        return Minecraft.getMinecraft().mcDataDir;
    }

}
