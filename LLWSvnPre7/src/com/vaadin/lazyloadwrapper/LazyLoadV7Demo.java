package com.vaadin.lazyloadwrapper;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractComponentContainer;
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
        labelsWithDifferentSizes.addComponent(undefSizeLabel);

        Label undefSizeLabelLazy = new Label("Label with unef size");
        labelsWithDifferentSizes.addComponent(new LazyLoadWrapper(
                undefSizeLabelLazy, 0, 1000));

        return labelsWithDifferentSizes;

    }

    public AbstractComponentContainer createTableDemo() {
        VerticalLayout vl = new VerticalLayout();

        Table t = new Table();
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

        Panel p = new Panel(new VerticalLayout());

        LazyLoadWrapper tableLLW = new LazyLoadWrapper(t, 0, 1000);
        tableLLW.setHeight("200px");
        tableLLW.setWidth("200px");
        vl.addComponent(tableLLW);
        // vl.addComponent(t);

        return vl;
    }
}
