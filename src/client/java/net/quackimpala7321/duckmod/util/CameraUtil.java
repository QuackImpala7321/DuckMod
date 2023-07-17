package net.quackimpala7321.duckmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;

public class CameraUtil {
    public static Camera getCamera() {
        return MinecraftClient.getInstance().gameRenderer.getCamera();
    }
}
