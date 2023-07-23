package net.quackimpala7321.duckmod.advancement.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.entity.custom.DuckEntity;

public class DuckOnHeadCriterion extends AbstractCriterion<DuckOnHeadCriterion.Conditions> {
    private static final Identifier ID = new Identifier(DuckMod.MOD_ID, "duck_on_head");

    @Override
    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, DuckEntity duckEntity) {
        LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, duckEntity);
        this.trigger(player, (conditions) -> conditions.matches(lootContext));
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        LootContextPredicate entityPredicate = EntityPredicate.contextPredicateFromJson(obj, "entity", predicateDeserializer);
        return new Conditions(playerPredicate, entityPredicate);
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final LootContextPredicate entity;

        public Conditions(LootContextPredicate player, LootContextPredicate entity) {
            super(ID, player);
            this.entity = entity;
        }

        public static Conditions create(EntityPredicate entity) {
            return new Conditions(LootContextPredicate.EMPTY, EntityPredicate.asLootContextPredicate(entity));
        }

        public boolean matches(LootContext duckEntityContext) {
            return this.entity.test(duckEntityContext);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("entity", this.entity.toJson(predicateSerializer));
            return jsonObject;
        }
    }
}
