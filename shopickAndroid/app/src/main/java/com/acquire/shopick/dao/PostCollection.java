package com.acquire.shopick.dao;

import java.util.List;
import com.acquire.shopick.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "POST_COLLECTION".
 */
public class PostCollection {

    private Long id;
    private Long post_collection_id;
    private String title;
    private String description;
    private String globalID;
    private String post_banner;
    private Boolean featured;
    private String brand_name;
    private String brand_logo;
    private String brand_id;
    private Boolean liked;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PostCollectionDao myDao;

    private List<Post> posts;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public PostCollection() {
    }

    public PostCollection(Long id) {
        this.id = id;
    }

    public PostCollection(Long id, Long post_collection_id, String title, String description, String globalID, String post_banner, Boolean featured, String brand_name, String brand_logo, String brand_id, Boolean liked) {
        this.id = id;
        this.post_collection_id = post_collection_id;
        this.title = title;
        this.description = description;
        this.globalID = globalID;
        this.post_banner = post_banner;
        this.featured = featured;
        this.brand_name = brand_name;
        this.brand_logo = brand_logo;
        this.brand_id = brand_id;
        this.liked = liked;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPostCollectionDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPost_collection_id() {
        return post_collection_id;
    }

    public void setPost_collection_id(Long post_collection_id) {
        this.post_collection_id = post_collection_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGlobalID() {
        return globalID;
    }

    public void setGlobalID(String globalID) {
        this.globalID = globalID;
    }

    public String getPost_banner() {
        return post_banner;
    }

    public void setPost_banner(String post_banner) {
        this.post_banner = post_banner;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_logo() {
        return brand_logo;
    }

    public void setBrand_logo(String brand_logo) {
        this.brand_logo = brand_logo;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Post> getPosts() {
        if (posts == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PostDao targetDao = daoSession.getPostDao();
            List<Post> postsNew = targetDao._queryPostCollection_Posts(id);
            synchronized (this) {
                if(posts == null) {
                    posts = postsNew;
                }
            }
        }
        return posts;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetPosts() {
        posts = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Post> getPostListCustom() {
        return posts;
    }
    // KEEP METHODS END

}