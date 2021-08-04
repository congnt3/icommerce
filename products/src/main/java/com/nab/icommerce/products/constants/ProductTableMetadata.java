package com.nab.icommerce.products.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class storing all constant values of table iCommerce-Products
 */
public class ProductTableMetadata {
    private ProductTableMetadata() {
    }

    public static final String TABLE_NAME = "iCommerce-Products";

    public static final String FIELD_CATEGORY = "category";

    public static final String FIELD_PRICE = "price";

    public static final String FIELD_NAME = "name";

    public static final String FIELD_COLOUR = "colour";

    public static final String FIELD_BRAND = "brand";

    public static final String FIELD_IMAGE_PATH = "image_path";

    public static final String FIELD_UUID = "uuid";

    public static final String GIS_BY_CATEGORY = "GSI_By_Category";

    public static final String GIS_BY_PRICE = "GSI_By_Price";

    public static final String GIS_BY_BRAND = "GSI_By_Brand";

    public static final String GIS_BY_COLOUR = "GSI_By_Colour";

    private static final Map<String, String> fieldToGSIMap = new HashMap<>();

    private static final Map<String, String> fieldTypeMap = new HashMap<>();


    static {
        fieldToGSIMap.put(FIELD_CATEGORY, GIS_BY_CATEGORY);
        fieldToGSIMap.put(FIELD_BRAND, GIS_BY_BRAND);
        fieldToGSIMap.put(FIELD_COLOUR, GIS_BY_COLOUR);

        fieldTypeMap.put(FIELD_CATEGORY, "S");
        fieldTypeMap.put(FIELD_BRAND, "S");
        fieldTypeMap.put(FIELD_COLOUR, "S");
        fieldTypeMap.put(FIELD_PRICE, "N");
        fieldTypeMap.put(FIELD_NAME, "S");
        fieldTypeMap.put(FIELD_IMAGE_PATH, "S");
        fieldTypeMap.put(FIELD_UUID, "S");

    }

    public static Map<String, String> getFieldToGSIMap() {
        return Collections.unmodifiableMap(fieldToGSIMap);
    }


    public static Map<String, String> getFieldTypeMap() {
        return Collections.unmodifiableMap(fieldTypeMap);
    }
}
