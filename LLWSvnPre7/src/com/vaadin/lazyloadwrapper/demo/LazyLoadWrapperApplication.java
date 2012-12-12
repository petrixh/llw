package com.vaadin.lazyloadwrapper.demo;

import java.io.Serializable;
import java.util.Locale;

import com.vaadin.annotations.Widgetset;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

@Widgetset("com.vaadin.lazyloadwrapper.widgetset.LazyLoadWrapperWidgetset")
public class LazyLoadWrapperApplication extends UI implements Serializable {

    private static final long serialVersionUID = -3214543723791435116L;
    Window mainWindow;
    private Navigator navigator;
    private VerticalLayout contentLayout;
    private HorizontalSplitPanel splitPanel;
    private LLWFetaureForm llwFeatures;
    private FieldGroup fieldGroup;
    private Tree featureTree;

    @Override
    protected void init(VaadinRequest request) {

        splitPanel = new HorizontalSplitPanel();
        splitPanel.setSplitPosition(250, Unit.PIXELS);
        splitPanel.setLocked(true);

        setContent(splitPanel);
        setSizeFull();
        splitPanel.setSizeFull();

        String fragment = getPage().getUriFragment();
        System.out.println("Fragment: " + fragment);
        if (fragment != null && fragment.contains("!")
                && fragment.contains("/")) {
            fragment = fragment.substring(fragment.indexOf("/"));
        }

        createFeaturesList(fragment);

        contentLayout = new VerticalLayout();
        contentLayout.setMargin(true);
        splitPanel.setSecondComponent(contentLayout);

        navigator = new Navigator(this, contentLayout);

        for (VIEWS view : VIEWS.values()) {
            navigator.addView(view.getInternalName(), view.getClazz());
        }

        // VerticalLayout vl = new VerticalLayout();
        // vl.setMargin(true);
        // vl.setSpacing(false);
        // setContent(vl);

        // vl.addComponent(new Label("Hello!"));
        // LazyLoadWrapper llw = new LazyLoadWrapper();
        // llw.setPlaceHolderSize("150px", "150px");
        // vl.addComponent(llw);
        // llw.setPlaceholderVisibleDelay(2250);
        //
        // Panel panel = new Panel();
        // VerticalLayout panelVl = new VerticalLayout();
        // panel.setContent(panelVl);
        // panelVl.addComponent(new Label("Lazily loaded content"));
        // panelVl.addComponent(new InlineDateField());
        //
        // llw.setLazyLoadComponent(panel);
        //
        // Panel vl2 = new Panel();
        // VerticalLayout panelVl2 = new VerticalLayout();
        // vl2.setContent(panelVl2);
        //
        // panelVl2.addComponent(new Label(
        // "Lazily loaded content should look like this!"));
        // panelVl2.addComponent(new InlineDateField());
        //
        // vl.addComponent(vl2);
        //
        // LazyLoadWrapperDemo llwDemo = new LazyLoadWrapperDemo();
        // vl.addComponent(llwDemo.createDemo().getContent());
        //
        // vl.addComponent(new LazyLoadV7Demo().createTableDemo());
        // vl.addComponent(new LazyLoadV7Demo().crateLabelsDemo());

        // vl.addComponent(new LazyLoadV7Demo().photoShowDemo());

    }

