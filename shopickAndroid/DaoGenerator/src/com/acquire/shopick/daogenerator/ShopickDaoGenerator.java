package com.acquire.shopick.daogenerator;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

import java.io.IOException;

/**
 * Created by yigit on 2/4/14.
 */
public class ShopickDaoGenerator extends DaoGenerator {
    public ShopickDaoGenerator() throws IOException {
    }

    public static void main(String[] args) {
        Schema schema = new Schema(8, "com.acquire.shopick.dao");
        schema.setDefaultJavaPackageTest("com.acquire.shopick.test.dao");
        schema.setDefaultJavaPackageDao("com.acquire.shopick.dao");
        schema.enableKeepSectionsByDefault();
        // Add user Entity
        addUser(schema);

        // Start Creating Categories Entity
        addTopCategories(schema);
        addCategories(schema);

        //Brand and store entity as they are dependent on categories
        addBrand(schema);
        addStore(schema);

        // Post is only depended on brand stores and categories
        Entity post = addPost(schema);

        // These  depends on all before
        addProduct(schema);
        addBrandUpdates(schema);
        addTips(schema);
        addRelatedProducts(schema);
        addPostCollection(schema, post);


        try {
            new DaoGenerator().generateAll(schema, "app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void addUser(Schema schema) {
        Entity user = schema.addEntity("User");
        user.addIntProperty("id").primaryKey();
        user.addStringProperty("email");
        user.addStringProperty("name");
        user.addStringProperty("profileImage");
        user.addStringProperty("coverImage");
        user.addIntProperty("gender");
        user.addStringProperty("instanceID");
        user.addStringProperty("usercode");
        user.addStringProperty("referred");
        user.addLongProperty("picks");
        user.addLongProperty("monthlyPicks");
        user.addStringProperty("authentication_token");
        user.addLongProperty("rank");
    }


    private static void addTopCategories(Schema schema) {
        Entity top_category = schema.addEntity("TopCategories");
        top_category.addLongProperty("id").primaryKey();
        top_category.addStringProperty("tag");
        top_category.addStringProperty("name");
        top_category.addStringProperty("category");
        top_category.addStringProperty("color");
        top_category.addStringProperty("image_url");
        top_category.addLongProperty("parent_category_id");
    }

    private static void addCategories(Schema schema) {
        Entity categories = schema.addEntity("Categories");
        categories.addLongProperty("id").primaryKey();
        categories.addStringProperty("tag");
        categories.addStringProperty("name");
        categories.addStringProperty("category");
        categories.addStringProperty("color");
        categories.addStringProperty("image_url");
        categories.addLongProperty("parent_category_id");
    }



    private static void addBrand(Schema schema) {
        Entity brand = schema.addEntity("Brands");
        brand.addLongProperty("id").primaryKey();
        brand.addStringProperty("name");
        brand.addStringProperty("logo_url");
        brand.addStringProperty("tagline");
        brand.addStringProperty("category");
        brand.addLongProperty("category_id");
        brand.addBooleanProperty("liked");
        brand.addBooleanProperty("dirty");

    }


    private static void addStore(Schema schema) {
        Entity store = schema.addEntity("Store");
        store.addLongProperty("id").primaryKey();
        store.addStringProperty("name");
        store.addFloatProperty("lat");
        store.addFloatProperty("lon");
        store.addLongProperty("brand_id");
        store.addStringProperty("phone");
        store.addStringProperty("email");
        store.addStringProperty("address");
        store.addStringProperty("brand_logo");
        store.addIntProperty("type");
        store.addStringProperty("category");
        store.addLongProperty("category_id");
        store.addLongProperty("location_id");
        store.addStringProperty("distance");
    }


    private static Entity addPost(Schema schema) {
        Entity post = schema.addEntity("Post");
        //post.implementsInterface("com.acquire.shopick.dao.CachesUIData");
        post.addLongProperty("localId").primaryKey().autoincrement();
        post.addLongProperty("id").unique();
        post.addStringProperty("globalID").unique().index();
        post.addStringProperty("localFileUri");
        post.addStringProperty("username");
        post.addStringProperty("user_image");
        post.addLongProperty("user_id");
        post.addStringProperty("storename");
        post.addLongProperty("store_id");
        post.addStringProperty("categoryname");
        post.addLongProperty("category_id");
        post.addStringProperty("brandname");
        post.addLongProperty("brand_id");
        post.addStringProperty("brand_logo");
        post.addStringProperty("image_url");
        post.addStringProperty("title");
        post.addStringProperty("description");
        post.addLongProperty("likes");
        post.addIntProperty("post_type");
        post.addIntProperty("order_in_category");
        post.addBooleanProperty("favorited");
        post.addBooleanProperty("isLocal");
        post.addStringProperty("category");
        post.addBooleanProperty("liked");
        post.addBooleanProperty("dirty");
        post.addStringProperty("featured_in_globalID");
        post.addStringProperty("featured_in_title");
        post.addBooleanProperty("read");
        return post;

    }




    private static void addProduct(Schema schema) {
        Entity product = schema.addEntity("Product");
        product.addLongProperty("id").primaryKey();
        product.addStringProperty("title");
        product.addStringProperty("description");
        product.addStringProperty("globalID");
        product.addStringProperty("image_url");
        product.addStringProperty("category");
        product.addLongProperty("category_id");
        product.addStringProperty("brand");
        product.addLongProperty("brand_id");
        product.addBooleanProperty("liked");
        product.addBooleanProperty("dirty");
        product.addLongProperty("mrp");
        product.addLongProperty("discount");
    }


    private static void addTips(Schema schema) {
        Entity tips = schema.addEntity("Tips");
        tips.addLongProperty("id").primaryKey();
        tips.addStringProperty("title");
        tips.addStringProperty("description");
        tips.addStringProperty("globalID");
        tips.addStringProperty("image_url");
        tips.addStringProperty("category");
        tips.addLongProperty("category_id");
        tips.addStringProperty("brand");
        tips.addLongProperty("brand_id");
        tips.addBooleanProperty("liked");
        tips.addBooleanProperty("dirty");
    }



    private static void addBrandUpdates(Schema schema) {
        Entity brandUpdates = schema.addEntity("BrandUpdates");
        brandUpdates.addLongProperty("id").primaryKey();
        brandUpdates.addStringProperty("title");
        brandUpdates.addStringProperty("description");
        brandUpdates.addStringProperty("globalID");
        brandUpdates.addStringProperty("image_url");
        brandUpdates.addStringProperty("category");
        brandUpdates.addLongProperty("category_id");
        brandUpdates.addStringProperty("brand");
        brandUpdates.addStringProperty("brand_name");
        brandUpdates.addLongProperty("brand_id");
        brandUpdates.addLongProperty("updatetype");
        brandUpdates.addBooleanProperty("liked");
        brandUpdates.addBooleanProperty("dirty");
    }



    private static void addPostCollection(Schema schema, Entity pos) {
        Entity postCollection = schema.addEntity("PostCollection");
        postCollection.addLongProperty("id").primaryKey();
        Property.PropertyBuilder pc_id = postCollection.addLongProperty("post_collection_id");
        postCollection.addStringProperty("title");
        postCollection.addStringProperty("description");
        postCollection.addStringProperty("globalID");
        postCollection.addStringProperty("post_banner");
        postCollection.addBooleanProperty("featured");
        postCollection.addStringProperty("brand_name");
        postCollection.addStringProperty("brand_logo");
        postCollection.addStringProperty("brand_id");
        postCollection.addBooleanProperty("liked");
        postCollection.addToMany(pos, pc_id.getProperty(), "posts");

    }


    private static void addRelatedProducts(Schema schema) {
        Entity relatedProducts = schema.addEntity("RelatedProducts");
        relatedProducts.addLongProperty("id").primaryKey();
        relatedProducts.addStringProperty("globalID");
        relatedProducts.addStringProperty("productglobalID");
    }

}
