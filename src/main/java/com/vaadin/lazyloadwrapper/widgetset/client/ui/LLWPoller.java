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
        // long start = System.currentTimeMillis();
        for (LazyLoadWrapperConnector llw : new ArrayList<LazyLoadWrapperConnector>(
                listeners)) {
            llw.checkVisibility();
        }

        // long stop = System.currentTimeMillis();
        // VConsole.log("Checking LLW visibility for all LLW:s took: "
        // + (stop - start) + " ms");
    }

    /**
     * Register a lazy load wrapper to the master poller
     * 
     * @param llw
     *            - the LLW instance to be registered
     */
    public synchronized void addLLW(LazyLoadWrapperConnector llw) {

        if (!listeners.contains(llw)) {
            listeners.add(llw);
        }
        if (listeners.size() == 1) {
            scheduleRepeating(1250);
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