package net.quackimpala7321.duckmod.advancement;

import net.minecraft.advancement.criterion.Criteria;
import net.quackimpala7321.duckmod.DuckMod;
import net.quackimpala7321.duckmod.advancement.criterion.DuckOnHeadCriterion;

public class ModCriteria {
    public static final DuckOnHeadCriterion DUCK_ON_HEAD = Criteria.register(new DuckOnHeadCriterion());

    public static void registerCriteria() {
        DuckMod.LOGGER.info("Registering Criteria for " + DuckMod.MOD_ID);
    }
}
