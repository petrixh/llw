package com.vaadin.lazyloadwrapper.olddemo;

import java.util.ArrayList;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class TableOutOfSync extends Window {

    private Table table;
    private IndexedContainer ic;
    private ArrayList<String> objIDs = new ArrayList<String>();
    private VerticalLayout mainLayout;

    // private Table.ColumnGenerator colGen = new ColumnGenerator() {
    //
    // @Override
    // public Component generateCell(Table source, Object itemId, Object
    // columnId) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    // };

    public TableOutOfSync() {

        mainLayout = new VerticalLayout();

        addComponent(new Label("Test of LLW out of sync..."));
        table = new Table();
        createTableContent();
        addComponent(table);
        setSizeFull();
        table.setSizeFull();
        // table.setHeight("400px");
        table.setPageLength(2);
        table.setCacheRate(0);

    }

    public void addComponent(Component c) {
        mainLayout.addComponent(c);
    }

    public void createTableContent() {
        if (ic == null) {
            ic = new IndexedContainer();
            ic.addContainerProperty("Name", String.class, "");
            ic.addContainerProperty("image", VerticalLayout.class, null);
            table.setContainerDataSource(ic);
        } else {
            ic.removeAllItems();
        }

        for (int i = 0; i < 120; i++) {
            String id = "Vaadin" + i;
            objIDs.add(id);
            Item item = table.addItem(id);
            item.getItemProperty("Name").setValue(id);
            VerticalLayout vl = new VerticalLayout();
            vl.addComponent(new LazyLoadWrapper(new Embedded(null,
                    new ThemeResource("vaadin.png")), 100, 2000, "1024px",
                    "248px", false, LazyLoadWrapper.MODE_LAZY_LOAD_DRAW));

            item.getItemProperty("image").setValue(vl);

        }

    }

}
