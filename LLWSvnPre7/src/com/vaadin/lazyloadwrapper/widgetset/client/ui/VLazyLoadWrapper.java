package com.vaadin.lazyloadwrapper.widgetset.client.ui;

import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Paintable;
import com.vaadin.client.RenderSpace;
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

    public interface LLWInterface extends
            com.vaadin.shared.communication.ServerRpc {

    }

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
    private int visibleDelay = 0;

    // private Paintable lazyLoadPaintableComponent;
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

        /* Set the style name (spinner) to the placeholder */
        setStylePrimaryName(LOADING_CLASSNAME);
        placeholder = DOM.createDiv();
        getElement().appendChild(placeholder);
    }

    // private boolean checkForNeedOfAutomaticReinitOnReattach(UIDL uidl) {
    // if (lazyLoadPaintableComponent == null
    // && uidl.getBooleanAttribute(WIDGET_VISIBLE_ID)
    // && recentlyAttached) {
    //
    // VConsole.error("Found that we should reinit the wrapper... ");
    // recentlyAttached = false;
    // if (uidl.hasAttribute(WRAPPER_AUTOREINIT_ON_REATTACH)
    // && uidl.getBooleanAttribute(WRAPPER_AUTOREINIT_ON_REATTACH)) {
    // client.updateVariable(wrappersPaintableId, WIDGET_VISIBLE_ID,
    // false, true);
    // return true;
    // }
    //
    // }
    // return false;
    // }

    // /**
    // * Process the variable updates from the server and set the local
    // variables.
    // *
    // * @param uidl
    // * - the new UIDL instance
    // */
    // private void processVariableUpdatesFromServer(UIDL uidl) {
    // proximity = uidl.getIntAttribute(WIDGET_LOAD_PROXIMITY);
    // visibleDelay = uidl.getIntAttribute(WIDGET_LOAD_VISIBLE_DELAY);
    // staticContainer = uidl.getBooleanAttribute(STATIC_CONTAINER);
    // mode = uidl.getIntAttribute(WRAPPER_MODE);
    //
    // // Set the placeholder to size
    // DOM.setStyleAttribute((com.google.gwt.user.client.Element) placeholder,
    // "width", uidl.getStringAttribute(PLACEHOLDER_WIDTH));
    // DOM.setStyleAttribute((com.google.gwt.user.client.Element) placeholder,
    // "height", uidl.getStringAttribute(PLACEHOLDER_HEIGHT));
    //
    // }

    // /**
    // * Called when the master poller fires. This method tries to determine if
    // * the widget is visible or not, if the wrapper has a defined delay
    // * {@link #visibleDelay} that the placeholder should be visible before
    // it's
    // * defined as visible, this is where that delay is checked. <br>
    // * <br>
    // * If the placeholder is still visible after <i>delay</i> ms the client
    // * either calls the server to request the lazy load component or calls
    // * {@link #lateDrawChild()} to load the child (if in LAZY_LOAD_DRAW
    // -mode).
    // *
    // * see {@link LazyLoadWrapper#MODE_LAZY_LOAD_DRAW} for more information
    // * about the MODE_LAZY_LOAD_DRAW -mode.
    // *
    // */
    // public void checkVisibility() {
    //
    // if (isVisibleInsideParent()) {
    // visibilityPollingTimer.removeLLW(this);
    //
    // if (visibleDelay == 0) {
    // widgetIsVible();
    //
    // } else {
    // if (visibleDelayTimer == null) {
    // createVisibleDelayTimer();
    // }
    //
    // visibleDelayTimer.schedule(visibleDelay);
    // }
    //
    // }
    //
    // }

    protected void removePlaceholder() {
        if (placeholder != null) {
            getElement().removeChild(placeholder);
            placeholder = null;
        }
    }

    protected void setPlaceHolderSize(String height, String width) {
        placeholder.getStyle().setProperty("height", height);
        placeholder.getStyle().setProperty("width", width);

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
    protected boolean isVisibleInsideParent() {

        Element childElement = getElement();
        Element parent;
        while ((parent = childElement.getOffsetParent()) != null) {

            // Check if parent is not scrollable or has overflow visible..
            if (parent.getStyle().getOverflow().equalsIgnoreCase("visible")
                    || parent.getStyle().getOverflow().equalsIgnoreCase("")) {
                childElement = parent;
                continue;
            }

            /* Vertical */
            /*
             * Check that the child is inside the vert. view area of the parent
             * if not, return visibility as false
             */
            // NEG: child top < parent view area bottom && child bottom > parent
            // view area top
            if (!(childElement.getOffsetTop() - proximity < parent
                    .getClientHeight() + parent.getScrollTop())
                    && (childElement.getOffsetTop()
                            + childElement.getOffsetHeight() + proximity > parent
                                .getScrollTop())) {
                return false;
            }

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
        }

        return true;

    }

    /**
     * When we are using the "MODE_LAZY_LOAD_DRAW" (all children already on
     * client side) this is where the child component is actually painted.
     */
    protected void lateDrawChild() {

        VConsole.error("LLW Drawing child...");
        // Remove the placeholder
        removePlaceholder();

        getElement().setClassName(CLASSNAME);

        // client.getPaintable(childUIDL).updateFromUIDL(childUIDL, client);
        //
        // // Tell the parent container that our size has updated
        // Set<Paintable> pa = new HashSet<Paintable>();
        // pa.add(this);
        // Util.getLayout(this).requestLayout(pa);
    }

    /*
     * Container methods
     */
    public RenderSpace getAllocatedSpace(Widget child) {

        // if (staticContainer) {
        return new RenderSpace(getOffsetWidth(), getOffsetHeight());
        // }
        // } else {
        //
        // RenderSpace llwRS = Util.getLayout(this).getAllocatedSpace(this);
        // RenderSpace rs = new RenderSpace(llwRS.getWidth(),
        // llwRS.getHeight());
        //
        // return rs;
        //
        // }

    }

    public boolean hasChildComponent(Widget component) {

        if (getWidget() == component) {
            return true;
        }

        return false;

    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        setWidget(newComponent);
    }

    public boolean requestLayout(Set<Paintable> children) {
        if (staticContainer) {
            return true;
        }

        if (getElement().getStyle().getHeight().equalsIgnoreCase("")
                || getElement().getStyle().getWidth().equalsIgnoreCase("")) {
            return false;
        }

        return true;
    }

    public void lateDrawChild(List<ComponentConnector> children) {
        lateDrawChild();
        for (ComponentConnector childConnector : children) {
            add(childConnector.getWidget());
        }

    }

    // public void updateCaption(Paintable component, UIDL uidl) {
    // if (VCaption.isNeeded(uidl)) {
    // if (captionWrapper != null) {
    // captionWrapper.updateCaption(uidl);
    // } else {
    // captionWrapper = new VCaptionWrapper(component, client);
    // setWidget(captionWrapper);
    // captionWrapper.updateCaption(uidl);
    // }
    // } else {
    // if (captionWrapper != null) {
    // setWidget((Widget) lazyLoadPaintableComponent);
    // }
    // }
    // }

}
