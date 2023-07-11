package net.quackimpala7321.duckmod.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class ModConfig {
    private final File FILE;
    private final JsonObject DEFAULT;

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public JsonObject root;
    private boolean replace;

    public ModConfig(String fileName, JsonObject defaultConfig) {
        this.FILE = FabricLoader.getInstance()
                .getConfigDir()
                .resolve(fileName)
                .toFile();

        this.DEFAULT = defaultConfig;
    }

    private JsonObject getDefaultConfig() {
        return this.DEFAULT;
    }

    public JsonElement getValue(String key) {
        String[] set = key.split("\\.");
        JsonObject layer = this.root;

        for(int i=0; i<set.length - 1; i++) {
            layer = layer.getAsJsonObject(set[i]);
        }

        return layer.get(set[set.length - 1]);
    }

    public void load() {
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

    public File getFile() {
        return this.FILE;
    }

    private void resolveMissingContents() {
        replace = false;
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

    private void resolveIndividual(String key, JsonObject current, JsonObject defaultConfig) {
        JsonElement jsonElement = defaultConfig.get(key);

        if(current.has(key) && !(jsonElement instanceof  JsonObject)) {
            return;
        }

        if(current.has(key) && jsonElement instanceof JsonObject) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject thisCurrent = current.getAsJsonObject(key);

            for (String keyName : jsonObject.keySet()) {
                resolveIndividual(keyName, thisCurrent, jsonObject);
            }

            current.remove(key);
            current.add(key, thisCurrent);

            return;
        }

        current.add(key, jsonElement);
        replace = true;
    }
}