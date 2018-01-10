package com.vaadin.lazyloadwrapper.widgetset.client.ui;

import com.vaadin.shared.annotations.DelegateToWidget;
import com.vaadin.shared.ui.AbstractComponentContainerState;

public class LLWState extends AbstractComponentContainerState
{

    /**
     * Current mode of the LLW
     */
    public int mode = LazyLoadWrapperConnector.MODE_LAZY_LOAD_FETCH;

    public boolean clientSideIsVisible = false;

    /**
     * Not implemented fully!
     */
    public boolean autoReinitLazyLoad = false;

    /**
     * The proximity in pixels from the view area when the component should be
     * loaded. Positive value means that the component should be loaded before
     * it's actually visible and negative value means that the component should
     * be <i>X</i> px visible before it's drawn.
     */
    @DelegateToWidget
    public int proximity = 250;

    /**
     * The delay in milliseconds how long the component should be visible on the
     * client side before it's actually drawn.
     */
    public int placeholderVisibleDelay = 0;

    /**
     * Defines if the container of the lazy load wrapper should be static or
     * not. If false, the container will expand to fit the child component,
     * while if true, the container will keep it's size and force the child to
     * be drawn within the size defined by the placeholder.
     */
    public boolean staticContainer = false;

    public String placeholderHeight = "100px";
    public String placeholderWidth = "100px";

    /**
     * Debug flag to indicate that the LLW should print additional debug
     * information.
     */
    @DelegateToWidget
    public boolean debug = false;

}
