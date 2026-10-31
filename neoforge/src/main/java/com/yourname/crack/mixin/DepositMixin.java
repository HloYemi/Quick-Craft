package com.yourname.crack.mixin;

import com.sxilverr.quickcraft.craft.Deposit;
import com.sxilverr.quickcraft.crafting.ItemKey;
import com.sxilverr.quickcraft.integration.projecte.ProjectEIntegration;
import com.yourname.crack.CrackConfig;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.capabilities.PECapabilities;
import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.math.BigInteger;

/**
 * 合成余项（byproduct / crafting remainder）直接折算 EMC 加回玩家账户。
 *
 * <p>不依赖转化桌方块、不依赖玩家是否学习过该物品——只要物品本身在
 * ProjectE 的 EMC 映射里有值，就按单价 × 数量加回。</p>
 *
 * <p>开关：{@link CrackConfig#REMAINDER_TO_EMC}。关闭时完全走原逻辑
 * （余项按用户选择的目的地 / 背包分发）。</p>
 */
@Mixin(Deposit.class)
public abstract class DepositMixin {

    @Shadow @Final private ServerPlayer player;

    @Shadow private int byproducts;

    @Inject(method = "put", at = @At("HEAD"), cancellable = true)
    private void crack$byproductToEmc(ItemKey key, int amount, boolean target, CallbackInfo ci) {
        if (target || amount <= 0) return;
        if (!CrackConfig.REMAINDER_TO_EMC) return;
        if (!ProjectEIntegration.available()) return;

        ItemStack sample = key.toStack(1);
        long unit = IEMCProxy.INSTANCE.getValue(sample);
        if (unit <= 0L) return;

        BigInteger gained = BigInteger.valueOf(unit).multiply(BigInteger.valueOf(amount));
        if (gained.signum() <= 0) return;

        IKnowledgeProvider knowledge = player.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY, null);
        if (knowledge == null) return;

        knowledge.setEmc(knowledge.getEmc().add(gained));
        knowledge.sync(player);

        byproducts += amount;
        ci.cancel();
    }
}
