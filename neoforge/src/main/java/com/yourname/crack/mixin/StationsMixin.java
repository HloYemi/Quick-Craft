package com.yourname.crack.mixin;

import com.sxilverr.quickcraft.crafting.Station;
import com.sxilverr.quickcraft.crafting.Stations;
import com.yourname.crack.CrackConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 让所有 {@link Station} 恒视为"已拥有"。
 *
 * <p>效果：</p>
 * <ul>
 *     <li>{@link Stations#has(Station)} 永远返回 true；</li>
 *     <li>{@link Stations#gridSize()} 因此恒为 3（3x3 配方全部可用）；</li>
 *     <li>{@code CraftingRecipeOption.fits} / {@code StationRecipeOption.fits} /
 *         {@code CraftTrees.missingStation} 都不会再判定"缺站点"。</li>
 * </ul>
 */
@Mixin(Stations.class)
public abstract class StationsMixin {

    @Inject(method = "has", at = @At("HEAD"), cancellable = true)
    private void crack$alwaysHas(Station station, CallbackInfoReturnable<Boolean> cir) {
        if (station != null && CrackConfig.ALWAYS_ALLOW_STATIONS) {
            cir.setReturnValue(true);
        }
    }
}
