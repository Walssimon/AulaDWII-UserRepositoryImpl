package br.com.senacsp.tads.stads4ma.library.domainmodel.repository;

import br.com.senacsp.tads.stads4ma.library.domainmodel.QPost;
import br.com.senacsp.tads.stads4ma.library.domainmodel.QProfile;
import br.com.senacsp.tads.stads4ma.library.domainmodel.QUser;
import br.com.senacsp.tads.stads4ma.library.domainmodel.User;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    private final QUser user = QUser.user;
    private final QPost post = QPost.post;

    public Optional<User> findByIdWithProfileAndPostsCriteria(UUID id){
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        Fetch<User,?> profileFetch = root.fetch("profile", JoinType.LEFT);
        Fetch<User,?> postsFetch = root.fetch("posts", JoinType.LEFT);

        criteriaQuery.select(root)
                .distinct(true)
                .where(builder.equal(root.get("id"), id));

        TypedQuery query = entityManager.createQuery(criteriaQuery);
        List<User> resultset = query.getResultList();
        return resultset.stream().findFirst();
    }

    public List<User> findByPostsAndNameLikeCriteria(int minPosts, String namePart){
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        criteriaQuery.select(root)
                .where(
                        builder.and(
                        builder.greaterThanOrEqualTo(
                                builder.size(root.get("posts")), minPosts),
        (builder.like(
                builder.lower(root.get("name")),
                "%" + namePart.toLowerCase() + "%"))
                )
                        )
                                .orderBy(builder.asc(root.get("name")));

     return entityManager.createQuery(criteriaQuery).getResultList();

    }

    private JPAQueryFactory qf(){
        return new JPAQueryFactory(this.entityManager);
    }

    public Optional<User> findByIdWithProfileAndPostsQueryDsl(UUID id){
        QUser u = QUser.user;
        QProfile p = QProfile.profile;
        QPost post = QPost.post;


        return this.qf().selectFrom(u)
                .leftJoin(u.profile, p).fetchJoin()
                .leftJoin(u.posts, post).fetchJoin()
                .where(u.id.eq(id))
                .distinct()
                .fetch()
                .stream().findFirst();
    }

    public List<User> findByPostsAndNameLikeQueryDsl(int minPosts, String namePart){

     return qf()
             .select(user)
             .from(user)
             .leftJoin(user.posts, post)
             .where(user.name.containsIgnoreCase(namePart))
             .groupBy(user.id)
             .having(post.id.count().goe(minPosts))
             .orderBy(user.name.asc())
             .fetch();
    }


}
