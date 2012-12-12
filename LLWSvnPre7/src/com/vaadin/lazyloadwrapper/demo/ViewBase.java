package com.vaadin.lazyloadwrapper.demo;

import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.UI;

public abstract class ViewBase extends CustomComponent implements View {

    private DemoParams demoParams;

    @Override
    public void enter(ViewChangeEvent event) {
        String parameters = event.getParameters();

        LazyLoadWrapperApplication rootUI = (LazyLoadWrapperApplication) UI
                .getCurrent();
        demoParams = rootUI.createFeaturesList(parameters);

    }

    /**
     * Used to handle the <i> usingWrappers </i> param automatically..
     * 
     * @param lazyLoadComponent
     * @return
     */
    protected Component wrapOrDontWrapComponent(Component lazyLoadComponent) {
        if (demoParams.isUsingWrappers()) {
            return createWrapper(lazyLoadComponent);
        }

        return lazyLoadComponent;
    }

    protected LazyLoadWrapper createWrapper(Component lazyLoadComponent) {
        LazyLoadWrapper llw = new LazyLoadWrapper();
        llw.setProximity(demoParams.getProximity());
        llw.setPlaceholderVisibleDelay(demoParams.getVisibleDelay());
        llw.setMode(demoParams.getMode());

        if (lazyLoadComponent != null) {
            llw.setLazyLoadComponent(lazyLoadComponent);
        }

        if (demoParams.getPlaceholderHeight() != null
                || demoParams.getPlaceholderWidth() != null) {
            llw.setPlaceHolderSize(demoParams.getPlaceholderWidth(),
                    demoParams.getPlaceholderHeight());
            if (demoParams.isStaticContainer()) {
                llw.setStaticConatiner(demoParams.isStaticContainer());
            }
        }

        return llw;
    }

}
