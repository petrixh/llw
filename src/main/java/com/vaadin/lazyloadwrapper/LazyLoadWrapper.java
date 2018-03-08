package com.vaadin.lazyloadwrapper;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;

import com.vaadin.lazyloadwrapper.widgetset.client.ui.LLWRpc;
import com.vaadin.lazyloadwrapper.widgetset.client.ui.LLWState;
import com.vaadin.lazyloadwrapper.widgetset.client.ui.LazyLoadWrapperConnector;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.SelectiveRenderer;

/**
 * Server side component for the VLazyLoadingWrapper widget.
 * 
 * A wrapper for loading Vaadin components lazily. The wrapper creates a
 * lightweight placeholder on the client side that has a spinner on it until the
 * user has scrolled the placeholder in to view at which point the wrapper will
 * draw the lazy load component. <br />
 * <br />
 * 
 * The component that is to be lazily loaded can be provided to the Lazy load
 * wrapper (LLW), through:
 * <ul>
 * <li>one of the constructors</li>
 * <li>the {@link LazyLoadWrapper#setLazyLoadComponent(Component)} -method</li>
 * <li>or the {@link LazyLoadComponentProvider#onComponentVisible()} -interface.
 * </li>
 * </ul>
 * If the lazy load component is has a defined width and/or height, the wrapper
 * will try to set the size of the placeholder to the size of the child
 * component automatically. If no sizing information is available, the wrapper
 * will set the (undefined size) for placeholder to a default value of of 100px <br />
 * <br />
 * The LLW has a proximity parameter that can be set through
 * {@link #setProximity(int)}. This works as a "fine tune" for the loading
 * event. The proximity is the offset from the visible area when the lazy load
 * component should be loaded from the server.<br />
 * * Positive numbers = before actually visible <br />
 * * Negative numbers = must be <i>X<i/> px visible <br />
 * * <b>Default: </b> 250px <br />
 * <br />
 * 
 * LLW can also be set to use a delay timer that defines a delay how long the
 * placeholder should be visible before the child component is drawn. <br>
 * * <b>Default: </b> 0 ms. <br>
 * <br>
 */

