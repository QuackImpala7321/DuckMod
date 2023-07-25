package net.quackimpala7321.duckmod.advancement;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.TameAnimalCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.advancement.criterion.DuckOnHeadCriterion;
import net.quackimpala7321.duckmod.entity.ModEntities;
import net.quackimpala7321.duckmod.item.ModItems;

import java.util.function.Consumer;

public class ModAdvancements implements Consumer<Consumer<Advancement>> {
    @Override
    public void accept(Consumer<Advancement> consumer) {
        Advancement rootAdvancement = ModAdvancements.buildCustom(Advancement.Builder.create()
                        .display(
                                ModItems.DUCK_INGOT,
                                createTitle("duck"),
                                createDescription("duck"),
                                new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                                AdvancementFrame.TASK,
                                false,
                                false,
                                false
                        )
                        .criterion("tamed_duck", TameAnimalCriterion.Conditions.create(EntityPredicate.Builder.create().type(ModEntities.DUCK_ENTITY).build())),
                consumer, "duck/root");

        Advancement duckOnHeadAdvancement = ModAdvancements.buildCustom(Advancement.Builder.create().parent(rootAdvancement)
                .display(
                        ModItems.DUCK_NEST,
                        createTitle("duck_on_head"),
                        createDescription("duck_on_head"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("duck_on_head", DuckOnHeadCriterion.Conditions.create(EntityPredicate.Builder.create().type(ModEntities.DUCK_ENTITY).build())),
                consumer, "duck/duck_on_head");

        Advancement duckatronAdvancement = ModAdvancements.buildCustom(Advancement.Builder.create().parent(rootAdvancement)
                .display(
                        ModItems.DUCK_CHESTPLATE,
                        createTitle("duckatron"),
                        createDescription("duckatron"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .rewards(AdvancementRewards.Builder.experience(200))
                .criterion("duck_armor", InventoryChangedCriterion.Conditions.items(
                        ModItems.DUCK_HELMET, ModItems.DUCK_CHESTPLATE, ModItems.DUCK_LEGGINGS, ModItems.DUCK_BOOTS)),
                consumer, "duck/duckatron");
    }

    private static Advancement buildCustom(Advancement.Builder builder, Consumer<Advancement> consumer, String path) {
        Advancement advancement = builder.build(new Identifier(DuckMod.MOD_ID, path));
        consumer.accept(advancement);
        return advancement;
    }

    private Text createTitle(String advancementName) {
        return Text.translatable("advancement." + DuckMod.MOD_ID + "." + advancementName + ".title");
    }

    private Text createDescription(String advancementName) {
        return Text.translatable("advancement." + DuckMod.MOD_ID + "." + advancementName + ".description");
    }
}
