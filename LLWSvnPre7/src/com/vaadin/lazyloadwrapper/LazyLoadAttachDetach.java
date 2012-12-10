package com.vaadin.lazyloadwrapper;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.lazyloadwrapper.LazyLoadWrapper.LazyLoadComponentProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class LazyLoadAttachDetach extends Window {

    private Panel p;
    private GridLayout gl = new GridLayout(4, 4);
    private VerticalLayout mainLayout;

    public LazyLoadAttachDetach() {
        super();
        setCaption("2.0");
        gl.setSizeFull();
        addComponent(gl);
        gl.setMargin(true);

        gl.addComponent(createBasicExample(), 0, 0);
        addComponent(createTableExample());

    }

    public void addComponent(Component c) {
        if (mainLayout == null) {
            mainLayout = new VerticalLayout();
            setContent(mainLayout);
        }

        mainLayout.addComponent(c);

    }

    protected Component createBasicExample() {
        final VerticalLayout root = new VerticalLayout();
        Button b = new Button("Add/Remove", new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (p.getParent() == null) {
                    root.addComponent(p);
                } else {
                    root.removeComponent(p);
                }

            }
        });

        root.addComponent(b);

        p = new Panel("This panel will be attached/detached");

        LazyLoadWrapper llw = new LazyLoadWrapper(
                new LazyLoadComponentProvider() {

                    public Component onComponentVisible() {
                        System.out.println("Asking for child");
                        return new Label("Wrapping me...");
                    }
                });

        llw.setMode(llw.MODE_LAZY_LOAD_FETCH);
        llw.setPlaceholderVisibleDelay(2000);

        // llw.setAutoReinitLazyLoad(true);
        // p.addComponent(llw);

        return root;
    }

    Component createTableExample() {
        final VerticalLayout root = new VerticalLayout();
        // root.setSizeFull();
        final Table t = new Table();

        Button b = new Button("Add/remove", new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (t.getParent() == null) {
                    root.addComponent(t);
                } else {
                    root.removeComponent(t);
                }

            }
        });

        t.setWidth("100px");
        t.setHeight("50px");
        root.addComponent(t);

        IndexedContainer ic = new IndexedContainer();
        // for (int i = 0; i < 10; i++) {
        // String s = new String("Table item: " + i);
        // ic.addItem(s);
        //
        // }
        // t.setContainerDataSource(ic);
        // }

        return root;
    }
}
