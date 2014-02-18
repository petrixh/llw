package com.vaadin.lazyloadwrapper.widgetset.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.lazyloadwrapper.widgetset.client.ui.gwt.VLazyLoadWrapper;
import com.vaadin.shared.ui.Connect;

@Connect(LazyLoadWrapper.class)
public class LazyLoadWrapperConnector extends
        AbstractComponentContainerConnector {

    private Object wrappersPaintableId;
    private boolean recentlyAttached;
    private Timer visibleDelayTimer;

    public static final int MODE_LAZY_LOAD_FETCH = 1;
    public static final int MODE_LAZY_LOAD_DRAW = 2;

    /** Polling timer used to check for visibility */
    protected static LLWPoller visibilityPollingTimer;

    LLWRpc rpc = RpcProxy.create(LLWRpc.class, this);

    @Override
    protected Widget createWidget() {
        if (visibilityPollingTimer == null) {
            visibilityPollingTimer = new LLWPoller();
        }

        Widget widget = GWT.create(VLazyLoadWrapper.class);

        // Add this LLW to the poller's list so that we receive poller updates
        visibilityPollingTimer.addLLW(this);

        return widget;
    }

    @Override
    public VLazyLoadWrapper getWidget() {
        return (VLazyLoadWrapper) super.getWidget();
    }

    @Override
    public LLWState getState() {
        return (LLWState) super.getState();
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {

        // TODO refactor me!
        updateToThisLLW();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        // Don't delegate these to widget as we must keep track of the order in
        // which they are set.
        getWidget().setPlaceHolderSize(getState().placeholderHeight,
                getState().placeholderWidth);

        // Don't delegate these to widget as we must keep track of the order in
        // which they are set.
        if (getState().staticContainer) {
            getWidget().setHeight(getState().placeholderHeight);
            getWidget().setWidth(getState().placeholderWidth);
        }

        if (stateChangeEvent.hasPropertyChanged("clientSideIsVisible")) {
            if (getState().clientSideIsVisible == false) {
                if (getState().debug) {
                    VConsole.log("ClientSideIsVisible set to false, readding LLW to timer...");
                }
                visibilityPollingTimer.addLLW(this);
            } else {
                if (getState().debug) {
                    VConsole.log("ClientSideIsVisible set to true, removing LLW to timer...");
                    visibilityPollingTimer.removeLLW(this);
                }
            }
        }
    }

    /**
     * Process the update from the server.
     * 
     * @param uidl
     * @param client
     */
    private void updateToThisLLW() {

        // if (checkForNeedOfAutomaticReinitOnReattach(uidl)) {
        // return;
        // }
        recentlyAttached = false;

        // processVariableUpdatesFromServer(uidl);

        // VConsole.log("LLW:" + wrappersPaintableId + " uidl has "
        // + uidl.getChildCount()
        // + " children. Parent attachment status: " + isAttached());

        /*
         * If UIDL has child we should paint it inside the placeholder or
         * configure for MODE_LAZY_LOAD_DRAW
         */
        if (getWidget().isAttached()) {

            // if (mode == MODE_LAZY_LOAD_DRAW) {
            // initializeLazyLoadDrawMode(uidl, client);
            // } else {
            // visibilityPollingTimer.removeLLW(this);
            // drawChildFromUIDL(uidl, client);
            // }
            // for (ComponentConnector componentConnector :
            // getChildComponents()) {
            // if (componentConnector instanceof ManagedLayout) {
            // getLayoutManager().setNeedsLayout(
            // (ManagedLayout) componentConnector);
            // }
            // }

            if (getState().clientSideIsVisible && getChildren().size() > 0
                    && getState().mode != MODE_LAZY_LOAD_DRAW) {
                ComponentConnector childConnector = getChildComponents().get(0);
                getWidget().lateDrawChild(childConnector.getWidget());

                getLayoutManager().setNeedsMeasure(childConnector);
                getLayoutManager().setNeedsMeasure(this);

                // Must use layout later due to some components like Table...
                getLayoutManager().layoutLater();

            } else {
                getWidget().ensurePlaceholderVisible();
            }
        }

    }

    /**
     * Called when we have determined that the wrapper is visible
     */
    private void widgetIsVible() {
        if (getState().debug) {
            VConsole.log("In WIDGETISVISIBLE");
        }

        if (!getWidget().isAttached()) {

            VConsole.log("The wrapper with ID: "
                    + getConnectorId()
                    + " is no longer attached to the DOM, ignoring draw of child component... ");
            return;
        }

        if (getState().mode == MODE_LAZY_LOAD_DRAW) {
            if (getChildComponents().size() > 1) {
                VConsole.error("LLW only supports one child component!");
                throw new RuntimeException(
                        "Only one lazy load component can be attached to a LLW!");
            }
            if (getChildComponents().size() != 0) {
                ComponentConnector childConnector = getChildComponents().get(0);

                getWidget().lateDrawChild(childConnector.getWidget());
                getLayoutManager().setNeedsMeasure(childConnector);
                getLayoutManager().setNeedsMeasure(this);
                getLayoutManager().layoutLater();
            }
        } else {
            // Inform the server that the component is visible...
            rpc.onWidgetVisible();
        }
    }

    public void checkVisibility() {

        // VConsole.log("LLW checking visibilty");

        if (getWidget().isVisibleInsideParent()) {
            visibilityPollingTimer.removeLLW(this);
            if (getState().debug) {
                VConsole.log("LLW has determined itself visible...");
            }

            if (getState().placeholderVisibleDelay == 0) {
                widgetIsVible();

            } else {
                if (getState().debug) {
                    VConsole.log("Starting visible delay timer...");
                }
                if (visibleDelayTimer == null) {
                    createVisibleDelayTimer();
                }

                visibleDelayTimer.schedule(getState().placeholderVisibleDelay);
            }
        }
    }

    /**
     * Creates the timer that is used when visibleDelay is defined.
     */
    private void createVisibleDelayTimer() {
        visibleDelayTimer = new Timer() {

            @Override
            public void run() {
                if (getWidget().isVisibleInsideParent()) {
                    widgetIsVible();
                } else {
                    visibilityPollingTimer
                            .addLLW(LazyLoadWrapperConnector.this);
                }
            }

        };

    }
}
