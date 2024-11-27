package net.sirbromate.nodimensionbackground;


import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("nodimensionbackground.properties");
    public static boolean disableNetherBackground = true;
    public static boolean disableEndBackground = true;
    public static boolean disableLoadingPanoramaBackground = false;

    static {
        try (BufferedReader reader = Files.newBufferedReader(CONFIG)) {
            Properties properties = new Properties();
            properties.load(reader);

            Config.disableNetherBackground = Boolean.parseBoolean(properties.getProperty("disable_nether_background"));
            Config.disableEndBackground = Boolean.parseBoolean(properties.getProperty("disable_end_background"));
            Config.disableEndBackground = Boolean.parseBoolean(properties.getProperty("disable_other_background"));
        } catch (Exception e) {
            save();
        }
    }

    static void save() {
        Properties properties = new Properties();
        properties.put("disable_nether_background", String.valueOf(Config.disableNetherBackground));
        properties.put("disable_end_background", String.valueOf(Config.disableEndBackground));
        properties.put("disable_loading_panorama_background", String.valueOf(Config.disableLoadingPanoramaBackground));

        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG)) {
            properties.store(writer, "No Dimension Background config file");
        } catch (IOException e) {
            LogManager.getLogger(Config.class).error(e.getMessage(), e);
        }
    }    
}
