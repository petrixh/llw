package com.vaadin.lazyloadwrapper;

import java.io.Serializable;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

public class LazyLoadWrapperApplication extends Application implements
        Serializable {

    private static final long serialVersionUID = -3214543723791435116L;
    Window mainWindow;

    @Override
    public void init() {

        mainWindow = new Window("This is your main Window");

        setMainWindow(mainWindow);

        // wrapperExample();
        LazyLoadWrapperDemo llwdemo = new LazyLoadWrapperDemo(this);
        setMainWindow(llwdemo.createDemo());
        //
        // TableOutOfSync toos = new TableOutOfSync();
        // setMainWindow(toos);

        addWindow(new LazyLoadAttachDetach());

    }

    /**
     * Examples on how to use the lazy load wrapper
     */
    private void wrapperExample() {

        /*
         * Simple example of how to wrap a Panel inside a lazy load wrapper
         */

        Panel examplepanel = new Panel("This is your panel");
        examplepanel.addComponent(new Label("This is your content"));
        examplepanel.addComponent(new Button("A button"));

        /*
         * Set a size for the panel (the wrapper will automatically size the
         * placeholder if child has absolute sizes)
         */
        examplepanel.setWidth("300px");
        examplepanel.setHeight("200px");

        /* This is how you would normally add your panel to your application. */
        // mainWindow.addComponent(examplepanel);

        /* This is how you can easily wrap the component inside the wrapper; */
        mainWindow.addComponent(new LazyLoadWrapper(examplepanel));

        /*
         * If you add a component with a undefined size to the wrapper, the
         * wrapper will create a default placeholder of 100px x 100px that will
         * expand when the wrapped component is loaded.
         */
        Panel p = new Panel("A panel");
        p.setWidth("300px");
        p.addComponent(new Label("A label inside the panel"));

        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setSizeUndefined();

        hl.addComponent(p);
        hl.addComponent(new Label("A label"));
        mainWindow.addComponent(new LazyLoadWrapper(hl));

        /*
         * If we want to, we can size the placeholder size manually so that it
         * resembles the size of the actual component. (We must use absolute
         * values here)
         * 
         * If we overshoot or underestimate the size, the wrapper will resize
         * itself when the component is loaded.
         */

        Button aButton = new Button("Wrapped button");
        mainWindow.addComponent(new LazyLoadWrapper(aButton, "120px", "30px"));

        /*
         * If a component with a relative size is placed in the wrapper, the
         * wrapper will try to match it (i.e. copy it's size parameters) while
         * still creating a placeholder on the client side.
         * 
         * In this example the wrapped child component has a size of: Width =
         * 80%, Height = undefined
         * 
         * The wrapper will copy the sizes from the child component but draw a
         * placeholder with sizes: Width = 80%, Height = 100px (placeholder
         * default height)
         */

        Panel fullSize = new Panel("YAP...");
        fullSize.setWidth("80%");
        fullSize.addComponent(new Label("Some content"));
        fullSize.addComponent(new Label("Some content"));
        fullSize.addComponent(new Label("Some content"));
        fullSize.addComponent(new Label("Some content"));
        fullSize.addComponent(new Label("Some content"));
        fullSize.addComponent(new Label("Some content"));
        fullSize.addComponent(new Label("Some content"));
        fullSize.addComponent(new Label("Some content"));
        fullSize.addComponent(new Label("Some content"));
        mainWindow.addComponent(new LazyLoadWrapper(fullSize));

    }
}