    public DemoParams createFeaturesList(String params) {
        VerticalLayout selectorLayout = new VerticalLayout();
        selectorLayout.setSpacing(true);
        selectorLayout.setMargin(true);
        splitPanel.setFirstComponent(selectorLayout);

        llwFeatures = new LLWFetaureForm();

        DemoParams bean = new DemoParams();
        bean.parseFromParams(params);
        fieldGroup = new FieldGroup(new BeanItem<DemoParams>(bean));
        fieldGroup.bindMemberFields(llwFeatures);

        llwFeatures.addNavigationClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                navigateTo(navigator.getState().split("/")[0]);
            }
        });

        llwFeatures.addResetClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {

                System.out.println("Resetting selections...");
                createFeaturesList(null);
            }
        });

        selectorLayout.addComponent(llwFeatures);

        selectorLayout.addComponent(createFeatureTree());

        return bean;
    }

    private Tree createFeatureTree() {
        if (featureTree != null) {
            return featureTree;
        }

        featureTree = new Tree();
        featureTree.setImmediate(true);

        String parentVersion = "1.0 alpha";

        HierarchicalContainer container = new HierarchicalContainer();
        container.addContainerProperty("caption", String.class, null);
        container.addItem(parentVersion);
        container.setChildrenAllowed(parentVersion, true);

        featureTree.setContainerDataSource(container);

        for (VIEWS view : VIEWS.values()) {
            container.addItem(view);
            container.setChildrenAllowed(view, false);
            featureTree.setItemCaption(view, view.getName());
            container.setParent(view, parentVersion);

        }

        featureTree.addItemClickListener(new ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.getItemId() instanceof VIEWS) {
                    navigateTo((VIEWS) event.getItemId());
                }
            }
        });

        for (Object id : featureTree.rootItemIds()) {
            featureTree.expandItemsRecursively(id);
        }

        return featureTree;
    }

    public void navigateTo(VIEWS view) {

        navigateTo(view.getInternalName());

    }

    public void navigateTo(String viewNameWithParams) {

        if (fieldGroup != null) {
            try {
                fieldGroup.commit();
                BeanItem<DemoParams> params = (BeanItem<DemoParams>) fieldGroup
                        .getItemDataSource();

                navigator.navigateTo(viewNameWithParams
                        + params.getBean().toParams());

            } catch (CommitException e) {

                e.printStackTrace();
                System.out.println("Parameter parsing failed");
            }
        }
    }

    // /**
    // * Examples on how to use the lazy load wrapper
    // */
    // private void wrapperExample() {
    //
    // /*
    // * Simple example of how to wrap a Panel inside a lazy load wrapper
    // */
    //
    // Panel examplepanel = new Panel("This is your panel");
    // examplepanel.addComponent(new Label("This is your content"));
    // examplepanel.addComponent(new Button("A button"));
    //
    // /*
    // * Set a size for the panel (the wrapper will automatically size the
    // * placeholder if child has absolute sizes)
    // */
    // examplepanel.setWidth("300px");
    // examplepanel.setHeight("200px");
    //
    // /* This is how you would normally add your panel to your application. */
    // // mainWindow.addComponent(examplepanel);
    //
    // /* This is how you can easily wrap the component inside the wrapper; */
    // mainWindow.addComponent(new LazyLoadWrapper(examplepanel));
    //
    // /*
    // * If you add a component with a undefined size to the wrapper, the
    // * wrapper will create a default placeholder of 100px x 100px that will
    // * expand when the wrapped component is loaded.
    // */
    // Panel p = new Panel("A panel");
    // p.setWidth("300px");
    // p.addComponent(new Label("A label inside the panel"));
    //
    // HorizontalLayout hl = new HorizontalLayout();
    // hl.setSpacing(true);
    // hl.setSizeUndefined();
    //
    // hl.addComponent(p);
    // hl.addComponent(new Label("A label"));
    // mainWindow.addComponent(new LazyLoadWrapper(hl));
    //
    // /*
    // * If we want to, we can size the placeholder size manually so that it
    // * resembles the size of the actual component. (We must use absolute
    // * values here)
    // *
    // * If we overshoot or underestimate the size, the wrapper will resize
    // * itself when the component is loaded.
    // */
    //
    // Button aButton = new Button("Wrapped button");
    // mainWindow.addComponent(new LazyLoadWrapper(aButton, "120px", "30px"));
    //
    // /*
    // * If a component with a relative size is placed in the wrapper, the
    // * wrapper will try to match it (i.e. copy it's size parameters) while
    // * still creating a placeholder on the client side.
    // *
    // * In this example the wrapped child component has a size of: Width =
    // * 80%, Height = undefined
    // *
    // * The wrapper will copy the sizes from the child component but draw a
    // * placeholder with sizes: Width = 80%, Height = 100px (placeholder
    // * default height)
    // */
    //
    // Panel fullSize = new Panel("YAP...");
    // fullSize.setWidth("80%");
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // fullSize.addComponent(new Label("Some content"));
    // mainWindow.addComponent(new LazyLoadWrapper(fullSize));
    //
    // }

    public static class LLWFetaureForm extends Panel {

        private Slider visibleDelay;
        private Slider proximity;
        private OptionGroup mode;
        private TextField placeholderHeight;
        private TextField placeholderWidth;
        private CheckBox staticContainer;
        private AbstractOrderedLayout layout;
        private Button refresh;
        private CheckBox usingWrappers;
        private NativeSelect heavinessFactor;
        private Button reset;

        public LLWFetaureForm() {

            layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            Label caption = new Label("Demo features");
            layout.addComponent(caption);

            usingWrappers = new CheckBox("Use wrappers");
            layout.addComponent(usingWrappers);

            refresh = new Button("Refresh");

            heavinessFactor = new NativeSelect("Heaviness factor");
            heavinessFactor.setNullSelectionAllowed(false);
            for (int i = 0; i < 10; i++) {
                heavinessFactor.addItem(i);
            }
            layout.addComponent(heavinessFactor);

            Label spacer = new Label("<hr/>", ContentMode.HTML);
            layout.addComponent(spacer);

            setCaption("LLW Features");

            visibleDelay = new Slider("Visible Delay");
            visibleDelay.setWidth("100%");
            visibleDelay.setMax(4000);
            visibleDelay.setMin(0);
            visibleDelay.setConverter(new DoubleToIntConverter());

            proximity = new Slider("Proximity");
            proximity.setConverter(new DoubleToIntConverter());
            proximity.setWidth("100%");
            proximity.setMax(1000);
            proximity.setMin(-1000);

            mode = new OptionGroup("Mode");
            mode.addItem(LazyLoadWrapper.MODE_LAZY_LOAD_FETCH);
            mode.setItemCaption(LazyLoadWrapper.MODE_LAZY_LOAD_FETCH,
                    "Lazy fetch");
            mode.addItem(LazyLoadWrapper.MODE_LAZY_LOAD_DRAW);
            mode.setItemCaption(LazyLoadWrapper.MODE_LAZY_LOAD_DRAW,
                    "Lazy draw");

            placeholderHeight = new TextField("Placeholder height");
            placeholderHeight.setNullRepresentation("");
            placeholderHeight.setNullSettingAllowed(true);

            placeholderWidth = new TextField("Placeholder width");
            placeholderWidth.setNullRepresentation("");
            placeholderWidth.setNullSettingAllowed(true);

            staticContainer = new CheckBox("Static container");

            addComponent(visibleDelay);
            addComponent(proximity);
            addComponent(mode);
            addComponent(mode);
            addComponent(placeholderHeight);
            addComponent(placeholderWidth);
            addComponent(staticContainer);

            HorizontalLayout hl = new HorizontalLayout();
            hl.setSpacing(true);
            hl.addComponent(refresh);

            reset = new Button("Reset");
            reset.addStyleName(Reindeer.BUTTON_LINK);

            hl.addComponent(reset);

            addComponent(hl);

        }

        public void addNavigationClickListener(ClickListener listener) {
            refresh.addClickListener(listener);
        }

        public void addResetClickListener(ClickListener listener) {
            reset.addClickListener(listener);
        }

        protected void addComponent(Component component) {
            layout.addComponent(component);
        }
    }

    public static class DoubleToIntConverter implements
            Converter<Double, Integer> {
        @Override
        public Integer convertToModel(Double value, Locale locale)
                throws com.vaadin.data.util.converter.Converter.ConversionException {
            return value.intValue();
        }

        @Override
        public Double convertToPresentation(Integer value, Locale locale)
                throws com.vaadin.data.util.converter.Converter.ConversionException {
            return new Double(value);
        }

        @Override
        public Class<Integer> getModelType() {
            return Integer.class;
        }

        @Override
        public Class<Double> getPresentationType() {
            return Double.class;
        }
    }
}
