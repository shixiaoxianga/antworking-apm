package com.antworking.core.start;

import java.util.ArrayList;
import java.util.List;

public class AwApplyStartManager {
    public static List<AwApplicationStart> starts = new ArrayList<>();

    static {
        starts.add(new AwApplySystemInfoInit());
    }
}
