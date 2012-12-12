package com.vaadin.lazyloadwrapper.demo.views;

import com.vaadin.lazyloadwrapper.demo.ViewBase;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class PhotoArchive extends ViewBase {

    @Override
    public void enter(ViewChangeEvent event) {
        super.enter(event);
        HorizontalLayout base = new HorizontalLayout();
        setCompositionRoot(base);

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        for (int i = 0; i < 10; i++) {
            Embedded e = new Embedded(
                    null,
                    new ExternalResource(
                            "http://www.freeimageslive.com/galleries/nature/sky/pics/nature00539.jpg"));

            e.setWidth("576px");
            e.setHeight("432px");

            layout.addComponent(wrapOrDontWrapComponent(e));
        }

        VerticalLayout layout2 = new VerticalLayout();
        layout2.setSpacing(true);

        for (int i = 0; i < 10; i++) {
            Embedded e = new Embedded(
                    null,
                    new ExternalResource(
                            "http://www.freeimageslive.com/galleries/nature/sky/pics/nature00539.jpg"));

            e.setWidth("576px");
            e.setHeight("432px");

            layout2.addComponent(wrapOrDontWrapComponent(e));
        }

        base.addComponent(layout);
        base.addComponent(layout2);

    }

}
