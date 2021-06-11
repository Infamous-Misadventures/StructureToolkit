package mod.patrigan.structure_toolkit.util;
import com.mojang.datafixers.util.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class GeneralUtils {

    // Weighted Random from: https://stackoverflow.com/a/6737362
    public static <T> T getRandomEntry(List<Pair<T, Integer>> rlList, Random random) {
        double totalWeight = 0.0;

        // Compute the total weight of all items together.
        for (Pair<T, Integer> pair : rlList) {
            totalWeight += pair.getSecond();
        }

        // Now choose a random item.
        int index = 0;
        for (double randomWeightPicked = random.nextFloat() * totalWeight; index < rlList.size() - 1; ++index) {
            randomWeightPicked -= rlList.get(index).getSecond();
            if (randomWeightPicked <= 0.0) break;
        }

        return rlList.get(index).getFirst();
    }



    public static ItemStack generateItemStack(ServerWorld world, BlockPos pos, ResourceLocation lootTable, Random random)
    {
        LootContext context = new LootContext.Builder(world)
                .withRandom(random)
                .withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(pos)) // positional context
                .create(LootParameterSets.CHEST);	// chest set requires positional context, has no other mandatory parameters

        LootTable table = world.getServer()
                .getLootTables()
                .get(lootTable);
        List<ItemStack> stacks = table.getRandomItems(context);
        return !stacks.isEmpty()
                ? stacks.get(0)
                : ItemStack.EMPTY;
    }
}
