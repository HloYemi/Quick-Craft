package com.yourname.crack;

public final class CrackConfig {
    public static final boolean ALWAYS_ALLOW_STATIONS = true;
    public static final boolean ALWAYS_ALLOW_EMC_SOURCE = true;

    /** 合成余项直接折算 EMC 加回玩家账户（不依赖转化桌方块）。 */
    public static final boolean REMAINDER_TO_EMC = true;

    private CrackConfig() {}
}