@SuppressWarnings({"serial", "javadoc"})
public class LazyLoadWrapper extends AbstractComponentContainer implements
        SelectiveRenderer {

    /**
     * Default mode: The lazy load wrapper client side will ask the server side
     * for the lazy load component when the placeholder has become visible
     */
    public static final int MODE_LAZY_LOAD_FETCH = LazyLoadWrapperConnector.MODE_LAZY_LOAD_FETCH;

    /**
     * @since 0.1 alpha - disabled due to changes in client-side rendering logic
     * 
     *        This mode sets configures the LLW to send all child component data
     *        to the client side upon initialization but render the child
     *        component lazily when it becomes visible. <br>
     * <br>
     *        <i><b>NOTE</b> This mode should only be used with components that
     *        do not need to be updated before they are actually drawn as
     *        updates will not propagate to the child component before it's
     *        actually drawn. </i>
     * 
     * 
     * 
     */
    public static final int MODE_LAZY_LOAD_DRAW = LazyLoadWrapperConnector.MODE_LAZY_LOAD_DRAW;

    private Component lazyloadComponent = null;

    /**
     * The instance of {@link LazyLoadComponentProvider} that will provide the
     * child component when it's needed (server side lazy load).
     */
    private LazyLoadComponentProvider childProvider = null;

    /**
     * Create extension/inline override to enable debug mode, for instance like
     * this:
     * 
     * <code><br/><br/>
     *  new LazyLoadWrapper(){ </br>
     *      { <br/>
     *          debug = true;<br/>
     *      }<br/>
     *  };<br/>
     *   
     * </code>
     * 
     * 
     */
    protected boolean debug = false;

    private LLWRpc serverRpc = new LLWRpc() {

        @Override
        public void onWidgetVisible() {

            // System.out.println("Server-side go widget visible call");
            setClientSideIsVisible(true);

        }
    };

    /*
     * CONSTRUCTORS FOR DEFAULT BEHAVIOR
     */

    /**
     * Create new Lazy load wrapper with default settings and no component.
     */
    public LazyLoadWrapper() {
        super();
        registerRpc(serverRpc);
    }

    /**
     * Create a new Lazy load wrapper with default settings and a lazy load
     * component <i>lazyloadComponent</i>.
     * 
     * @param lazyloadComponent
     *            - the component to be lazily loaded
     */
    public LazyLoadWrapper(Component lazyloadComponent) {
        this();
        setLazyLoadComponent(lazyloadComponent);

    }

    /**
     * Create a lazy load wrapper with a defined
     * {@link LazyLoadWrapper#proximity}
     * 
     * @param lazyloadComponent
     *            - the component to be loaded
     * @param proximity
     *            - the proximity in pixels from the viewable area when the
     *            component should be loaded
     */
    public LazyLoadWrapper(Component lazyloadComponent, int proximity) {
        this(lazyloadComponent);
        getState().proximity = proximity;

    }

    /**
     * Create a lazy load wrapper with a defined proximity and
     * placeholderVisibleDelay
     * 
     * @param lazyloadComponent
     *            - the component to be loaded
     * @param proximity
     *            - the proximity in pixels from the viewable area when the
     *            component should be loaded
     * @param placeholderVisibleDelay
     *            - the delay in <i>ms</i> how long the placeholder should be
     *            visible before the child component is loaded
     */
    public LazyLoadWrapper(Component lazyloadComponent, int proximity,
            int placeholderVisibleDelay) {

        this(lazyloadComponent, proximity);
        getState().placeholderVisibleDelay = placeholderVisibleDelay;
    }

    /**
     * Create a new lazy load wrapper with specified placeholder size that
     * resizes itself to fit the child components when they are loaded.
     * 
     * @param lazyloadComponent
     *            - the component to be loaded
     * @param placeHolderWidth
     *            - the width of the placeholder
     * @param placeHolderHeight
     *            - the height of the placeholder
     */
    public LazyLoadWrapper(Component lazyloadComponent,
            String placeHolderWidth, String placeHolderHeight) {
        this(lazyloadComponent);
        setPlaceHolderSize(placeHolderWidth, placeHolderHeight);

    }

    /**
     * Create a new lazy load wrapper with specified placeholder size that
     * does/does not resize itself when the child components are loaded.
     * 
     * @param lazyloadComponent
     *            - the component to be loaded
     * @param placeHolderWidth
     *            - the width of the placeholder
     * @param placeHolderHeight
     *            - the height of the placeholder
     * @param staticContainer
     *            - true if the placeholder should keep it size after that the
     *            components are loaded <br />
     *            false if the placeholder should auto resize to accommodate
     *            is's children.
     */
    public LazyLoadWrapper(Component lazyloadComponent,
            String placeHolderWidth, String placeHolderHeight,
            boolean staticContainer) {

        this(lazyloadComponent, placeHolderWidth, placeHolderHeight);
        setStaticConatiner(staticContainer);
    }

    /**
     * Create a new lazy load wrapper with specified proximity, visible delay
     * and placeholder size that does/does not resize itself when the child
     * components are loaded.
     * 
     * @param lazyloadComponent
     *            - the component to be loaded
     * @param proximity
     *            - the proximity in pixels from the viewable area when the
     *            component should be loaded
     * @param placeholderVisibleDelay
     *            - the delay in <i>ms</i> how long the placeholder should be
     *            visible before the child component is loaded
     * @param placeHolderWidth
     *            - the width of the placeholder
     * @param placeHolderHeight
     *            - the height of the placeholder
     * @param staticContainer
     *            - true if the placeholder should keep it size after that the
     *            components are loaded <br />
     *            false if the placeholder should auto resize to accommodate
     *            is's children.
     */
    public LazyLoadWrapper(Component lazyloadComponent, int proximity,
            int placeholderVisibleDelay, String placeHolderWidth,
            String placeHolderHeight, boolean staticContainer) {

        this(lazyloadComponent, proximity, placeholderVisibleDelay);
        setPlaceHolderSize(placeHolderWidth, placeHolderHeight);
        setStaticConatiner(staticContainer);

    }

    /* CONSTRUCTORS FOR MODE_LAZY_LOAD_DRAW */

    /**
     * Create a lazy load wrapper with a defined proximity,
     * placeholderVisibleDelay and mode.
     * 
     * @param lazyloadComponent
     *            - the component to be loaded
     * @param proximity
     *            - the proximity in pixels from the viewable area when the
     *            component should be loaded
     * @param placeholderVisibleDelay
     *            - the delay in <i>ms</i> how long the placeholder should be
     *            visible before the child component is loaded
     * @param mode
     *            - the mode for this lazy load wrapper <lu> <li>
     *            {@link #MODE_LAZY_LOAD_FETCH}</li> <li>
     *            {@link #MODE_LAZY_LOAD_DRAW}</li> </lu>
     */
    public LazyLoadWrapper(Component lazyloadComponent, int proximity,
            int placeholderVisibleDelay, int mode) {

        this(lazyloadComponent, proximity, placeholderVisibleDelay);
        setMode(mode);

    }

    /**
     * Create a new lazy load wrapper with specified placeholder size that
     * resizes itself to fit the child components when they are loaded.
     * 
     * @param lazyloadComponent
     *            - the component to be loaded
     * @param placeHolderWidth
     *            - the width of the placeholder
     * @param placeHolderHeight
     *            - the height of the placeholder
     * @param mode
     *            - the mode for this lazy load wrapper <lu> <li>
     *            {@link #MODE_LAZY_LOAD_FETCH}</li> <li>
     *            {@link #MODE_LAZY_LOAD_DRAW}</li> </lu>
     */
    public LazyLoadWrapper(Component lazyloadComponent,
            String placeHolderWidth, String placeHolderHeight, int mode) {

        this(lazyloadComponent, placeHolderWidth, placeHolderHeight);
        setMode(mode);

    }

    /**
     * Create a new lazy load wrapper with specified placeholder size that
     * does/does not resize itself when the child components are loaded.
     * 
     * @param lazyloadComponent
     *            - the component to be loaded
     * @param placeHolderWidth
     *            - the width of the placeholder
     * @param placeHolderHeight
     *            - the height of the placeholder
     * @param staticContainer
     *            - true if the placeholder should keep it size after that the
     *            components are loaded <br />
     *            false if the placeholder should auto resize to accommodate
     *            is's children.
     * @param mode
     *            - the mode for this lazy load wrapper <lu> <li>
     *            {@link #MODE_LAZY_LOAD_FETCH}</li> <li>
     *            {@link #MODE_LAZY_LOAD_DRAW}</li> </lu>
     */
    public LazyLoadWrapper(Component lazyloadComponent,
            String placeHolderWidth, String placeHolderHeight,
            boolean staticContainer, int mode) {

        this(lazyloadComponent, placeHolderWidth, placeHolderHeight,
                staticContainer);
        setMode(mode);
    }

    /**
     * Create a new lazy load wrapper with specified proximity, visible delay
     * and placeholder size that does/does not resize itself when the child
     * components are loaded.
     * 
     * @param lazyloadComponent
     *            - the component to be loaded
     * @param proximity
     *            - the proximity in pixels from the viewable area when the
     *            component should be loaded
     * @param placeholderVisibleDelay
     *            - the delay in <i>ms</i> how long the placeholder should be
     *            visible before the child component is loaded
     * @param placeHolderWidth
     *            - the width of the placeholder
     * @param placeHolderHeight
     *            - the height of the placeholder
     * @param staticContainer
     *            - true if the placeholder should keep it size after that the
     *            components are loaded <br />
     *            false if the placeholder should auto resize to accommodate
     *            is's children.
     * @param mode
     *            - the mode for this lazy load wrapper <lu> <li>
     *            {@link #MODE_LAZY_LOAD_FETCH}</li> <li>
     *            {@link #MODE_LAZY_LOAD_DRAW}</li> </lu>
     */
    public LazyLoadWrapper(Component lazyloadComponent, int proximity,
            int placeholderVisibleDelay, String placeHolderWidth,
            String placeHolderHeight, boolean staticContainer, int mode) {

        this(lazyloadComponent, proximity, placeholderVisibleDelay,
                placeHolderWidth, placeHolderHeight, staticContainer);
        setMode(mode);
    }

    /* CONSTRUCTORS FOR SERVER SIDE LAZY LOAD */

    /**
     * Create a lazy load wrapper with default settings and server side lazy
     * load.
     * 
     * @param childProvider
     *            - the instance of {@link LazyLoadComponentProvider} that will
     *            provide the <i>component</i> when it's needed.
     */
    public LazyLoadWrapper(LazyLoadComponentProvider childProvider) {
        this();
        this.childProvider = childProvider;
    }

    /**
     * Create a lazy load wrapper with a defined
     * {@link LazyLoadWrapper#proximity} and server side lazy load.
     * 
     * @param proximity
     *            - the proximity in pixels from the viewable area when the
     *            component should be loaded
     * @param childProvider
     *            - the instance of {@link LazyLoadComponentProvider} that will
     *            provide the <i>component</i> when it's needed.
     */
    public LazyLoadWrapper(int proximity,
            LazyLoadComponentProvider childProvider) {

        this(childProvider);
        getState().proximity = proximity;
    }

    /**
     * Create a lazy load wrapper with a defined proximity and
     * placeholderVisibleDelay and server side lazy load.
     * 
     * @param proximity
     *            - the proximity in pixels from the viewable area when the
     *            component should be loaded
     * @param placeholderVisibleDelay
     *            - the delay in <i>ms</i> how long the placeholder should be
     *            visible before the child component is loaded
     * 
     * @param childProvider
     *            - the instance of {@link LazyLoadComponentProvider} that will
     *            provide the <i>component</i> when it's needed.
     */
    public LazyLoadWrapper(int proximity, int placeholderVisibleDelay,
            LazyLoadComponentProvider childProvider) {

        this(proximity, childProvider);
        getState().placeholderVisibleDelay = placeholderVisibleDelay;

    }

    /**
     * Create a new lazy load wrapper with server side lazy load and with a
     * specified placeholder size that resizes itself to fit the child
     * components when they are loaded.
     * 
     * @param placeHolderWidth
     *            - the width of the placeholder
     * @param placeHolderHeight
     *            - the height of the placeholder
     * @param childProvider
     *            - the instance of {@link LazyLoadComponentProvider} that will
     *            provide the <i>component</i> when it's needed.
     */
    public LazyLoadWrapper(String placeHolderWidth, String placeHolderHeight,
            LazyLoadComponentProvider childProvider) {

        this(childProvider);
        setPlaceHolderSize(placeHolderWidth, placeHolderHeight);
    }

    /**
     * Create a new lazy load wrapper with server side lazy load and with a
     * specified placeholder size that does/does not resize itself when the
     * child components are loaded.
     * 
     * @param placeHolderWidth
     *            - the width of the placeholder
     * @param placeHolderHeight
     *            - the height of the placeholder
     * @param staticContainer
     *            - true if the placeholder should keep it size after that the
     *            components are loaded <br />
     *            false if the placeholder should auto resize to accommodate
     *            is's children.
     * @param childProvider
     *            - the instance of {@link LazyLoadComponentProvider} that will
     *            provide the <i>component</i> when it's needed.
     */
    public LazyLoadWrapper(String placeHolderWidth, String placeHolderHeight,
            boolean staticContainer, LazyLoadComponentProvider childProvider) {

        this(placeHolderWidth, placeHolderHeight, childProvider);
        setStaticConatiner(staticContainer);
    }

    /**
     * Create a new lazy load wrapper with server side lazy load, defined
     * proximity, placeholderVisibleDelay and placeholder size that does/does
     * not resize itself when the child components are loaded.
     * 
     * @param proximity
     *            - the proximity in pixels from the viewable area when the
     *            component should be loaded
     * @param placeholderVisibleDelay
     *            - the delay in <i>ms</i> how long the placeholder should be
     *            visible before the child component is loaded
     * @param placeHolderWidth
     *            - the width of the placeholder
     * @param placeHolderHeight
     *            - the height of the placeholder
     * @param staticContainer
     *            - true if the placeholder should keep it size after that the
     *            components are loaded <br />
     *            false if the placeholder should auto resize to accommodate
     *            is's children.
     * @param childProvider
     *            - the instance of {@link LazyLoadComponentProvider} that will
     *            provide the <i>component</i> when it's needed.
     */
    public LazyLoadWrapper(int proximity, int placeholderVisibleDelay,
            String placeHolderWidth, String placeHolderHeight,
            boolean staticContainer, LazyLoadComponentProvider childProvider) {

        this(placeHolderWidth, placeHolderHeight, staticContainer,
                childProvider);

        getState().proximity = proximity;
        getState().placeholderVisibleDelay = placeholderVisibleDelay;
    }

    /*
     * Methods
     */

    /**
     * @deprecated Use {@link #setLazyLoadComponent(Component)} instead.
     */
    @Override
    @Deprecated
    public void addComponent(Component component) {
        throw new UnsupportedOperationException();
    }

    /*
     * 
     * Getters and setters
     */

    /**
     * Set the component that is to be loaded lazily when the placeholder is
     * visible.
     * 
     * @param lazyloadComponent
     *            - component to be loaded
     */
    public void setLazyLoadComponent(Component lazyloadComponent) {
        removeAllComponents();
        this.lazyloadComponent = lazyloadComponent;

        if (lazyloadComponent.getWidthUnits() != Unit.PERCENTAGE
                && lazyloadComponent.getWidth() != -1.0) {

            getState().placeholderWidth = lazyloadComponent.getWidth()
                    + lazyloadComponent.getWidthUnits().getSymbol();
        } else {
            setWidth(lazyloadComponent.getWidth(),
                    lazyloadComponent.getWidthUnits());
        }

        if (lazyloadComponent.getHeightUnits() != Unit.PERCENTAGE
                && lazyloadComponent.getHeight() != -1.0) {
            getState().placeholderHeight = lazyloadComponent.getHeight()
                    + lazyloadComponent.getHeightUnits().getSymbol();
        } else {
            setHeight(lazyloadComponent.getHeight(),
                    lazyloadComponent.getHeightUnits());
        }

        if (getState().mode == MODE_LAZY_LOAD_DRAW) {
            super.addComponent(lazyloadComponent);
        }
    }

    /**
     * Get the current lazy load component. If no component is specified null is
     * returned.
     * 
     * @return - the current lazy load component or null
     */
    public Component getLazyLoadComponent() {
        return lazyloadComponent;
    }

    /**
     * Set the size of the place holder that will be shown on the client-side.
     * 
     * @param width
     * @param height
     */
    public void setPlaceHolderSize(String width, String height) {
        getState().placeholderHeight = height;
        getState().placeholderWidth = width;

        if (getState().staticContainer) {
            setWidth(width);
            setHeight(height);
        } else {
            setSizeUndefined();
        }

    }

    /**
     * Get the delay that the placeholder is visible on client side before the
     * lazy load component is actually loaded.
     * 
     * @return placeholderVisibleDelay - the delay in ms
     */
    public int getPlaceholderVisibleDelay() {
        return getState().placeholderVisibleDelay;
    }

    /**
     * Set the delay how long the placeholder should be visible before the
     * component is loaded from the server to the client.
     * 
     * @param placeholderVisibleDelay
     *            - the delay in ms
     */
    public void setPlaceholderVisibleDelay(int placeholderVisibleDelay) {
        getState().placeholderVisibleDelay = placeholderVisibleDelay;
    }

    /**
     * Set the proximity (in pixels) measured from the viewable area when the
     * client should ask the server for the lazy load component.
     * 
     * @param proximity
     *            - the proximity in pixels<br />
     * <br />
     *            Positive if component should be loaded before it's actually
     *            visible<br />
     *            Negative if component should be <i> X </i> pixels visible
     *            before it's loaded.
     */
    public void setProximity(int proximity) {
        getState().proximity = proximity;
    }

    /**
     * Get the current proximity (in pixels) for loading the component from
     * server
     */
    public int getProximity() {
        return getState().proximity;
    }

    /**
     * Set the mode of operation for the lazy load wrapper. <i>
     * {@link #MODE_LAZY_LOAD_FETCH} </i> or <i> {@link #MODE_LAZY_LOAD_DRAW}
     * </i><br />
     * 
     * @param mode
     * 
     */
    public void setMode(int mode) {
        if (mode == MODE_LAZY_LOAD_DRAW || mode == MODE_LAZY_LOAD_FETCH) {
            getState().mode = mode;

            if (mode == MODE_LAZY_LOAD_DRAW) {
                setClientSideIsVisible(true);
            }

        }
    }

    /**
     * Returns the current mode.
     */
    public int getMode() {
        return getState().mode;
    }

    /**
     * Sets the static container. If static container is set to true, the
     * container will keep it's size when the lazy load component is loaded. <br/>
     * <br/>
     * If static container is set to false, the wrapper will try to
     * expand/shrink to fit it's child component.
     * 
     * @param staticContainer
     */
    public void setStaticConatiner(boolean staticContainer) {
        getState().staticContainer = staticContainer;
        if (staticContainer) {
            setWidth(getState().placeholderWidth);
            setHeight(getState().placeholderHeight);
        } else {
            setSizeUndefined();
        }

    }

    /**
     * Get the current container mode.
     */
    public boolean getStaticContainer() {
        return getState().staticContainer;
    }

    /**
     * Set the isVisible parameter for the wrapper. When called with <i>true</i>
     * this will show the wrapped component immediately on the client side. <br>
     * <br>
     * <i> Note: not the same thing as Vaadins isVisible()</i><br>
     * 
     * 
     * @param visible
     * <br>
     *            - true: show wrapped component immediately <br>
     *            - false: show child component when scrolled into view.
     */
    public void setClientSideIsVisible(boolean visible) {
        getState().clientSideIsVisible = visible;

        if (getState().clientSideIsVisible) {
            if (childProvider != null) {
                lazyloadComponent = childProvider.onComponentVisible();
            }
            showLoadedComponent(lazyloadComponent);

        } else {
            hideLoadedComponent(lazyloadComponent);
        }

    }

    protected void showLoadedComponent(@SuppressWarnings("hiding") Component lazyloadComponent) {
        // Attach child to container.
        if (lazyloadComponent != null) {
            super.addComponent(lazyloadComponent);
            lazyloadComponent.markAsDirty();
        }
    }

    protected void hideLoadedComponent(@SuppressWarnings("hiding") Component lazyloadComponent) {
        if (lazyloadComponent != null
                && lazyloadComponent.getParent() != null
                && lazyloadComponent.getParent().equals(this)) {
            super.removeComponent(lazyloadComponent);
        }
    }

    /**
     * Get the client side isVisible parameter. If mode is set to
     * {@link #MODE_LAZY_LOAD_DRAW} then true is returned even if the lazy load
     * component is not drawn on the client side. <br>
     * <br>
     * <i> Note: not the same thing as Vaadins isVisible()</i><br>
     * 
     * @return true/false - client side visible/wrapped
     * 
     * 
     */
    public boolean getClientSideIsVisible() {
        return getState().clientSideIsVisible;
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    // replaced by get iterator
    @Override
    public Iterator<Component> getComponentIterator() {
        // System.out.println("Iterating thrhough components on server");
        Iterator<Component> iterator = new Iterator<Component>() {

            private boolean first = lazyloadComponent == null;

            @Override
	    public boolean hasNext() {

                return !first;
            }

            @Override
	    public Component next() {
                if (!first) {
                    // System.out.println("Returning lazy load component");
                    first = true;
                    return lazyloadComponent;
                } else {
                    // System.out.println("Retruning null component");
                    return null;
                }
            }

            @Override
	    public void remove() {
                throw new UnsupportedOperationException();

            }

        };

        return iterator;
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        throw new UnsupportedOperationException();

    }

    /*
     * 
     * Server side lazy load...
     */

    // public void setAutoReinitLazyLoad(boolean autoReinitLazyLoad) {
    // this.autoReinitLazyLoad = autoReinitLazyLoad;
    // }
    //
    // public boolean isAutoReinitLazyLoad() {
    // return autoReinitLazyLoad;
    // }

    /**
     * The listener interface for implementing server side lazy load. If no
     * child component is specified for the lazy load wrapper, the wrapper will
     * try to retrieve one using this interface when the placeholder on the
     * client side is visible.
     * 
     */
    public interface LazyLoadComponentProvider extends Serializable {

        /**
         * Called when the placeholder component has become visible
         */
        public Component onComponentVisible();

    }

    @Override
    public int getComponentCount() {
        return lazyloadComponent != null ? 1 : 0;
    }

    @Override
    public LLWState getState() {
        // TODO Auto-generated method stub
        return (LLWState) super.getState();
    }

    @Override
    public Iterator<Component> iterator() {

        if (!getState().clientSideIsVisible && getState().mode != MODE_LAZY_LOAD_DRAW) {
            return Collections.emptyIterator();
        }
        return getComponentIterator();
    }

    @Override
    public boolean isRendered(Component childComponent) {
        if (getState().clientSideIsVisible
                || getState().mode == MODE_LAZY_LOAD_DRAW) {
            return true;
        }

        return false;
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        getState().debug = debug;
    }
}
