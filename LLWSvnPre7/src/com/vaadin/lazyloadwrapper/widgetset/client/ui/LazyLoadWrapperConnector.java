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
        // TODO Auto-generated method stub
        return (LLWState) super.getState();
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        // super.onConnectorHierarchyChange(event);
        // TODO do sth here maby.. .
        updateToThisLLW();

    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        LLWState state2 = getState();

        VConsole.log("New state for LLW");
        VConsole.log("Mode: " + state2.mode);
        VConsole.log("Placeholder visible delay: "
                + state2.placeholderVisibleDelay);

        getWidget().setPlaceHolderSize(getState().placeholderHeight,
                getState().placeholderWidth);

        if (state2.staticContainer) {
            getWidget().setHeight(state2.placeholderHeight);
            getWidget().setWidth(state2.placeholderWidth);
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
        if (getChildren().size() > 0 && getWidget().isAttached()) {

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

            if (getState().mode != MODE_LAZY_LOAD_DRAW) {
                getWidget().lateDrawChild(getChildComponents());
            }
        }

    }

    /**
     * Called when we have determined that the wrapper is visible
     */
    private void widgetIsVible() {
        VConsole.log("In WIDGETISVISIBLE");

        if (!getWidget().isAttached()) {

            VConsole.log("The wrapper with PID: "
                    + getConnectorId()
                    + " is no longer attached to the DOM, ignoring paint of child component... ");
            return;
        }

        if (getState().mode == MODE_LAZY_LOAD_DRAW) {
            getWidget().lateDrawChild(getChildComponents());
        } else {
            // Inform the server that the component is visible...
            rpc.onWidgetVisible();
        }
    }

    public void checkVisibility() {

        VConsole.log("LLW checking visibilty");

        if (getWidget().isVisibleInsideParent()) {
            visibilityPollingTimer.removeLLW(this);

            if (getState().placeholderVisibleDelay == 0) {
                widgetIsVible();

            } else {
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
