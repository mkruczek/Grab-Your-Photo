package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyGenerator {

    public static void main(String[] args) {

        Schema schema = new Schema(1, "pl.sdacademy.grabyourphoto.gallery.db");
        schema.enableKeepSectionsByDefault();

        addTable(schema);

        try {
            new DaoGenerator().generateAll(schema, "./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void addTable (Schema schema){
        Entity image = schema.addEntity("Image");
        image.addIdProperty().primaryKey().autoincrement();
        image.addStringProperty("resources");
    }
}
