package com.vaadin.lazyloadwrapper.widgetset.client.ui;

import com.vaadin.shared.AbstractComponentState;

public class LLWState extends AbstractComponentState {

    /**
     * Current mode of the LLW
     */
    private int mode = LazyLoadWrapperConnector.MODE_LAZY_LOAD_FETCH;

    private boolean clientSideIsVisible = false;
    private boolean autoReinitLazyLoad = false;

    /**
     * The proximity in pixels from the view area when the component should be
     * loaded. Positive value means that the component should be loaded before
     * it's actually visible and negative value means that the component should
     * be <i>X</i> px visible before it's drawn.
     */
    private int proximity = 250;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isClientSideIsVisible() {
        return clientSideIsVisible;
    }

    public void setClientSideIsVisible(boolean clientSideIsVisible) {
        this.clientSideIsVisible = clientSideIsVisible;
    }

    public boolean isAutoReinitLazyLoad() {
        return autoReinitLazyLoad;
    }

    public void setAutoReinitLazyLoad(boolean autoReinitLazyLoad) {
        this.autoReinitLazyLoad = autoReinitLazyLoad;
    }

    public int getProximity() {
        return proximity;
    }

    public void setProximity(int proximity) {
        this.proximity = proximity;
    }

    public int getPlaceholderVisibleDelay() {
        return placeholderVisibleDelay;
    }

    public void setPlaceholderVisibleDelay(int placeholderVisibleDelay) {
        this.placeholderVisibleDelay = placeholderVisibleDelay;
    }

    public boolean isStaticContainer() {
        return staticContainer;
    }

    public void setStaticContainer(boolean staticContainer) {
        this.staticContainer = staticContainer;
    }

    public String getPlaceholderHeight() {
        return placeholderHeight;
    }

    public void setPlaceholderHeight(String placeholderHeight) {
        this.placeholderHeight = placeholderHeight;
    }

    public String getPlaceholderWidth() {
        return placeholderWidth;
    }

    public void setPlaceholderWidth(String placeholderWidth) {
        this.placeholderWidth = placeholderWidth;
    }

    /**
     * The delay in milliseconds how long the component should be visible on the
     * client side before it's actually drawn.
     */
    private int placeholderVisibleDelay = 0;

    /**
     * Defines if the container of the lazy load wrapper should be static or
     * not. If false, the container will expand to fit the child component,
     * while if true, the container will keep it's size and force the child to
     * be drawn within the size defined by the placeholder.
     */
    private boolean staticContainer = false;

    private String placeholderHeight = "100px";
    private String placeholderWidth = "100px";

}
