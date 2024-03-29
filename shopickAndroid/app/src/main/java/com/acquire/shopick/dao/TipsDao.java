package com.acquire.shopick.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.acquire.shopick.dao.Tips;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TIPS".
*/
public class TipsDao extends AbstractDao<Tips, Long> {

    public static final String TABLENAME = "TIPS";

    /**
     * Properties of entity Tips.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Description = new Property(2, String.class, "description", false, "DESCRIPTION");
        public final static Property GlobalID = new Property(3, String.class, "globalID", false, "GLOBAL_ID");
        public final static Property Image_url = new Property(4, String.class, "image_url", false, "IMAGE_URL");
        public final static Property Category = new Property(5, String.class, "category", false, "CATEGORY");
        public final static Property Category_id = new Property(6, Long.class, "category_id", false, "CATEGORY_ID");
        public final static Property Brand = new Property(7, String.class, "brand", false, "BRAND");
        public final static Property Brand_id = new Property(8, Long.class, "brand_id", false, "BRAND_ID");
        public final static Property Liked = new Property(9, Boolean.class, "liked", false, "LIKED");
        public final static Property Dirty = new Property(10, Boolean.class, "dirty", false, "DIRTY");
    };


    public TipsDao(DaoConfig config) {
        super(config);
    }
    
    public TipsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TIPS\" (" + //
                "\"ID\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TITLE\" TEXT," + // 1: title
                "\"DESCRIPTION\" TEXT," + // 2: description
                "\"GLOBAL_ID\" TEXT," + // 3: globalID
                "\"IMAGE_URL\" TEXT," + // 4: image_url
                "\"CATEGORY\" TEXT," + // 5: category
                "\"CATEGORY_ID\" INTEGER," + // 6: category_id
                "\"BRAND\" TEXT," + // 7: brand
                "\"BRAND_ID\" INTEGER," + // 8: brand_id
                "\"LIKED\" INTEGER," + // 9: liked
                "\"DIRTY\" INTEGER);"); // 10: dirty
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TIPS\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Tips entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(3, description);
        }
 
        String globalID = entity.getGlobalID();
        if (globalID != null) {
            stmt.bindString(4, globalID);
        }
 
        String image_url = entity.getImage_url();
        if (image_url != null) {
            stmt.bindString(5, image_url);
        }
 
        String category = entity.getCategory();
        if (category != null) {
            stmt.bindString(6, category);
        }
 
        Long category_id = entity.getCategory_id();
        if (category_id != null) {
            stmt.bindLong(7, category_id);
        }
 
        String brand = entity.getBrand();
        if (brand != null) {
            stmt.bindString(8, brand);
        }
 
        Long brand_id = entity.getBrand_id();
        if (brand_id != null) {
            stmt.bindLong(9, brand_id);
        }
 
        Boolean liked = entity.getLiked();
        if (liked != null) {
            stmt.bindLong(10, liked ? 1L: 0L);
        }
 
        Boolean dirty = entity.getDirty();
        if (dirty != null) {
            stmt.bindLong(11, dirty ? 1L: 0L);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Tips readEntity(Cursor cursor, int offset) {
        Tips entity = new Tips( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // description
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // globalID
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // image_url
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // category
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // category_id
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // brand
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8), // brand_id
            cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0, // liked
            cursor.isNull(offset + 10) ? null : cursor.getShort(offset + 10) != 0 // dirty
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Tips entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDescription(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setGlobalID(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setImage_url(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCategory(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCategory_id(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setBrand(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setBrand_id(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
        entity.setLiked(cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0);
        entity.setDirty(cursor.isNull(offset + 10) ? null : cursor.getShort(offset + 10) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Tips entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Tips entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
