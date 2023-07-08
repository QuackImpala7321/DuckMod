package net.quackimpala7321.duckmod.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.quackimpala7321.duckmod.DuckMod;

import java.io.*;

public class ModConfig {
    private static final File FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve(DuckMod.MOD_ID + ".config.json")
            .toFile();

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public JsonObject root = new JsonObject();

    public ModConfig() {

    }

    public void load() {
        DuckMod.LOGGER.info("Loading Config for " + DuckMod.MOD_ID);

        if(FILE.exists()) {
            read();
            return;
        }

        write();
    }

    private void read() {
        try (FileReader fileReader = new FileReader(FILE)){
            this.root = JsonParser.parseReader(fileReader).getAsJsonObject();
        } catch (Exception e) {

        }
    }

    private void write() {
        try (FileWriter fileWriter = new FileWriter(FILE)) {
            FILE.createNewFile();
            fileWriter.write(getDefaultConfig());
        } catch (Exception e) {

        }
    }

    private String getDefaultConfig() {
        JsonObject duck = new JsonObject();

        duck.addProperty("wild_duck_damage", 2.0f);
        duck.addProperty("tamed_duck_damage", 4.0f);
        duck.addProperty("duck_spawn_weight", 8);

        root.add("duck", duck);

        return GSON.toJson(root);
    }
}
