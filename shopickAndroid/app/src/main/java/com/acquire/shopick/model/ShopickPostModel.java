package com.acquire.shopick.model;

import com.acquire.shopick.Config;
import com.acquire.shopick.dao.DaoSession;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.dao.PostDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.Query;

/**
 * Created by yigit on 2/4/14.
 */
@Singleton
public class ShopickPostModel {
    private final PostDao postDao;
    private final DaoSession daoSession;
    private final Query<Post> userPostsQuery;
    private final Query<Post> typePostsQuery;
    private final Query<Post> brandPostsQuery;
    private final Query<Post> categoryPostsQuery;

    private final DeleteQuery<Post> deleteByStringId;
    private final Query<Post> findByStringIdQuery;

    @Inject
    public ShopickPostModel(DaoSession daoSession) {
        this.daoSession = daoSession;
        postDao = daoSession.getPostDao();
        userPostsQuery = postDao.queryBuilder().where(
                PostDao.Properties.User_id.eq("x")
        ).orderDesc(PostDao.Properties.Id).build();

        typePostsQuery = postDao.queryBuilder().where(
                PostDao.Properties.Post_type.eq("x")
        ).orderDesc(PostDao.Properties.Id).build();

        brandPostsQuery = postDao.queryBuilder().where(
                PostDao.Properties.Brand_id.eq("x")
        ).orderDesc(PostDao.Properties.Id).build();

        categoryPostsQuery = postDao.queryBuilder().where(
                PostDao.Properties.Category_id.eq("x")
        ).orderDesc(PostDao.Properties.Id).build();


        deleteByStringId = postDao.queryBuilder().where(
                PostDao.Properties.Id.eq("x")
        ).buildDelete();
        findByStringIdQuery = postDao.queryBuilder().where(
                PostDao.Properties.GlobalID.eq("x")
        ).build();
    }



    public List<Post> getAllPosts() {
       return postDao.queryBuilder().limit(Config.MAX_POST_RESULTS).orderDesc(PostDao.Properties.Id).list();
    }

    public List<Post> getTypePosts(int type_post) {
        if (type_post != -1) {
            Query<Post> typePostQuery = typePostsQuery.forCurrentThread();
            typePostQuery.setParameter(0, type_post);
            return typePostQuery.list();
        }
        return getAllPosts();
    }

    public List<Post> getBrandPosts(Long brand_id) {
        if (brand_id != -1) {
            Query<Post> brandPostQuery = brandPostsQuery.forCurrentThread();
            brandPostQuery.setParameter(0, brand_id);
            return brandPostQuery.list();
        }
        return getAllPosts();
    }


    public List<Post> getCategoryPosts(Long category_id) {
        if (category_id != -1) {
            Query<Post> categoryPostQuery = categoryPostsQuery.forCurrentThread();
            categoryPostQuery.setParameter(0, category_id);
            return categoryPostQuery.list();
        }
        return getAllPosts();
    }


    public void deleteById(String id) {
        synchronized (deleteByStringId) {
            DeleteQuery<Post> deleteQuery = deleteByStringId.forCurrentThread();
            deleteQuery.setParameter(0, id);
            deleteQuery.executeDeleteWithoutDetachingEntities();
        }
    }

    public List<Post> getUserPosts(Long user_id) {
        if (user_id != -1) {
            Query<Post> postQuery = userPostsQuery.forCurrentThread();
            postQuery.setParameter(0, user_id);
            return postQuery.list();
        }
        return null;
    }

    public void savePost(Post post) {
        if(post != null) {
            postDao.insertOrReplace(post);
        }
    }

    public void savePosts(final List<Post> posts) {
        if(posts != null) {
            postDao.insertOrReplaceInTx(posts);
        }
    }

    public Post load(String id) {
        synchronized (findByStringIdQuery) {
            Query<Post> newQuery = findByStringIdQuery.forCurrentThread();
            newQuery.setParameter(0, id);
            return newQuery.unique();
        }
    }

    public void delete(Post tweet) {
        postDao.delete(tweet);
    }
}
