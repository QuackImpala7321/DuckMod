package net.quackimpala7321.duckmod.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.quackimpala7321.duckmod.DuckMod;

import java.io.*;
import java.util.Iterator;

public class ModConfig {
    private static final File FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve(DuckMod.MOD_ID + ".config.json")
            .toFile();

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public JsonObject root;
    private boolean replace;

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

            resolveMissingContents();
        } catch (Exception e) {

        }
    }

    private void write() {
        try (FileWriter fileWriter = new FileWriter(FILE)) {
            FILE.createNewFile();

            this.root = getDefaultConfig();
            fileWriter.write(GSON.toJson(this.root));
        } catch (Exception e) {

        }
    }

    private void resolveMissingContents() {
        JsonObject defaultConfig = getDefaultConfig();

        for (String keyName : defaultConfig.keySet()) {
            resolveIndividual(keyName, this.root, defaultConfig);
        }

        // End of checks
        if(!replace) {
            return;
        }

        try (FileWriter fileWriter = new FileWriter(FILE)) {
            fileWriter.write(GSON.toJson(this.root));
        } catch (Exception e) {

        }
    }

    private void resolveDictionary(String name) {
        replace = false;

        resolveIndividual(name, this.root, getDefaultConfig());

        JsonObject current = this.root.getAsJsonObject(name);
        JsonObject defaultConfig = getDefaultConfig().getAsJsonObject(name);

        if(replace) {
            return;
        }

        for (String keyName : defaultConfig.keySet()) {
            resolveIndividual(keyName, current, defaultConfig);
        }

        this.root.remove(name);
        this.root.add(name, current);
    }

    private void resolveIndividual(String key, JsonObject current, JsonObject defaultConfig) {
        if(current.has(key)) {
            return;
        }

        current.add(key, defaultConfig.get(key));
        DuckMod.LOGGER.info("Adding missing entry for " + key);
        replace = true;
    }

    private JsonObject getDefaultConfig() {
        JsonObject rootObject = new JsonObject();
        JsonObject duck = new JsonObject();

        duck.addProperty("wild_duck_damage", 2.0f);
        duck.addProperty("tamed_duck_damage", 4.0f);
        duck.addProperty("duck_spawn_weight", 8);

        rootObject.add("duck", duck);

        return rootObject;
    }
}
