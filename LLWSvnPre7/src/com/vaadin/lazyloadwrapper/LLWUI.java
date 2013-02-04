package com.vaadin.lazyloadwrapper;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class LLWUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        Label lazyLoadComponent = new Label("This component was lazily loaded");
        lazyLoadComponent.setSizeUndefined();

        LazyLoadWrapper llw = new LazyLoadWrapper(lazyLoadComponent);
        llw.setProximity(0);
        // Set placeholder visible for 5 seconds
        llw.setPlaceholderVisibleDelay(5000);

        layout.addComponent(llw);

    }

}
