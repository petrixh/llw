package com.vaadin.lazyloadwrapper;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

@SuppressWarnings({ "javadoc", "serial" })
public class LLWUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        CssLayout layout = new CssLayout();
        // layout.setMargin(true);
        // layout.setSpacing(true);
        setContent(layout);

        Panel scroll = new Panel();
        scroll.setSizeFull();
        scroll.setHeight("500px");
        scroll.setContent(layout);

        Panel scroll2 = new Panel();
        scroll2.setSizeFull();
        CssLayout cssLayout = new CssLayout();
        cssLayout.setWidth("100%");
        scroll2.setContent(cssLayout);
        setContent(scroll2);

        for (int i = 0; i < 100; i++) {
            cssLayout.addComponent(createDummyComponent());
        }

        cssLayout.addComponent(scroll);

        for (int i = 0; i < 100; i++) {
            cssLayout.addComponent(createDummyComponent());
        }

        final Label lazyLoadComponent = new Label(
                "This component was lazily loaded");
        lazyLoadComponent.setSizeUndefined();

        LazyLoadWrapper llw = new LazyLoadWrapper(() -> lazyLoadComponent);

        llw.setProximity(100);
        // Set placeholder visible for 5 seconds
        llw.setPlaceholderVisibleDelay(5000);

        for (int i = 0; i < 100; i++) {
            layout.addComponent(createDummyComponent());
        }

        layout.addComponent(llw);

        for (int i = 0; i < 100; i++) {
            layout.addComponent(createDummyComponent());
        }

    }

    private Component createDummyComponent() {
        Label label = new Label("Dummy");

        label.setHeight("100px");

        return label;
    }

}
