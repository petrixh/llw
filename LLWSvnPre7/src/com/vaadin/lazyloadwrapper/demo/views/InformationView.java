package com.vaadin.lazyloadwrapper.demo.views;

import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.lazyloadwrapper.demo.ViewBase;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class InformationView extends ViewBase {

    private String version = "1.1 alpha";

    @Override
    public void enter(ViewChangeEvent event) {

        setCompositionRoot(new HorizontalLayout());

        createCaptionLabel();

        LazyLoadWrapper llw = new LazyLoadWrapper();
        getCompositionRoot().addComponent(llw);

        HorizontalLayout layout = (HorizontalLayout) getCompositionRoot();
        layout.setSpacing(true);
        layout.setWidth("100%");

        layout.setExpandRatio(layout.getComponent(0), 1f);
    }

    private void createCaptionLabel() {
        Label caption = new Label(
                "<h2>Lazy Load Wrapper v."
                        + version
                        + "</h2><p>Lazy load wrapper is a wrapper class for Vaadin that can take any Vaadin GUI component"
                        + " and load it lazily when it's scrolled in to view.</p> "
                        + "<p>The wrapper can cut down the time it takes to render a heavy page upon initialization, as heavy components are not "
                        + "loaded and rendered before they are actually needed. The wrapper itself creates a lightweight placeholder in place of the component on the client side."
                        + "<h2>Heavy application example</h2><p>Below is an example of how lazy loading wrappers can be used to speed up the "
                        + "start up of a heavy application. The application in it self is not supposed to do anything except consume a lot of resources... You can set a "
                        + "heaviness factor to see the difference in loading times. The heaviness factor adds more components to the \"heavy compnent\" at the bottom that will, without the wrapper, "
                        + " slow down the initial startup of the application. If you want to actually se the wrappers you can select the \"Show me the "
                        + "wrappers\" selector in order to see how the wrapper loads the components. When the \"Show me the wrappers\" selector is enabled, the wrappers are configured to "
                        + "load a component when it's at least 100 pixels visible and have been visible for 1 second.</p>",
                ContentMode.HTML);

        getCompositionRoot().addComponent(caption);
    }

    @Override
    protected ComponentContainer getCompositionRoot() {
        return (ComponentContainer) super.getCompositionRoot();
    }

}
