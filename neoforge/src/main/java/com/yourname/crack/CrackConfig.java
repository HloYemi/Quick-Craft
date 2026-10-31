package com.yourname.crack;

/**
 * Quick Craft 破解开关。
 *
 * <p>ALWAYS_ALLOW_STATIONS = true 时：</p>
 * <ul>
 *     <li>所有 {@code Station} 恒视为"已拥有"；</li>
 *     <li>3x3 配方、锻造、石切、TACZ、Avaritia、ExtendedCrafting 等全部可合成；</li>
 *     <li>玩家无需附近有任何工作方块，无需携带工作台物品，无需装备升级件。</li>
 * </ul>
 */
public final class CrackConfig {
    public static final boolean ALWAYS_ALLOW_STATIONS = true;

    private CrackConfig() {
    }
}
