package com.yourname.crack.mixin;

import com.sxilverr.quickcraft.integration.projecte.ProjectEIntegration;
import com.yourname.crack.CrackConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 让 EMC 来源检测恒为 true。
 *
 * <p>原本 {@link ProjectEIntegration#hasAccess(Player, Level, BlockPos, int)} 要求
 * 背包里有转化桌 tablet，或附近有转化桌方块，才把 EMC 视为可用。
 * 这里在方法入口直接返回 true，使配方树在缺材料时无条件尝试走 EMC 分支。</p>
 *
 * <p><b>注意</b>：这只会放行"来源检测"，真正扣除 EMC 时仍要求玩家账户里
 * 有足够 EMC（转化桌里存过、或身上有可折算 EMC 的物品）。EMC 余额为 0 时
 * 依然合不出来。</p>
 */
@Mixin(ProjectEIntegration.class)
public abstract class ProjectEIntegrationMixin {

    @Inject(method = "hasAccess", at = @At("HEAD"), cancellable = true)
    private static void crack$alwaysHasAccess(Player player, Level level, BlockPos center, int range,
                                              CallbackInfoReturnable<Boolean> cir) {
        if (CrackConfig.ALWAYS_ALLOW_EMC_SOURCE) {
            cir.setReturnValue(true);
        }
    }
}
