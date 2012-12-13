package com.vaadin.lazyloadwrapper.widgetset.client.ui.gwt;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.UIDL;
import com.vaadin.client.VCaptionWrapper;
import com.vaadin.client.VConsole;

/**
 * Client side implementation of LazyLoadWrapper. The wrapper creates a
 * placeholder with a spinner on the view and notifies the server or loads the
 * lazy load component when the placeholder is visible.
 * 
 * When the wrapper receives the lazy load component from the server, the
 * wrapper replaces the placeholder with the actual component. (Default mode) <br>
 * <br>
 * If the mode is set to {@link #MODE_LAZY_LOAD_DRAW} the wrapper will wrap the
 * child component on the client side until it becomes visible and then render
 * it.
 * 
 */

public class VLazyLoadWrapper extends SimplePanel {

    public static final String WRAPPER_AUTOREINIT_ON_REATTACH = "autoreinit";
    /*- Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-lazyloadingwrapper";
    public static final String LOADING_CLASSNAME = "v-lazyloadingwrapper-loading";

    /* The ID's used in server communication */
    public static final String WIDGET_VISIBLE_ID = "widgetvisible";
    public static final String WIDGET_LOAD_PROXIMITY = "proximity";
    public static final String WIDGET_LOAD_VISIBLE_DELAY = "visibledelay";
    public static final String STATIC_CONTAINER = "staticcontainer";
    public static final String PLACEHOLDER_HEIGHT = "placeholderheight";
    public static final String PLACEHOLDER_WIDTH = "placeholderwidth";
    public static final String WRAPPER_MODE = "wrappermode";

    /** The client side widget identifier */
    protected String wrappersPaintableId;

    /** Reference to the server connection object. */
    protected ApplicationConnection client;

    private int proximity = 0;

    private VCaptionWrapper captionWrapper;
    private UIDL childUIDL;

    private int mode = 0;
    private boolean staticContainer = false;

    /** Timer used when visible delay is defined */
    private Timer visibleDelayTimer = null;

    private Element placeholder = null;
    private boolean recentlyAttached = false;

    @Override
    protected void onAttach() {
        super.onAttach();
        /*
         * We take note that the wrapper has just been attached. The recently
         * attached parameter will be set to false during the next update from
         * the server, but this way we can catch the event where the wrapper is
         * reattached and should automatically reinitialize itself...
         */
        recentlyAttached = true;
    }

    /**
     * Creates a new instance of the Lazy load wrapper (LLW) client side
     * implementation, sets the style name and initiates the visibility polling
     * timer if it's not initiated.
     */
    public VLazyLoadWrapper() {
        super();

        createPlaceholder();
    }

    private void createPlaceholder() {
        /* Set the style name (spinner) to the placeholder */
        setStylePrimaryName(LOADING_CLASSNAME);
        if (placeholder == null) {
            placeholder = DOM.createDiv();
        }
        getElement().appendChild(placeholder);
    }

    /**
     * Removes the placeholder from this LLW
     */
    protected void removePlaceholder() {
        if (placeholder != null) {
            getElement().removeChild(placeholder);
            // placeholder = null;
        }
    }

    public void setPlaceHolderSize(String height, String width) {
        if (placeholder != null) {
            placeholder.getStyle().setProperty("height", height);
            placeholder.getStyle().setProperty("width", width);
        }
    }

    /**
     * Draw the child component(s)
     */
    public void lateDrawChild(List<ComponentConnector> children) {

        VConsole.error("LLW Drawing child...");

        // Remove the placeholder
        removePlaceholder();

        getElement().setClassName(CLASSNAME);

        for (ComponentConnector childConnector : children) {
            add(childConnector.getWidget());
            childConnector.getLayoutManager().setNeedsMeasure(childConnector);
            childConnector.getLayoutManager().layoutNow();
        }
    }

    /**
     * Check that the placeholder visible and not obscured behind the scrollable
     * area of a panel for example.
     * 
     * @param child
     *            - the cild element to be checked for visibility
     * @return <b>true</b> = visible <br>
     *         <t/> <b>false</b> = obscured
     */
    public boolean isVisibleInsideParent() {

        Element tempElement = getElement();
        Element childElement = getElement();
        Element parent;
        while ((parent = tempElement.getOffsetParent()) != null) {

            // Check if parent is not scrollable or has overflow visible..
            if (!parent.getClassName().contains("v-scrollable")) {
                tempElement = parent;
                continue;
            }

            /* Vertical */
            /*
             * Check that the child is inside the vert. view area of the parent
             * if not, return visibility as false
             */
            int childTopPosY = childElement.getOffsetTop() - proximity;
            int childBottomPosY = childElement.getOffsetTop()
                    + childElement.getOffsetHeight() + proximity;

            int parentVisibleTop = parent.getScrollTop();
            int parentVisibleBottom = parentVisibleTop
                    + parent.getClientHeight();

            if (checkVerticalVisibility(childTopPosY, childBottomPosY,
                    parentVisibleTop, parentVisibleBottom) == false) {
                // Child is not visible vertically at all...
                return false;
            }

            // TODO refactor X-check to method for testability!

            /* Horizontal */
            /*
             * Check that the child is inside the horiz. view area of the parent
             * if not, return visibility as false
             */
            // NEG: child left < parent right && child right > parent left
            if (!((childElement.getOffsetLeft() - proximity) < parent
                    .getScrollLeft() + parent.getClientWidth())
                    && (childElement.getOffsetLeft()
                            + childElement.getOffsetWidth() + proximity > parent
                                .getScrollLeft())) {
                return false;
            }

            childElement = parent;
            tempElement = parent;
        }

        return true;

    }

    /**
     * 
     * TODO WRITE UNIT TESTS FOR THIS... It keeps breaking!
     * 
     * Checks if the child is visible vertically inside the parent.
     * 
     * @return true if visible, false otherwise
     */
    protected boolean checkVerticalVisibility(int childTopY, int childBottomY,
            int parentTopY, int parentBottomY) {
        if (childTopY < parentTopY && childBottomY > parentBottomY) {
            // Child is larger than parent visible area, but is currently
            // visible in parent.
            return true;
        }

        boolean childTopVisible = childTopY >= parentTopY
                && childTopY <= parentBottomY;

        boolean childBottomVisible = childBottomY <= parentBottomY
                && childBottomY >= parentTopY;

        if (childTopVisible || childBottomVisible) {
            return true;
        }

        return false;
    }

    public int getProximity() {
        return proximity;
    }

    public void setProximity(int proximity) {
        this.proximity = proximity;
    }

    public void ensurePlaceholderVisible() {
        if (placeholder.getParentElement() == null) {
            createPlaceholder();
        }

    }

}
