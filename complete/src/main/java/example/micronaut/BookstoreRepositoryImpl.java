package example.micronaut;

import example.micronaut.anotherdomain.Bookstore;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton // <1>
public class BookstoreRepositoryImpl implements BookstoreRepository {

    @PersistenceContext(name = "another")
    private EntityManager entityManager;  // <2>
    private final ApplicationConfiguration applicationConfiguration;

    public BookstoreRepositoryImpl(@CurrentSession EntityManager entityManager,
                                   ApplicationConfiguration applicationConfiguration) { // <2>
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    @Transactional(readOnly = true, transactionManager = "another") // <3>
    public Optional<Bookstore> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(Bookstore.class, id));
    }

    @Override
    @Transactional("another") // <4>
    public Bookstore save(@NotBlank String name) {
        Bookstore bookstore = new Bookstore(name);
        entityManager.persist(bookstore);
        return bookstore;
    }

    @Override
    @Transactional("another")
    public void deleteById(@NotNull Long id) {
        findById(id).ifPresent(bookstore -> entityManager.remove(bookstore));
    }

    private final static List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "name");

    @Transactional(readOnly = true, transactionManager = "another")
    public List<Bookstore> findAll(@NotNull SortingAndOrderArguments args) {
        String qlString = "SELECT g FROM Bookstore as g";
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
                qlString += " ORDER BY g." + args.getSort().get() + " " + args.getOrder().get().toLowerCase();
        }
        TypedQuery<Bookstore> query = entityManager.createQuery(qlString, Bookstore.class);
        query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
        args.getOffset().ifPresent(query::setFirstResult);

        return query.getResultList();
    }

    @Override
    @Transactional("another")
    public int update(@NotNull Long id, @NotBlank String name) {
        return entityManager.createQuery("UPDATE Bookstore x SET name = :name where id = :id")
                .setParameter("name", name)
                .setParameter("id", id)
                .executeUpdate();
    }
}
