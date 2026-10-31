package com.yourname.crack.mixin;

import com.sxilverr.quickcraft.crafting.Station;
import com.sxilverr.quickcraft.crafting.Stations;
import com.yourname.crack.CrackConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Stations.class)
public abstract class StationsMixin {
    @Inject(method = "has", at = @At("HEAD"), cancellable = true)
    private void crack$alwaysHas(Station station, CallbackInfoReturnable<Boolean> cir) {
        if (station != null && CrackConfig.ALWAYS_ALLOW_STATIONS) {
            cir.setReturnValue(true);
        }
    }
}
