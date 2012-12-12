package com.vaadin.lazyloadwrapper.demo;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.vaadin.lazyloadwrapper.LazyLoadWrapper;

public class DemoParams {

    boolean usingWrappers = true;
    int heavinessFactor = 0;
    int proximity = 0;
    int visibleDelay = 250;
    int mode = LazyLoadWrapper.MODE_LAZY_LOAD_FETCH;
    String placeholderWidth = null;
    String placeholderHeight = null;
    boolean staticContainer = false;

    public void parseFromParams(String parameters) {
        HashMap<String, String> params = new HashMap<String, String>();

        if (parameters == null) {
            return;
        }

        for (String parameter : parameters.split("/")) {
            String[] keyValue = parameter.split("=");
            if (keyValue.length > 1) {
                params.put(keyValue[0], keyValue[1]);
            }
        }

        parseFromParams(params);
    }

    public void parseFromParams(HashMap<String, String> params) {

        Field[] declaredFields = getClass().getDeclaredFields();

        for (Field field : declaredFields) {
            if (params.containsKey(field.getName())) {
                setFieldValue(params, field);
            }
        }
    }

    private void setFieldValue(HashMap<String, String> params, Field field) {
        field.setAccessible(true);
        try {
            if (boolean.class == field.getType()) {
                if ("true".equals(params.get(field.getName()))) {
                    field.setBoolean(this, true);
                } else {
                    field.setBoolean(this, false);
                }
            }

            if (int.class == field.getType()) {
                int value = Integer.parseInt(params.get(field.getName()));
                field.setInt(this, value);
            }

            if (String.class == field.getType()) {
                Object value = params.get(field.getName());
                if (value != null && "null".equals(value)) {
                    value = null;
                }

                field.set(this, value);
            }

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        field.setAccessible(false);
    }

    public String toParams() {
        StringBuilder sb = new StringBuilder();
        Field[] declaredFields = getClass().getDeclaredFields();

        for (Field decField : declaredFields) {
            decField.setAccessible(true);
            try {
                sb.append("/");

                sb.append(decField.getName()).append("=")
                        .append(decField.get(this));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            decField.setAccessible(false);
        }

        return sb.toString();
    }

    public boolean isUsingWrappers() {
        return usingWrappers;
    }

    public void setUsingWrappers(boolean usingWrappers) {
        this.usingWrappers = usingWrappers;
    }

    public int getHeavinessFactor() {
        return heavinessFactor;
    }

    public void setHeavinessFactor(int heavinessFactor) {
        this.heavinessFactor = heavinessFactor;
    }

    public int getProximity() {
        return proximity;
    }

    public void setProximity(int proximity) {
        this.proximity = proximity;
    }

    public int getVisibleDelay() {
        return visibleDelay;
    }

    public void setVisibleDelay(int visibleDelay) {
        this.visibleDelay = visibleDelay;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getPlaceholderWidth() {
        return placeholderWidth;
    }

    public void setPlaceholderWidth(String placeholderWidth) {
        this.placeholderWidth = placeholderWidth;
    }

    public String getPlaceholderHeight() {
        return placeholderHeight;
    }

    public void setPlaceholderHeight(String placeholderHeight) {
        this.placeholderHeight = placeholderHeight;
    }

    public boolean isStaticContainer() {
        return staticContainer;
    }

    public void setStaticContainer(boolean staticContainer) {
        this.staticContainer = staticContainer;
    }
}
