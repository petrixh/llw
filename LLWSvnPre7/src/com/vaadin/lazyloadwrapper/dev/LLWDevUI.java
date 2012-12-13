package com.vaadin.lazyloadwrapper.dev;

import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

public class LLWDevUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        Label l = new Label("Dev ui initialized");

        LazyLoadWrapper llw = new LazyLoadWrapper();
        llw.setMode(llw.MODE_LAZY_LOAD_DRAW);
        llw.setLazyLoadComponent(l);
        llw.setPlaceholderVisibleDelay(1500);

        setContent(llw);
    }

}
