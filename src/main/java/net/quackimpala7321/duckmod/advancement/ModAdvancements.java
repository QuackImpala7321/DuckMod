package net.quackimpala7321.duckmod.advancement;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.item.ModItems;

import java.util.function.Consumer;

public class ModAdvancements implements Consumer<Consumer<Advancement>> {
    @Override
    public void accept(Consumer<Advancement> consumer) {
        Advancement rootAdvancement = Advancement.Builder.create()
                .display(
                        ModItems.DUCK_INGOT,
                        createTitle("duckatron"),
                        createDescription("duckatron"),
                        new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .rewards(AdvancementRewards.Builder.experience(200))
                .criterion("duck_armor", InventoryChangedCriterion.Conditions.items(
                        ModItems.DUCK_HELMET, ModItems.DUCK_CHESTPLATE, ModItems.DUCK_LEGGINGS, ModItems.DUCK_BOOTS))
                .build(consumer, DuckMod.MOD_ID + "/duck");
    }

    private Text createTitle(String advancementName) {
        return Text.translatable("advancement." + DuckMod.MOD_ID + "." + advancementName + ".title");
    }

    private Text createDescription(String advancementName) {
        return Text.translatable("advancement." + DuckMod.MOD_ID + "." + advancementName + ".description");
    }
}
