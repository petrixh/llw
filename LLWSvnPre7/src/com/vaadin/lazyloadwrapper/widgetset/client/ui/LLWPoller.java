package com.vaadin.lazyloadwrapper.widgetset.client.ui;

import java.util.ArrayList;

import com.google.gwt.user.client.Timer;

/**
 * The static poller that's shared with all LLW:s in an application. When the
 * poller is triggered, all LLW instances will be called to check their
 * visibility.
 */
public class LLWPoller extends Timer {

    ArrayList<LazyLoadWrapperConnector> listeners = new ArrayList<LazyLoadWrapperConnector>();

    @Override
    public void run() {
        // VLazyLoadWrapper[] currListeners = new VLazyLoadWrapper[1];
        // currListeners = listeners.toArray(currListeners);
        for (LazyLoadWrapperConnector llw : new ArrayList<LazyLoadWrapperConnector>(
                listeners)) {
            llw.checkVisibility();
        }

    }

    /**
     * Register a lazy load wrapper to the master poller
     * 
     * @param llw
     *            - the LLW instance to be registered
     */
    public synchronized void addLLW(LazyLoadWrapperConnector llw) {

        listeners.add(llw);
        if (listeners.size() == 1) {
            scheduleRepeating(250);
        }
    }

    /**
     * Remove a llw from the master poller.
     * 
     * @param llw
     *            - the instance of the llw to be removed.
     */
    public synchronized void removeLLW(LazyLoadWrapperConnector llw) {
        listeners.remove(llw);
        if (listeners.isEmpty()) {
            cancel();
        }
    }
}