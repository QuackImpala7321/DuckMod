package net.quackimpala7321.duckmod.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.quackimpala7321.duckmod.registry.ModBlockEntities;

public class DuckBlockEntity extends BlockEntity {
    public DuckBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DUCK_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, DuckBlockEntity blockEntity) {
        if(world.isClient) {
            return;
        }

        int ticksSinceTouch = world.getBlockState(pos).get(DuckBlock.TICKS_SINCE_TOUCH);

        if(ticksSinceTouch < DuckBlock.TICKS_MAX) {
            ticksSinceTouch++;
        }

        world.setBlockState(pos, state.with(DuckBlock.TICKS_SINCE_TOUCH, ticksSinceTouch));
    }
}
