package com.vaadin.lazyloadwrapper.dev;

import com.vaadin.lazyloadwrapper.demo.LazyLoadWrapperApplication;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

/**
 * UI provider to load different UI instances depening on what to test..
 * 
 * @author vaadin
 * 
 */
public class CustomUIProvider extends UIProvider {

    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        // TODO Auto-generated method stub
        String pathInfo = event.getRequest().getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            return LazyLoadWrapperApplication.class;
        }

        if (pathInfo.contains("dev")) {
            return LLWDevUI.class;
        }

        return null;
    }

}
