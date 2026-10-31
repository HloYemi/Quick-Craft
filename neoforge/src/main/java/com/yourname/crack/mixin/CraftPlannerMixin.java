package com.yourname.crack.mixin;

import com.sxilverr.quickcraft.craft.CraftNode;
import com.sxilverr.quickcraft.craft.CraftPlanner;
import com.sxilverr.quickcraft.craft.EmcBank;
import com.sxilverr.quickcraft.craft.EmcSource;
import com.sxilverr.quickcraft.craft.VirtualPool;
import com.sxilverr.quickcraft.crafting.Availability;
import com.sxilverr.quickcraft.crafting.ItemKey;
import com.sxilverr.quickcraft.crafting.Stations;
import com.sxilverr.quickcraft.crafting.TreeBuilder;
import com.yourname.crack.CrackConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 目标物本身有 EMC 值时，直接买，跳过合成。
 *
 * <p>问题：楼梯配方是"6 木板 → 4 楼梯"，下单 1 个楼梯时必须合成一整批，
 * 消耗 6 木板 = 48 EMC，多出的 3 个退回 EMC。用户看到"下单 1 消耗 48 EMC"，
 * 体验很差。</p>
 *
 * <p>修复：如果 target 本身已学习且有 EMC 值，且余额够直接买，直接构造
 * 一个"全部购买"的 Plan，跳过 {@code CraftExecutor.simulate}。</p>
 *
 * <p>效果：</p>
 * <ul>
 *     <li>下单 1 楼梯 → 消耗 ~12 EMC，得 1 楼梯（不合成）；</li>
 *     <li>下单 10 楼梯 → 消耗 ~120 EMC，得 10 楼梯；</li>
 *     <li>没学习过 / 无 EMC 值 / 余额不足 → 回退到原合成逻辑。</li>
 * </ul>
 *
 * <p>开关：{@link CrackConfig#BUY_TARGET_FIRST}。</p>
 */
@Mixin(CraftPlanner.class)
public abstract class CraftPlannerMixin {

    @Inject(method = "plan", at = @At("HEAD"), cancellable = true)
    private static void crack$buyTargetFirst(TreeBuilder builder, ItemStack target, int quantity,
                                              Map<ItemKey, ResourceLocation> overrides,
                                              Map<String, Item> ingredientChoices,
                                              VirtualPool initial, Availability availability,
                                              Stations stations, boolean collapseOwned, boolean hideLooping,
                                              EmcSource emc, BigInteger budget,
                                              CallbackInfoReturnable<CraftPlanner.Plan> cir) {
        if (!CrackConfig.BUY_TARGET_FIRST) return;
        if (emc == null || target == null || target.isEmpty() || initial == null) return;

        int qty = Math.max(1, quantity);
        ItemKey targetKey = ItemKey.of(target);
        ItemStack sample = targetKey.toStack(1);
        if (!emc.learned(sample)) return;
        long unit = emc.value(sample);
        if (unit <= 0L) return;

        int have = initial.count(targetKey);
        int need = qty - have;
        if (need <= 0) return;

        BigInteger available = budget == null ? emc.emc() : budget;
        BigInteger cost = BigInteger.valueOf(unit).multiply(BigInteger.valueOf(need));
        if (cost.compareTo(available) > 0) return;   // 余额不够买全部，回退到合成

        builder.setEmcLookup(emc);
        CraftNode root = builder.build(target, qty, overrides, ingredientChoices, availability, stations,
                collapseOwned, hideLooping);
        Set<ItemKey> keys = collectKeys(root, new HashSet<>());
        keys.addAll(builder.loopIngredientKeys());
        EmcBank bank = emc.bank(keys, available);

        VirtualPool working = initial.copy();
        working.setEmc(bank);
        if (!bank.buy(targetKey, need)) return;
        working.produce(targetKey, need);

        CraftPlanner.Plan plan = new CraftPlanner.Plan(
                root, initial, working, bank, keys, qty, qty, builder.truncated(), List.of()
        );
        cir.setReturnValue(plan);
    }

    private static Set<ItemKey> collectKeys(CraftNode node, Set<ItemKey> out) {
        out.add(ItemKey.of(node.output));
        for (CraftNode child : node.children) collectKeys(child, out);
        return out;
    }
}
