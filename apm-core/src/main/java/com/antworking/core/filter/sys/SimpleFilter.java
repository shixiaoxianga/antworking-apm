package com.antworking.core.filter.sys;

import com.antworking.core.filter.AbstractBaseFilter;
import com.antworking.model.base.BaseCollectModel;

public class SimpleFilter extends AbstractBaseFilter {
    @Override
    protected boolean doFilter(BaseCollectModel model) {
        return false;
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }
}
