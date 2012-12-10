package com.vaadin.lazyloadwrapper.widgetset.client.ui;

import com.vaadin.shared.communication.ServerRpc;

public interface LLWRpc extends ServerRpc {

    public void onWidgetVisible();

}
