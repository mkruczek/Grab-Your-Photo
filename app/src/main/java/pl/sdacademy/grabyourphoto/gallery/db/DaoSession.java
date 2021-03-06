package pl.sdacademy.grabyourphoto.gallery.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig imageDaoConfig;

    private final ImageDao imageDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        imageDaoConfig = daoConfigMap.get(ImageDao.class).clone();
        imageDaoConfig.initIdentityScope(type);

        imageDao = new ImageDao(imageDaoConfig, this);

        registerDao(Image.class, imageDao);
    }
    
    public void clear() {
        imageDaoConfig.clearIdentityScope();
    }

    public ImageDao getImageDao() {
        return imageDao;
    }

}
