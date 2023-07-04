package net.quackimpala7321.duckmod.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.quackimpala7321.duckmod.entity.ModEntities;
import net.quackimpala7321.duckmod.entity.custom.DuckEntity;

public class DuckEggItem extends Item {
    public DuckEggItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        if(world.isClient) return ActionResult.PASS;

        DuckEntity duckEntity = ModEntities.DUCK_ENTITY.create(world);
        if (duckEntity == null) return ActionResult.CONSUME;
        duckEntity.refreshPositionAndAngles(context.getBlockPos().add(0, 1, 0), context.getPlayerYaw(), 0.0f);

        world.spawnEntity(duckEntity);

        ItemStack itemStack = context.getPlayer().getStackInHand(Hand.MAIN_HAND);

        if(itemStack.isEmpty()) return ActionResult.FAIL;
        itemStack.decrement(1);

        return ActionResult.SUCCESS;
    }
}
