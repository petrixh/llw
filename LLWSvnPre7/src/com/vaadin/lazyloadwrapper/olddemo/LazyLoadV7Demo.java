package com.vaadin.lazyloadwrapper.olddemo;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class LazyLoadV7Demo {

    public LazyLoadV7Demo() {

    }

    public AbstractComponentContainer crateLabelsDemo() {

        VerticalLayout labelsWithDifferentSizes = new VerticalLayout();

        Label defaultLabelOrig = new Label("This is a default label");
        labelsWithDifferentSizes.addComponent(defaultLabelOrig);

        Label defaultLabel = new Label("This is a default label");

        labelsWithDifferentSizes.addComponent(new LazyLoadWrapper(defaultLabel,
                0, 10000000));

        Label undefSizeLabel = new Label("Label with unef size");
        undefSizeLabel.setSizeUndefined();
        labelsWithDifferentSizes.addComponent(undefSizeLabel);

        Label undefSizeLabelLazy = new Label("Label with unef size");
        undefSizeLabelLazy.setSizeUndefined();
        labelsWithDifferentSizes.addComponent(new LazyLoadWrapper(
                undefSizeLabelLazy, 0, 1000));

        return labelsWithDifferentSizes;

    }

    public AbstractComponentContainer createTableDemo() {
        VerticalLayout vl = new VerticalLayout();

        Table t = new Table();
        t.setSizeFull();
        // t.setWidth("100px");
        t.setHeight("200px");

        IndexedContainer ic = new IndexedContainer();
        ic.addContainerProperty("Name", String.class, null);
        ic.addContainerProperty("value", Integer.class, 0);

        for (int i = 0; i < 100; i++) {
            String name = "Foobaar" + i;
            ic.addItem(name);
            ic.getContainerProperty(name, "Name").setValue(name);
            ic.getContainerProperty(name, "value").setValue(i);
        }

        t.setContainerDataSource(ic);

        VerticalLayout panelLayout = new VerticalLayout();
        Panel p = new Panel(panelLayout);
        panelLayout.addComponent(t);

        LazyLoadWrapper tableLLW = new LazyLoadWrapper(t, 0, 1000);
        tableLLW.setPlaceHolderSize("200px", "100px");
        // tableLLW.setStaticConatiner(true);
        vl.addComponent(tableLLW);
        // vl.addComponent(t);

        return vl;
    }

    public Component photoShowDemo() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        for (int i = 39; i < 41; i++) {
            Embedded e = new Embedded(
                    null,
                    new ExternalResource(
                            "http://www.freeimageslive.com/galleries/nature/sky/pics/nature00539.jpg"));

            if (i == 39) {
                layout.addComponent(e);
                e.setHeight("864px");
                continue;
            }
            LazyLoadWrapper llw = new LazyLoadWrapper(e, "1152px", "864px");
            llw.setProximity(250);
            llw.setPlaceholderVisibleDelay(5000);
            llw.setMode(LazyLoadWrapper.MODE_LAZY_LOAD_DRAW);
            layout.addComponent(llw);

        }

        Label l = new Label("Spacer...");
        l.setHeight("1000px");
        layout.addComponent(l);

        // TabSheet p = new TabSheet(layout, new Label("Foo"));
        // p.setHeight("400px");

        return layout;
    }
}
