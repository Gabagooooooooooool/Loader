package me.gabagool.loader;

import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModClassLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Random;

/**
 * @author Gabagooooooooooool
 * @version 1.0
 * @license AGPLv3
 * Custom remote Forge loader
 */
@Mod(modid = "loader", name = "loader", version = "1.0", clientSideOnly = true)
@SuppressWarnings({"deprecation", "unused"})
public class Loader {
    private static Class<?> mainClass;
    private static Object mainClassInstance;
    private static final Logger logger = LogManager.getLogger("loader");

    @SneakyThrows
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        /* YOU ARE NOT ALLOWED TO REMOVE THIS */ logger.info(">>> Loader by Gabagooooooooooool");
        /* YOU ARE NOT ALLOWED TO REMOVE THIS */ logger.info("> Licensed under GNU Affero General Public License v3.0");
        /* YOU ARE NOT ALLOWED TO REMOVE THIS */ logger.info("> Implements resource loader by robere2");
        /* YOU ARE NOT ALLOWED TO REMOVE THIS */ logger.info("> Usage of this loader for malicious purposes (stealing SSIDs, discord tokens, passwords etc) is disallowed.");
        /* YOU ARE NOT ALLOWED TO REMOVE THIS */ logger.info("> By implementing this loader you are fully aware of the above terms.");
        /* YOU ARE NOT ALLOWED TO REMOVE THIS */ logger.info("> You are not allowed to remove or alter this message by any way.");
        /* YOU ARE NOT ALLOWED TO REMOVE THIS */ logger.warn("I am not responsible for damages made using this loader. Following method may produce issues.");
        File modFile = new File(System.getProperty("java.io.tmpdir") + "aur_rel_" + new Random().nextInt() + ".jar");
        Files.copy(new URL("https://github.com/OctoSplash01/AuroraClient/releases/latest/download/bin.jar").openStream(), modFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        ModClassLoader loader = net.minecraftforge.fml.common.Loader.instance().getModClassLoader();
        loader.addFile(modFile);
        FMLClientHandler.instance().addModAsResource(ForgeModContainer.getInstance());
        // Implemented from robere2 under Apache License 2.0
        Field defaultResourcePacksField;
        try {
            defaultResourcePacksField = Minecraft.class.getDeclaredField("field_110449_ao");
        } catch (NoSuchFieldException e) {
            defaultResourcePacksField = Minecraft.class.getDeclaredField("defaultResourcePacks");
        }
        defaultResourcePacksField.setAccessible(true);
        List<IResourcePack> defaultResourcePacks = (List<IResourcePack>) defaultResourcePacksField.get(Minecraft.getMinecraft());
        defaultResourcePacks.add(new FileResourcePack(modFile));
        defaultResourcePacksField.set(Minecraft.getMinecraft(), defaultResourcePacks);
        Minecraft.getMinecraft().refreshResources();
        //
        mainClass = loader.loadClass("me.aurora.client.Aurora"); // Replace with your mod class file
        mainClassInstance = mainClass.newInstance();
        mainClass.getMethod("preInit", FMLPreInitializationEvent.class).invoke(mainClassInstance, event); // Pre init
    }

    @SneakyThrows
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Minecraft.getMinecraft().refreshResources();
        mainClass.getMethod("init", FMLInitializationEvent.class).invoke(mainClassInstance, event); // Init
    }


}
