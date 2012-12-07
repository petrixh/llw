package com.vaadin.lazyloadwrapper;

import java.io.Serializable;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.lazyloadwrapper.LazyLoadWrapper.LazyLoadComponentProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class LazyLoadWrapperDemo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3214543723791435116L;
    private static final String version = "1.0";

    LazyLoadWrapper llw;
    Window allFeaturesWindow, mainWindow, loadExampleNoWrapper,
            loadExampleWithWrapper;

    Panel demoPanel = null;

    Table table;
    int tableSize = 4;
    int heavyness = 4;
    boolean showWrappers = false;
    Panel mainPanel;

    Application appInstance = null;

    public LazyLoadWrapperDemo(Application applicationInstance) {
        appInstance = applicationInstance;

    }

    public Window createDemo() {

        mainWindow = new Window("Lazy load wrapper v." + version);
        mainPanel = new Panel("Lazy load Wrapper v." + version + " demo.");
        mainWindow.addComponent(mainPanel);

        Label heading = new Label(
                "<h2>Lazy Load Wrapper v."
                        + version
                        + "</h2><p>Lazy load wrapper is a wrapper class for Vaadin that can take any Vaadin GUI component"
                        + " and load it lazily when it's scrolled in to view.</p> "
                        + "<p>The wrapper can cut down the time it takes to render a heavy page upon initialization, as heavy components are not "
                        + "loaded and rendered before they are actually needed. The wrapper itself creates a lightweight placeholder in place of the component on the client side."
                        + "<h2>Heavy application example</h2><p>Below is an example of how lazy loading wrappers can be used to speed up the "
                        + "start up of a heavy application. The application in it self is not supposed to do anything except consume a lot of resources... You can set a "
                        + "heaviness factor to see the difference in loading times. The heaviness factor adds more components to the \"heavy compnent\" at the bottom that will, without the wrapper, "
                        + " slow down the initial startup of the application. If you want to actually se the wrappers you can select the \"Show me the "
                        + "wrappers\" selector in order to see how the wrapper loads the components. When the \"Show me the wrappers\" selector is enabled, the wrappers are configured to "
                        + "load a component when it's at least 100 pixels visible and have been visible for 1 second.</p>",
                Label.CONTENT_XHTML);

        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setWidth("99%");

        hl.addComponent(heading);

        LazyLoadWrapper llw = new LazyLoadWrapper();
        hl.addComponent(llw);

        hl.setExpandRatio(heading, 10);
        hl.setExpandRatio(llw, 1);

        hl.setComponentAlignment(llw, Alignment.TOP_RIGHT);

        mainPanel.addComponent(hl);

        Button launchHeavyAppNoWrapperExample = new Button(
                "Launch heavy app example without wrappers",
                new Button.ClickListener() {

                    public void buttonClick(ClickEvent event) {

                        heavyAppLaunchExample(heavyness, false, 0, 0);
                        mainWindow.addWindow(loadExampleNoWrapper);
                    }

                });
        launchHeavyAppNoWrapperExample.setWidth("275px");

        HorizontalLayout selectors1 = new HorizontalLayout();
        NativeSelect heavynessSelect = new NativeSelect("App heaviness factor:");
        for (int i = 0; i < 10; i++) {
            heavynessSelect.addItem(i + "");
        }

        heavynessSelect.setNullSelectionAllowed(false);
        heavynessSelect.setValue("1");
        heavynessSelect.setImmediate(true);

        class ChangeListener implements ValueChangeListener {

            public void valueChange(ValueChangeEvent event) {

                try {
                    heavyness = (Integer.parseInt(event.getProperty()
                            .toString())) * 4;
                } catch (NumberFormatException nfe) {
                }

            }

        }

        heavynessSelect.addListener(new ChangeListener());
        selectors1.addComponent(launchHeavyAppNoWrapperExample);

        selectors1.addComponent(heavynessSelect);
        selectors1.setSpacing(true);
        selectors1.setComponentAlignment(heavynessSelect, Alignment.TOP_RIGHT);
        selectors1.setWidth("500px");
        mainPanel.addComponent(selectors1);

        Button launchHeavyAppWithWrapperExample = new Button(
                "Launch heavy app example with wrappers",
                new Button.ClickListener() {

                    public void buttonClick(ClickEvent event) {

                        if (showWrappers) {
                            heavyAppLaunchExample(heavyness, true, 1000, -100);

                        } else {
                            heavyAppLaunchExample(heavyness, true, 0, 250);
                        }

                        mainWindow.addWindow(loadExampleWithWrapper);

                    }
                });

        launchHeavyAppWithWrapperExample.setWidth("275px");
        HorizontalLayout selectors2 = new HorizontalLayout();
        selectors2.addComponent(launchHeavyAppWithWrapperExample);

        // Button w/ text and tooltip
        CheckBox showWrappersSelect = new CheckBox("Show me the wrappers");
        showWrappersSelect
                .setDescription("Sets the parameters of the lazy load wrapper so that you will see the wrappers before content is loaded.");
        showWrappersSelect.setImmediate(true);
        showWrappersSelect.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                showWrappers = event.getButton().booleanValue();

            }
        });

        selectors2.addComponent(showWrappersSelect);
        selectors2.setComponentAlignment(showWrappersSelect,
                Alignment.TOP_RIGHT);
        selectors2.setWidth("500px");

        mainPanel.addComponent(selectors2);

        // Label heading2 = new Label(
        // "<h2>Lazy load wrapper feature demo</h2><p>Below is a presentation of all of the "
        // +
        // "features of lazy load wrapper. The wrapper has many parameters that can be changed in order to "
        // + "accommodate many different user scenarios. </p>");
        // heading2.setContentMode(Label.CONTENT_XHTML);
        // mainPanel.addComponent(heading2);
        //
        // Button featureDemo = new Button("Launch feature demo",
        // new Button.ClickListener() {
        //
        // public void buttonClick(ClickEvent event) {
        // createTechDemo();
        //
        // mainWindow.addWindow(allFeaturesWindow);
        //
        // }
        // });
        //
        // mainPanel.addComponent(featureDemo);

        return mainWindow;

    }

    private void heavyAppLaunchExample(int heaviness, boolean useWrapper,
            int delay, int proximity) {

        GridLayout gl = new GridLayout();
        gl.setRows(4);
        gl.setColumns(4);
        gl.setSpacing(true);

        if (useWrapper) {
            loadExampleWithWrapper = new Window(
                    "Heavy app demo with wrappers. Heaviness index: "
                            + heaviness / 4 + ". Wrapper visible delay: "
                            + delay + " ms. Wrapper proximity: " + proximity
                            + " pixels.");
            // loadExampleWithWrapper.setTheme("llwtheme");
            loadExampleWithWrapper.setName("heavyAppWithWrappers");
            // this.addWindow(loadExampleWithWrapper);
            loadExampleWithWrapper.setContent(gl);
            loadExampleWithWrapper.setWidth("90%");
            loadExampleWithWrapper.setHeight("90%");

        } else {
            loadExampleNoWrapper = new Window(
                    "Heavy app demo without wrappers. Heaviness index: "
                            + heaviness / 4 + ".");
            // this.addWindow(loadExampleNoWrapper);
            loadExampleNoWrapper.setContent(gl);
            loadExampleNoWrapper.setWidth("90%");
            loadExampleNoWrapper.setHeight("90%");
        }

        Panel text = new Panel();
        text.setWidth("1250px");
        text.setHeight("1250px");
        text
                .addComponent(new Label(
                        "<p>This is a example application that has a \"heavy component\" at the bottom of the page and a moderately heavy component to the right. The \"heavy component\" is a  "
                                + " nested set of layouts that have components in them . This is NOT a demonstration of good programming (actually the opposite) but works for showing the difference "
                                + "between applications with different levels of required initial computation. </p> <p>The wrapping in this example is done through nesting several lazy load wrappers "
                                + " within each other. First all of the \"heavy components\" are nested inside their own wrappers. Then all of them are nested inside a big wrapper.</p>",
                        Label.CONTENT_XHTML));

        ((VerticalLayout) text.getContent()).setSpacing(true);
        gl.addComponent(text, 0, 0);

        VerticalLayout vl1 = new VerticalLayout();
        Table table1 = new Table();
        table1.setWidth("400px");
        table1.setHeight("400px");
        table1.addContainerProperty("Label", Label.class, null);
        table1.addContainerProperty("Button", Button.class, null);

        for (int i = 0; i < 5; i++) {
            table1.addItem(new Object[] { new Label("Random text"),
                    new Button("A button") }, new Integer(i));
        }
        vl1.addComponent(table1);
        HorizontalLayout idfLayout = new HorizontalLayout();
        idfLayout.setSpacing(true);
        idfLayout.addComponent(new InlineDateField());
        VerticalLayout idfLayoutVL = new VerticalLayout();
        idfLayoutVL.setSpacing(true);
        idfLayoutVL.addComponent(new Label("Pick a date... "));
        idfLayoutVL.addComponent(new Label("it will do..."));
        idfLayoutVL.addComponent(new Label("absolutely nothing..."));
        idfLayout.addComponent(idfLayoutVL);

        Panel tableAndIDF = new Panel();
        tableAndIDF.addComponent(vl1);

        vl1.addComponent(idfLayout);
        if (useWrapper) {
            gl.addComponent(new LazyLoadWrapper(tableAndIDF, proximity, delay,
                    "450px", "600px", true), 1, 0);
        } else {
            gl.addComponent(tableAndIDF, 1, 0);
        }

        if ((heaviness / 4) > 0) {
            GridLayout gl2 = new GridLayout();
            gl2.setRows((heaviness / 4) + 1);
            gl2.setColumns(4);
            gl2.setSpacing(true);

            Panel morePanels[] = new Panel[heaviness];
            int row = 0;
            int col = 0;

            for (int i = 0; i < heaviness; i++) {
                morePanels[i] = new Panel("A \"heavy component\" grid pos:"
                        + (i / 4) + "," + i);
            }

            for (int i = 0; i < (heaviness / 4); i++) {
                for (int j = 0; j < 4; j++) {
                    // morePanels[(i * 4) + j].addComponent(new
                    // InlineDateField());
                    morePanels[(i * 4) + j].addComponent(new HeavyComponent());
                    if (useWrapper) {
                        LazyLoadWrapper llw = new LazyLoadWrapper(
                                morePanels[(i * 4) + j], proximity, delay,
                                "212px", "158px", true);
                        // llw.setMode(LazyLoadWrapper.MODE_LAZY_LOAD_DRAW);
                        gl2.addComponent(llw, j, i);
                    } else {
                        gl2.addComponent(morePanels[(i * 4) + j], j, i);
                    }
                }
            }

            if (useWrapper) {
                gl.addComponent(new LazyLoadWrapper(gl2, proximity, delay,
                        "1255px", 158 * (heaviness / 4) + "px", false), 0, 2);
            } else {
                gl.addComponent(gl2, 0, 2);
            }

        }

    }

    private void createTechDemo() {

        if (allFeaturesWindow != null
                && appInstance.getWindows().contains(allFeaturesWindow)) {
            appInstance.removeWindow(allFeaturesWindow);
        }

        allFeaturesWindow = new Window("Lazy load wrapper v." + version);
        Label label = new Label("Lazy load wrapper v." + version);
        allFeaturesWindow.addComponent(label);
        allFeaturesWindow.setName("AllFeatures");
        // allFeaturesWindow.setTheme("llwtheme");
        ((VerticalLayout) allFeaturesWindow.getLayout()).setSpacing(true);
        // this.addWindow(allFeaturesWindow);
        allFeaturesWindow.setWidth("1060px");
        allFeaturesWindow.setHeight("90%");
        allFeaturesWindow.setScrollable(true);

        demoPanel = new Panel("This is the demo panel");

        Panel bigPanel = new Panel();
        bigPanel.setWidth("1000px");
        bigPanel.setHeight("1500px");
        bigPanel.addComponent(new Label(
                "This is a big panel to enable scrolling"));

        String desc = "<br>The lazy load wrapper is a wrapper class for "
                + "any Vaadin component that implements the lazy load feature"
                + " on a component level. \n\nThe wrapper has a few adjustable "
                + "features: <br>"
                + "<p>\t <b>Proximity:</b> the nr of pixels from the view area of "
                + "the client browser that the lazy load component should be loaded. </p>"
                + "<p><b>VisibleDelay:</b> The time in milliseconds how long the "
                + "placeholder should be visible before the component is actually loaded. </p>"
                + "<p><b>Placeholder size:</b> The size of the placeholder on the client-"
                + "side. </p>"
                + "<p><b>StaticPlaceholder:</b> Defines wether or not the placeholder should "
                + "keep it's size when the component is loaded inside the placeholder </p>"
                + "<br> <p> The lazy load wrapper has also two modes for receiving content. The component "
                + "can be provided to the wrapper upon creation (client-side lazy load) or the wrapper "
                + "can ask for the component when it's needed through the<i> \"ChildProvider\"</i>-interface "
                + "(client- and server-side lazy load) (<i>MODE_LAZY_LOAD_FETCH</i>). </p> <p>Finally the lazy load wrapper has"
                + " a feature called \"Lazy load draw\" "
                + " that tries to minimize the client-server "
                + "communication by sending all of the child components to the client-side upon "
                + "initialization and rendering them only as needed. When the wrapper is"
                + "in the lazy load draw mode, the wrapper registers itself as listener for the UIDL updates for the "
                + "undrawn children and catches any updates that the cild components recieve. Finally when the child component"
                + "becomes visible it and it's updates are drawn. (<i>MODE_LAZY_LOAD_DRAW</i>)</p>"
                + "<br>Below are a few examples of the lazy load wrapper in action.";

        Label description = new Label(desc);
        description.setContentMode(Label.CONTENT_XHTML);
        bigPanel.addComponent(description);

        allFeaturesWindow.addComponent(bigPanel);

        ((VerticalLayout) demoPanel.getLayout()).setSpacing(true);
        allFeaturesWindow.addComponent(demoPanel);

        final int defProximity = 250;
        final int defVisibleDly = 0;

        final GridLayout gl = new GridLayout();
        gl.setColumns(2);
        gl.setRows(15);
        gl.setSpacing(true);
        demoPanel.addComponent(gl);

        gl.setSpacing(true);

        gl.addComponent(createBasicExpandExample(defProximity, defVisibleDly),
                0, 0);
        // gl.addComponent(createBasicExpandExample(defProximity,
        // defVisibleDly), 0, 1);

        Panel expandController = new Panel("Set parameters and reload");
        final TextField expandMargin = new TextField("Proximity in px");
        final TextField expandDelay = new TextField("Visible delay in ms");
        expandMargin.setValue(defProximity + "");
        expandDelay.setValue(defVisibleDly + "");

        expandController.addComponent(expandMargin);
        expandController.addComponent(expandDelay);
        Button expandReload = new Button("Reload", new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {

                try {
                    int marg = Integer.parseInt((String) expandMargin
                            .getValue());
                    int del = Integer.parseInt((String) expandDelay.getValue());

                    gl.removeComponent(0, 0);
                    gl.addComponent(createBasicExpandExample(marg, del), 0, 0);

                } catch (NumberFormatException nfe) {
                    allFeaturesWindow
                            .showNotification("Unable to parse proximity or delay!");
                }

            }
        });

        VerticalLayout vl = new VerticalLayout();
        ((VerticalLayout) expandController.getLayout()).setSpacing(true);
        expandController.addComponent(expandMargin);
        expandController.addComponent(expandDelay);
        expandController.addComponent(expandReload);
        expandController.setWidth("200px");

        gl.addComponent(expandController, 1, 0);
        gl.setWidth("100%");
        gl.setComponentAlignment(expandController, Alignment.TOP_RIGHT);

        //
        // nested example
        //

        gl.addComponent(createNestedExample(defProximity, defVisibleDly), 0, 2);

        Panel nestedController = new Panel("Set parameters and reload");
        final TextField nestedMargin = new TextField("Proximity in px");
        final TextField nestedDelay = new TextField("Visible delay in ms");
        nestedMargin.setValue(defProximity + "");
        nestedDelay.setValue(defVisibleDly + "");

        nestedController.addComponent(nestedMargin);
        nestedController.addComponent(nestedDelay);
        Button nestedReload = new Button("Reload", new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {

                try {
                    int marg = Integer.parseInt((String) nestedMargin
                            .getValue());
                    int del = Integer.parseInt((String) nestedDelay.getValue());

                    gl.removeComponent(0, 2);
                    gl.addComponent(createNestedExample(marg, del), 0, 2);

                } catch (NumberFormatException nfe) {
                    allFeaturesWindow
                            .showNotification("Unable to parse proximity or delay!");
                }

            }
        });

        ((VerticalLayout) nestedController.getLayout()).setSpacing(true);
        nestedController.addComponent(nestedMargin);
        nestedController.addComponent(nestedDelay);
        nestedController.addComponent(nestedReload);
        nestedController.setWidth("200px");

        gl.addComponent(nestedController, 1, 2);
        gl.setWidth("100%");
        gl.setComponentAlignment(nestedController, Alignment.TOP_RIGHT);

        //
        // inside scrollable
        //

        gl.addComponent(createInsideScrollable(defProximity, defVisibleDly), 0,
                3);
        Panel scrollController = new Panel("Set parameters and reload");
        final TextField scrollMargin = new TextField("Proximity in px");
        final TextField scrollDelay = new TextField("Visible delay in ms");
        scrollMargin.setValue(defProximity + "");
        scrollDelay.setValue(defVisibleDly + "");

        scrollController.addComponent(scrollMargin);
        scrollController.addComponent(scrollDelay);
        Button scrollReload = new Button("Reload", new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {

                try {
                    int marg = Integer.parseInt((String) scrollMargin
                            .getValue());
                    int del = Integer.parseInt((String) scrollDelay.getValue());

                    gl.removeComponent(0, 3);
                    gl.addComponent(createInsideScrollable(marg, del), 0, 3);

                } catch (NumberFormatException nfe) {
                    allFeaturesWindow
                            .showNotification("Unable to parse proximity or delay!");
                }

            }
        });

        ((VerticalLayout) scrollController.getLayout()).setSpacing(true);
        scrollController.addComponent(scrollMargin);
        scrollController.addComponent(scrollDelay);
        scrollController.addComponent(scrollReload);
        scrollController.setWidth("200px");

        gl.addComponent(scrollController, 1, 3);
        gl.setWidth("100%");
        gl.setComponentAlignment(scrollController, Alignment.TOP_RIGHT);

        //
        // Server side lazy load
        //

        gl.addComponent(createServerSideLazyLoad(defProximity, defVisibleDly),
                0, 4);
        Panel SSLController = new Panel("Set parameters and reload");
        final TextField SLLMargin = new TextField("Proximity in px");
        final TextField SLLDelay = new TextField("Visible delay in ms");
        SLLMargin.setValue(defProximity + "");
        SLLDelay.setValue(defVisibleDly + "");

        Button SSLReload = new Button("Reload", new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {

                try {
                    int marg = Integer.parseInt((String) SLLMargin.getValue());
                    int del = Integer.parseInt((String) SLLDelay.getValue());

                    gl.removeComponent(0, 4);
                    gl.addComponent(createServerSideLazyLoad(marg, del), 0, 4);

                } catch (NumberFormatException nfe) {
                    allFeaturesWindow
                            .showNotification("Unable to parse proximity or delay!");
                }

            }
        });

        ((VerticalLayout) SSLController.getLayout()).setSpacing(true);
        SSLController.addComponent(SLLMargin);
        SSLController.addComponent(SLLDelay);
        SSLController.addComponent(SSLReload);
        SSLController.setWidth("200px");

        gl.addComponent(SSLController, 1, 4);
        gl.setWidth("100%");
        gl.setComponentAlignment(SSLController, Alignment.TOP_RIGHT);

        //
        // "MODE_B"
        //

        gl
                .addComponent(
                        createClientSideOnly(defProximity, defVisibleDly), 0, 5);
        Panel BController = new Panel("Set parameters and reload");
        final TextField BMargin = new TextField("Proximity in px");
        final TextField BDelay = new TextField("Visible delay in ms");
        BMargin.setValue(defProximity + "");
        BDelay.setValue(defVisibleDly + "");

        Button BReload = new Button("Reload", new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {

                try {
                    int marg = Integer.parseInt((String) BMargin.getValue());
                    int del = Integer.parseInt((String) BDelay.getValue());

                    gl.removeComponent(0, 5);
                    gl.addComponent(createClientSideOnly(marg, del), 0, 5);

                } catch (NumberFormatException nfe) {
                    allFeaturesWindow
                            .showNotification("Unable to parse proximity or delay!");
                }

            }
        });

        ((VerticalLayout) BController.getLayout()).setSpacing(true);
        BController.addComponent(BMargin);
        BController.addComponent(BDelay);
        BController.addComponent(BReload);
        BController.setWidth("200px");

        gl.addComponent(BController, 1, 5);
        gl.setWidth("100%");
        gl.setComponentAlignment(BController, Alignment.TOP_RIGHT);

    }

    private Component createBasicExpandExample(int proximity, int visibleDelay) {

        HorizontalLayout exampleRoot = new HorizontalLayout();
        exampleRoot.setSpacing(true);

        /*
         * Example 1 default behavior
         */
        Panel wrapper = new Panel("Default settings");
        // wrapper.setWidth("220px");

        wrapper.addComponent(new Label("This is a lazily loaded label"));
        wrapper.addComponent(new Label("This is another lazily loaded label"));
        wrapper.addComponent(new Button("Lazily loaded Button",
                new Button.ClickListener() {

                    public void buttonClick(ClickEvent event) {
                        allFeaturesWindow.showNotification("Button pressed");

                    }
                }));

        // LazyLoadWrapper defaultBehavior = new LazyLoadWrapper(wrapper);
        LazyLoadWrapper defaultBehavior = new LazyLoadWrapper(wrapper,
                proximity, visibleDelay);

        exampleRoot.addComponent(defaultBehavior);

        /*
         * Example 2 default behavior sized placeholder
         */

        Panel wrapper2 = new Panel("Example with predefined size");
        wrapper2.setWidth("220px");
        wrapper2.addComponent(new Label("This is a lazily loaded label"));
        wrapper2.addComponent(new Label("This is another lazily loaded label"));
        wrapper2.addComponent(new Button("Lazily loaded Button",
                new Button.ClickListener() {

                    public void buttonClick(ClickEvent event) {
                        allFeaturesWindow.showNotification("Button pressed");

                    }
                }));

        // LazyLoadWrapper defaultBehaviorDefPhSize = new
        // LazyLoadWrapper(wrapper2, "200px", "100px");
        LazyLoadWrapper defaultBehaviorDefPhSize = new LazyLoadWrapper(
                wrapper2, proximity, visibleDelay, "200px", "200px", false);

        exampleRoot.addComponent(defaultBehaviorDefPhSize);

        /*
         * Example 3 default behavior sized placeholder
         */

        Panel wrapper3 = new Panel("Defined size, static placeholder");
        wrapper3.addComponent(new Label("This is a lazily loaded label"));
        wrapper3.addComponent(new Label("This is another lazily loaded label"));
        wrapper3.addComponent(new Button("Lazily loaded Button",
                new Button.ClickListener() {

                    public void buttonClick(ClickEvent event) {
                        allFeaturesWindow.showNotification("Button pressed");

                    }
                }));

        // LazyLoadWrapper defaultBehaviorDefPhSizeStaticPh = new
        // LazyLoadWrapper(wrapper3, "200px", "100px", true);
        LazyLoadWrapper defaultBehaviorDefPhSizeStaticPh = new LazyLoadWrapper(
                wrapper3, proximity, visibleDelay, "180px", "160px", true);
        exampleRoot.addComponent(defaultBehaviorDefPhSizeStaticPh);

        Label expandDesc = new Label(
                "<p>This is a demo of the placeholder sizing as well as expand and static modes. <br><br>The first "
                        + "placeholder is created with default settings for the lazy load wrapper that creates a "
                        + "wrapper with a placeholder with <ul>"
                        + "<li>width = 100px, height = 100px </li> <li>proximity = 250px</li> <li>visibleDelay = 0</li> <li>staticContainer = false</li> </ul> The panel in the first example has a defined with of 220 px "
                        + "and a undefined height, so the placeholder is automatically rezied to 220px x 100px. </p>"
                        + "<p>The second placeholder has a user (programmer) defined size of 200x200px and the staticContainer set to false.</p>"
                        + "<p>The third example has a defined size of 180x160px and the staticContainer set to true. </p>");

        expandDesc.setContentMode(Label.CONTENT_XHTML);

        Panel basicExample = new Panel("Placeholder size, expand and static.");
        basicExample.setWidth("750px");
        basicExample.addComponent(expandDesc);
        basicExample.addComponent(exampleRoot);

        return basicExample;
    }

    private Component createNestedExample(int proximity, int visibleDelay) {

        Label desc = new Label(
                "<p>In this example a table with nested lazy load wrappers that are "
                        + "wrapped inside a lazy load wrapper. ");
        desc.setContentMode(Label.CONTENT_XHTML);

        class ButtonListerner implements Button.ClickListener {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent event) {
                allFeaturesWindow.showNotification("Table button pressed");

            }

        }
        ButtonListerner listener = new ButtonListerner();

        Button b1 = new Button("Button 1", listener);
        Button b2 = new Button("Button 2", listener);
        Button b3 = new Button("Button 3", listener);
        Button b4 = new Button("Button 4", listener);
        Button b5 = new Button("Button 5", listener);

        LazyLoadWrapper llw1 = new LazyLoadWrapper(b1, proximity, visibleDelay,
                "70px", "26px", false);
        LazyLoadWrapper llw2 = new LazyLoadWrapper(b2, proximity, visibleDelay,
                "70px", "26px", false);
        LazyLoadWrapper llw3 = new LazyLoadWrapper(b3, proximity, visibleDelay,
                "70px", "26px", false);
        LazyLoadWrapper llw4 = new LazyLoadWrapper(b4, proximity, visibleDelay,
                "70px", "26px", false);
        LazyLoadWrapper llw5 = new LazyLoadWrapper(b5, proximity, visibleDelay,
                "70px", "26px", false);

        Table table = new Table("This is a Table");
        table.setHeight("220px");

        table.addContainerProperty("First Name", String.class, null);
        table.addContainerProperty("Last Name", String.class, null);
        table.addContainerProperty("Year", Integer.class, null);
        table.addContainerProperty("Component", Component.class, null);

        table.addItem(new Object[] { "Nicolaus", "Copernicus",
                new Integer(1473), llw1 }, new Integer(1));
        table.addItem(
                new Object[] { "Tycho", "Brahe", new Integer(1546), llw2 },
                new Integer(2));
        table.addItem(new Object[] { "Giordano", "Bruno", new Integer(1548),
                llw3 }, new Integer(3));
        table.addItem(new Object[] { "Galileo", "Galilei", new Integer(1564),
                llw4 }, new Integer(4));
        table.addItem(new Object[] { "Johannes", "Kepler", new Integer(1571),
                llw5 }, new Integer(5));

        Panel panel = new Panel("Table with child wrappers");
        panel.addComponent(desc);
        panel.addComponent(new LazyLoadWrapper(table, proximity, visibleDelay,
                "340px", "250px", false));
        // demoPanel.addComponent(panel);
        return panel;

    }

    private Component createInsideScrollable(int proximity, int visibleDelay) {

        Label desc = new Label(
                "<p>In this example a lazy load wrapper is hidden behind the scrollable area of "
                        + "an other component (in this case a panel). The wrapper will not load the component until "
                        + "the placeholder is actually visible (also considers the proximity parameter default = 250px). "
                        + "You can test this by setting proximity=-50 and delay=0. </p>");
        desc.setContentMode(Label.CONTENT_XHTML);

        Panel scrollPanel = new Panel();
        scrollPanel.setHeight("200px");
        scrollPanel.setWidth("200px");
        scrollPanel.setScrollable(true);
        scrollPanel.setStyleName("opacityPanel");

        for (int i = 0; i < 10; i++) {
            scrollPanel.addComponent(new Label("Some random data"));
        }

        Button b = new Button();
        b.addListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                allFeaturesWindow.showNotification("Button pressed!");

            }
        });

        Button button = new Button("Hidden button", new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                allFeaturesWindow
                        .showNotification("You found the hidden button!");

            }
        });

        LazyLoadWrapper insideScrollWrapper = new LazyLoadWrapper(button,
                proximity, visibleDelay);

        HorizontalLayout hl = new HorizontalLayout();
        Panel shiftPanel = new Panel();

        shiftPanel.setHeight("130px");
        shiftPanel.setWidth("130px");
        hl.addComponent(new Label("Panel that shifts LLW to the right"));
        hl.addComponent(insideScrollWrapper);

        shiftPanel.setContent(hl);
        shiftPanel.setScrollable(true);
        scrollPanel.addComponent(shiftPanel);
        // hl.addComponent(insideScrollWrapper);

        // scrollPanel.addComponent(hl);

        for (int i = 0; i < 20; i++) {
            scrollPanel.addComponent(new Label("Some random data"));
        }

        Panel scrollPanel2 = new Panel();
        scrollPanel2.setHeight("300px");
        scrollPanel2.setWidth("300px");

        for (int i = 0; i < 2; i++) {
            scrollPanel2.addComponent(new Label("Some random data"));
        }

        scrollPanel2.addComponent(scrollPanel);

        for (int i = 0; i < 20; i++) {
            scrollPanel2.addComponent(new Label("Some random data"));
        }

        Panel panel = new Panel("Wrapper not actually visible");
        // HorizontalLayout roothl = new HorizontalLayout();
        // roothl.setSpacing(true);
        // panel.setLayout(roothl);
        ((VerticalLayout) panel.getLayout()).setSpacing(true);
        panel.addComponent(desc);
        panel.addComponent(scrollPanel2);

        return panel;

    }

    private Component createServerSideLazyLoad(int proximity, int visibleDelay) {
        Label desc = new Label(
                "<p>Here a Inline date field is lazely loaded on both client- and server-side</p>");
        desc.setContentMode(Label.CONTENT_XHTML);

        Panel panel = new Panel("Server side lazy load");
        panel.addComponent(desc);

        LazyLoadWrapper serverSideLazy = new LazyLoadWrapper(proximity,
                visibleDelay, "260px", "200px", false,
                new LazyLoadComponentProvider() {

                    /**
             * 
             */
                    private static final long serialVersionUID = -881434261604841798L;

                    public Component onComponentVisible() {

                        InlineDateField idf = new InlineDateField(
                                "SS-lazy loaded IDF");

                        return idf;
                    }

                });

        panel.addComponent(serverSideLazy);

        return panel;
    }

    private Component createClientSideOnly(int proximity, int visibleDelay) {

        Label desc = new Label(
                "<p>In this example, a table with four columns is wrapped inside the lazy load wrapper. When "
                        + "the placeholder becomes visible, the table is rendered on the client side without any server communication."
                        + " All information about the "
                        + "child component (and it's children) is sent to the client-side on the first update and rendered lazily. </p>"
                        + "<p>If a undrawn child component is updated before it's drawn, the update information is stored in the wrapper and "
                        + "used when the child component is actually drawn. </p> <p>You can test this feature by pressing one of the"
                        + " buttons that change the contents of the table before it's actually drawn. To get the placeholder partly visible "
                        + "while pressing the buttons, you can try setting the margin to -200 and the delay to 1000. This means that the placeholder "
                        + "must be 200 pixels above the browser bottom for 1 second before the contents is drawn.</p>");
        desc.setContentMode(Label.CONTENT_XHTML);

        Panel expand = new Panel(
                "\"MODE_LAZY_LOAD_DRAW\": all child components are sent to the client side upon initialization. ");
        expand.addComponent(desc);

        final VerticalLayout vl = new VerticalLayout();
        vl.setSpacing(true);

        final Label l = new Label("Changable label");
        final Label l2 = new Label("This label should not change!");
        final Label l3 = new Label("This label should not change!");

        table = new Table("This is a Table");
        table.setHeight("150px");
        table.setWidth("600px");

        table.addContainerProperty("First Name", String.class, null);
        table.addContainerProperty("Last Name", String.class, null);
        table.addContainerProperty("Year", Integer.class, null);
        table.addContainerProperty("Component", Component.class, null);

        table.addItem(new Object[] { "Nicolaus", "Copernicus",
                new Integer(1473), l }, new Integer(1));
        table.addItem(new Object[] { "Tycho", "Brahe", new Integer(1546), l2 },
                new Integer(2));
        table.addItem(
                new Object[] { "Giordano", "Bruno", new Integer(1548), l3 },
                new Integer(3));

        vl.addComponent(table);

        LazyLoadWrapper llw1 = new LazyLoadWrapper(vl, proximity, visibleDelay,
                "600px", "200px", false);
        llw1.setMode(2);

        expand.addComponent(llw1);

        Button b2 = new Button("Change label1 on table row 1",
                new Button.ClickListener() {

                    int update = 1;

                    public void buttonClick(ClickEvent event) {
                        l.setValue(update++ + " Update to label.");

                    }
                });

        Button b3 = new Button("Add row to table", new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {

                table.addItem(new Object[] { new String("New"),
                        new String("entry"), new Integer(99),
                        new Label("update") }, new Integer(tableSize++));

            }
        });

        b2.setWidth("200px");
        b3.setWidth("200px");

        Panel rootPanel = new Panel("Client side mode \"lazy load draw\"");
        ((VerticalLayout) rootPanel.getLayout()).setSpacing(true);
        rootPanel.addComponent(b2);
        rootPanel.addComponent(b3);

        rootPanel.addComponent(expand);

        return rootPanel;

    }

}

class HeavyComponent extends Panel {

    public HeavyComponent() {
        int rows = 2;
        int cols = 4;
        int counter = 0;

        GridLayout gl = new GridLayout();
        gl.setSpacing(true);
        gl.setRows(rows);
        gl.setColumns(cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                VerticalLayout vl = new VerticalLayout();
                vl.setSpacing(true);

                HorizontalLayout hl = new HorizontalLayout();
                hl.addComponent(new Label("" + counter++));
                hl.addComponent(new Label("" + counter++));
                hl.setSpacing(true);

                vl.addComponent(hl);

                hl = new HorizontalLayout();
                hl.addComponent(new Label("" + counter++));
                hl.addComponent(new Label("" + counter++));
                hl.setSpacing(true);

                vl.addComponent(hl);

                gl.addComponent(vl, j, i);

            }

        }

        setContent(gl);

    }
}
