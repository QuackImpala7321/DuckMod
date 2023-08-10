package net.quackimpala7321.duckmod.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.quackimpala7321.duckmod.registry.ModBlockEntities;
import net.quackimpala7321.duckmod.registry.ModItems;
import org.jetbrains.annotations.Nullable;

public class DuckBlock extends BlockWithEntity {
    public DuckBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(TICKS_SINCE_TOUCH, 0));
    }

    public static final int TICKS_MAX = 10;
    public static final IntProperty TICKS_SINCE_TOUCH = IntProperty.of("ticks", 0, TICKS_MAX);

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);

        if(world.isClient) {
            return;
        }

        if(!(entity instanceof PlayerEntity)) {
            return;
        }

        if(world.getBlockState(pos).get(TICKS_SINCE_TOUCH) < TICKS_MAX) {
            return;
        }

        world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, ModItems.DUCK.getDefaultStack()));
        world.setBlockState(pos, state.with(TICKS_SINCE_TOUCH, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TICKS_SINCE_TOUCH);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.DUCK_BLOCK_ENTITY, (world1, pos, state1, blockEntity) -> DuckBlockEntity.tick(world1, pos, state1, blockEntity));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DuckBlockEntity(pos, state);
    }
}
