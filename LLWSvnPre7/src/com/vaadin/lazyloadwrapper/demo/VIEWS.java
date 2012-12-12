package com.vaadin.lazyloadwrapper.demo;

import com.vaadin.lazyloadwrapper.demo.views.InformationView;
import com.vaadin.lazyloadwrapper.demo.views.PhotoArchive;
import com.vaadin.lazyloadwrapper.demo.views.ServerSideForceVisible;

public enum VIEWS {

    INFO("Home", InformationView.class), PHOTO_ARCHIVE("Photo archive",
            PhotoArchive.class), SS_SHOW_COMPONENT("Server-side visible",
            ServerSideForceVisible.class);

    private String name;
    private Class<? extends ViewBase> clazz;

    private VIEWS(String name, Class<? extends ViewBase> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public String getInternalName() {
        if (INFO.equals(this)) {
            return "";
        }

        return name.replaceAll(" ", "").toLowerCase();
    }

    public Class<? extends ViewBase> getClazz() {
        return clazz;
    }

}
