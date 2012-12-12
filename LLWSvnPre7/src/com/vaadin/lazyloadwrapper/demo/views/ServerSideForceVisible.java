package com.vaadin.lazyloadwrapper.demo.views;

import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.lazyloadwrapper.demo.ViewBase;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ServerSideForceVisible extends ViewBase {

    @Override
    public void enter(ViewChangeEvent event) {
        super.enter(event);

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        setCompositionRoot(layout);

        Label hiddenContent = new Label(
                "This content should become visible only after explicitly ordered to do so...");
        hiddenContent.setHeight("300px");
        hiddenContent.setWidth("300px");

        final LazyLoadWrapper wrapper = createWrapper(hiddenContent);
        wrapper.setProximity(-20000);
        layout.addComponent(wrapper);

        Button showContent = new Button("Show component",
                new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        wrapper.setClientSideIsVisible(true);
                    }
                });

        layout.addComponent(showContent);

        Button hideContent = new Button("Hide component",
                new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        wrapper.setClientSideIsVisible(false);
                    }
                });

        layout.addComponent(hideContent);

    }

}
