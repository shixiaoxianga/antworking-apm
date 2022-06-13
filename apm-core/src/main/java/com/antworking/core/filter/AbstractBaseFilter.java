package com.antworking.core.filter;

import com.antworking.model.base.BaseCollectModel;

public abstract class AbstractBaseFilter implements IFilter {


    @Override
    public boolean filter(BaseCollectModel model) {

        return doFilter(model);
    }

    protected abstract boolean doFilter(BaseCollectModel model);
}
