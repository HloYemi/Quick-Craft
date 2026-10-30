package com.sxilverr.quickcraft.crafting;

import net.minecraft.network.chat.Component;

public enum Station {
    CRAFTING("block.minecraft.crafting_table", "minecraft:crafting_table"),
    SMITHING("block.minecraft.smithing_table", "minecraft:smithing_table"),
    STONECUTTER("block.minecraft.stonecutter", "minecraft:stonecutter"),
    GUN_SMITH_TABLE("quickcraft.station.gun_smith_table", "tacz:gun_smith_table"),
    AMMO_ASSEMBLY_TABLE("quickcraft.station.ammo_assembly_table", "tacz:workbench_a"),
    ATTACHMENT_TABLE("quickcraft.station.attachment_table", "tacz:workbench_c"),
    EXTREME_CRAFTING("quickcraft.station.extreme_crafting_table", "avaritia:extreme_crafting_table"),
    EXTENDED_BASIC("quickcraft.station.basic_crafting_table", "extendedcrafting:basic_table"),
    EXTENDED_ADVANCED("quickcraft.station.advanced_crafting_table", "extendedcrafting:advanced_table"),
    EXTENDED_ELITE("quickcraft.station.elite_crafting_table", "extendedcrafting:elite_table"),
    EXTENDED_ULTIMATE("quickcraft.station.ultimate_crafting_table", "extendedcrafting:ultimate_table");

    private final String translationKey;
    private final String iconId;

    Station(String translationKey, String iconId) {
        this.translationKey = translationKey;
        this.iconId = iconId;
    }

    public Component displayName() {
        return Component.translatable(translationKey);
    }

    public String iconId() {
        return iconId;
    }
}
