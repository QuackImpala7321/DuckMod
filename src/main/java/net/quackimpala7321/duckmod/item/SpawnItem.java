package net.quackimpala7321.duckmod.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SpawnItem extends Item {
    private final EntityType<?> type;

    public SpawnItem(EntityType<?> type, Settings settings) {
        super(settings);
        this.type = type;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        if(world.isClient) return ActionResult.PASS;

        Entity entity = type.create(world);
        if (entity == null) return ActionResult.CONSUME;
        entity.refreshPositionAndAngles(context.getBlockPos().add(0, 1, 0), context.getPlayerYaw(), 0.0f);

        world.spawnEntity(entity);

        ItemStack itemStack = context.getPlayer().getStackInHand(Hand.MAIN_HAND);

        if(itemStack.isEmpty()) return ActionResult.FAIL;
        itemStack.decrement(1);

        return ActionResult.SUCCESS;
    }
}
