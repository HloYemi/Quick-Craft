package com.sxilverr.quickcraft.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record LabeledSource(String id, Component name, ItemStack icon, BlockPos pos, ItemSource source,
                            boolean depositable) {
    public static final String SELF_ID = "self";

    public boolean isSelf() {
        return SELF_ID.equals(id);
    }

    public Component pickerLabel() {
        if (pos == null) return name;
        return name.copy().append(" (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")");
    }
}
