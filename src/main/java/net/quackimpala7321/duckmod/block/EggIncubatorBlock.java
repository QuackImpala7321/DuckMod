package net.quackimpala7321.duckmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.quackimpala7321.duckmod.entity.DuckBossEntity;
import net.quackimpala7321.duckmod.registry.ModEntities;
import net.quackimpala7321.duckmod.entity.DuckEntity;
import net.quackimpala7321.duckmod.registry.ModItems;
import org.jetbrains.annotations.Nullable;

public class EggIncubatorBlock extends Block {
    public static final int MAX_AGE = 3;

    private static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    private static final EnumProperty<EggType> EGG_TYPE = EnumProperty.of("egg", EggType.class);
    public static final IntProperty AGE = IntProperty.of("age", 0, MAX_AGE);

    public EggIncubatorBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(EGG_TYPE, EggType.NONE)
                .with(AGE, 0));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        if(hasEgg(state)) {
            if(getAge(state) < MAX_AGE)
                return ActionResult.FAIL;

            if(!world.isClient) {
                if(state.get(EGG_TYPE) == EggType.EMBLEM) {
                    world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 1.0f, false, World.ExplosionSourceType.BLOCK);

                    DuckBossEntity duckBossEntity = ModEntities.DUCK_BOSS.create(world);
                    if(duckBossEntity == null) return ActionResult.CONSUME;

                    duckBossEntity.refreshPositionAndAngles(pos, (float) Math.toRadians(90), 0.0f);
                    world.spawnEntity(duckBossEntity);
                } else {
                    world.setBlockState(pos, state.with(EGG_TYPE, EggType.NONE).with(AGE, 0));
                }
            }

            return ActionResult.SUCCESS;
        }

        if(!world.isClient) {
            BlockState blockState = setEggType(state, item);

            if(blockState != null) {
                world.setBlockState(pos, blockState);
            } else {
                return ActionResult.FAIL;
            }
        }

        if(!player.getAbilities().creativeMode)
            itemStack.decrement(1);

        return ActionResult.SUCCESS;
    }

    @Nullable
    private BlockState setEggType(BlockState state, Item item) {
        if (item == Items.EGG) {
            return state.with(EGG_TYPE, EggType.CHICKEN);
        } else if (item == ModItems.DUCK_EGG) {
            return state.with(EGG_TYPE, EggType.DUCK);
        } else if (item == ModItems.MYSTERIOUS_EMBLEM) {
            return state.with(EGG_TYPE, EggType.EMBLEM);
        }

        return null;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(random.nextInt(5) > 0) return;

        world.setBlockState(pos, state.with(AGE, getAge(state) + 1));
    }

    private int getAge(BlockState state) {
        return state.get(AGE);
    }

    private boolean hasEgg(BlockState state) {
        return state.get(EGG_TYPE) != EggType.NONE;
    }

    public enum EggType implements StringIdentifiable {
        CHICKEN("chicken"),
        DUCK("duck"),
        EMBLEM("emblem"),
        NONE("none");

        private final String name;

        EggType (String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return getAge(state) < MAX_AGE && hasEgg(state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(EGG_TYPE, AGE);
    }
}
