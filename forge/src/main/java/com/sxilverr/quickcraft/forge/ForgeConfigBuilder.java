package com.sxilverr.quickcraft.forge;

import com.sxilverr.quickcraft.QuickCraftCommon;
import com.sxilverr.quickcraft.config.ConfigBuilder;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class ForgeConfigBuilder implements ConfigBuilder {
    private static final String KEY = QuickCraftCommon.MODID + ".configuration.";

    private final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public ForgeConfigSpec build() {
        return builder.build();
    }

    @Override
    public ConfigBuilder comment(String... lines) {
        builder.comment(lines);
        return this;
    }

    @Override
    public ConfigBuilder push(String name) {
        builder.translation(KEY + name).push(name);
        return this;
    }

    @Override
    public ConfigBuilder pop() {
        builder.pop();
        return this;
    }

    @Override
    public Supplier<Boolean> define(String name, boolean def) {
        return builder.translation(KEY + name).define(name, def);
    }

    @Override
    public Supplier<String> define(String name, String def) {
        return builder.translation(KEY + name).define(name, def);
    }

    @Override
    public Supplier<String> define(String name, String def, Predicate<Object> validator) {
        return builder.translation(KEY + name).define(name, def, validator);
    }

    @Override
    public Supplier<Integer> defineInRange(String name, int def, int min, int max) {
        return builder.translation(KEY + name).defineInRange(name, def, min, max);
    }

    @Override
    public Supplier<Double> defineInRange(String name, double def, double min, double max) {
        return builder.translation(KEY + name).defineInRange(name, def, min, max);
    }

    @Override
    public Supplier<List<? extends String>> defineList(String name, List<String> def, String newElement, Predicate<Object> validator) {
        return builder.translation(KEY + name).defineListAllowEmpty(name, def, validator);
    }
}
