package com.yourname.crack;

public final class CrackConfig {
    public static final boolean ALWAYS_ALLOW_STATIONS = true;
    public static final boolean ALWAYS_ALLOW_EMC_SOURCE = true;
    public static final boolean REMAINDER_TO_EMC = true;

    /** 目标物本身有 EMC 值时，直接买，跳过合成。 */
    public static final boolean BUY_TARGET_FIRST = true;

    private CrackConfig() {}
}
