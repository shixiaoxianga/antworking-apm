package com.antworking.core.plugin;

import com.antworking.core.enhance.AwEnhanceStatement;
import com.antworking.core.interceptor.AntWorkingDynamicVariable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class PluginInst {
    private List<AwEnhanceStatement> statements = new ArrayList<>();
}
