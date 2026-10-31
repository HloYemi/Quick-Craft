package com.yourname.crack;

/**
 * Quick Craft 破解开关。
 */
public final class CrackConfig {
    /** 放开所有工作站方块要求（合成台/锻造台/石切台/TACZ/…）。 */
    public static final boolean ALWAYS_ALLOW_STATIONS = true;

    /** 放开 ProjectE 的 EMC 来源检测（不再要求背包有 tablet 或附近有转化桌）。 */
    public static final boolean ALWAYS_ALLOW_EMC_SOURCE = true;

    private CrackConfig() {
    }
}
