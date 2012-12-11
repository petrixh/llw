package com.vaadin.lazyloadwrapper;

import java.io.Serializable;

import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Widgetset("com.vaadin.lazyloadwrapper.widgetset.LazyLoadWrapperWidgetset")
public class LazyLoadWrapperApplication extends UI implements Serializable {

    private static final long serialVersionUID = -3214543723791435116L;
    Window mainWindow;

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout vl = new VerticalLayout();
        vl.setMargin(true);
        vl.setSpacing(false);
        setContent(vl);

        // vl.addComponent(new Label("Hello!"));
        // LazyLoadWrapper llw = new LazyLoadWrapper();
        // llw.setPlaceHolderSize("150px", "150px");
        // vl.addComponent(llw);
        // llw.setPlaceholderVisibleDelay(2250);
        //
        // Panel panel = new Panel();
        // VerticalLayout panelVl = new VerticalLayout();
        // panel.setContent(panelVl);
        // panelVl.addComponent(new Label("Lazily loaded content"));
        // panelVl.addComponent(new InlineDateField());
        //
        // llw.setLazyLoadComponent(panel);
        //
        // Panel vl2 = new Panel();
        // VerticalLayout panelVl2 = new VerticalLayout();
        // vl2.setContent(panelVl2);
        //
        // panelVl2.addComponent(new Label(
        // "Lazily loaded content should look like this!"));
        // panelVl2.addComponent(new InlineDateField());
        //
        // vl.addComponent(vl2);
        //
        // LazyLoadWrapperDemo llwDemo = new LazyLoadWrapperDemo();
        // vl.addComponent(llwDemo.createDemo().getContent());
        //
        // vl.addComponent(new LazyLoadV7Demo().createTableDemo());
        // vl.addComponent(new LazyLoadV7Demo().crateLabelsDemo());

        vl.addComponent(new LazyLoadV7Demo().photoShowDemo());

    }

    // /**
    // * Examples on how to use the lazy load wrapper
    // */
    // private void wrapperExample() {
    //
    // /*
    // * Simple example of how to wrap a Panel inside a lazy load wrapper
    // */
    //
    // Panel examplepanel = new Panel("This is your panel");
    // examplepanel.addComponent(new Label("This is your content"));
    // examplepanel.addComponent(new Button("A button"));
    //
    // /*
    // * Set a size for the panel (the wrapper will automatically size the
    // * placeholder if child has absolute sizes)
    // */
    // examplepanel.setWidth("300px");
    // examplepanel.setHeight("200px");
    //
    // /* This is how you would normally add your panel to your application. */
    // // mainWindow.addComponent(examplepanel);
    //
    // /* This is how you can easily wrap the component inside the wrapper; */
    // mainWindow.addComponent(new LazyLoadWrapper(examplepanel));
    //
    // /*
    // * If you add a component with a undefined size to the wrapper, the
    // * wrapper will create a default placeholder of 100px x 100px that will
    // * expand when the wrapped component is loaded.
    // */
    // Panel p = new Panel("A panel");
    // p.setWidth("300px");
    // p.addComponent(new Label("A label inside the panel"));
    //
    // HorizontalLayout hl = new HorizontalLayout();
    // hl.setSpacing(true);
    // hl.setSizeUndefined();
    //
    // hl.addComponent(p);
    // hl.addComponent(new Label("A label"));
    // mainWindow.addComponent(new LazyLoadWrapper(hl));
    //
    // /*
    // * If we want to, we can size the placeholder size manually so that it
    // * resembles the size of the actual component. (We must use absolute
    // * values here)
    // *
    // * If we overshoot or underestimate the size, the wrapper will resize
    // * itself when the component is loaded.
    // */
    //
    // Button aButton = new Button("Wrapped button");
    // mainWindow.addComponent(new LazyLoadWrapper(aButton, "120px", "30px"));
    //
    // /*
    // * If a component with a relative size is placed in the wrapper, the
    // * wrapper will try to match it (i.e. copy it's size parameters) while
    // * still creating a placeholder on the client side.
    // *
    // * In this example the wrapped child component has a size of: Width =
    // * 80%, Height = undefined
    // *
    // * The wrapper will copy the sizes from the child component but draw a
    // * placeholder with sizes: Width = 80%, Height = 100px (placeholder
    // * default height)
    // */
    //
    // Panel fullSize = new Panel("YAP...");
    // fullSize.setWidth("80%");
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // mainWindow.addComponent(new LazyLoadWrapper(fullSize));
    //
    // }
}
