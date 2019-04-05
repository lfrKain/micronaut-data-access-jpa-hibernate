package example.micronaut;

import example.micronaut.anotherdomain.Bookstore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface BookstoreRepository {

    Optional<Bookstore> findById(@NotNull Long id);

    Bookstore save(@NotBlank String name);

    void deleteById(@NotNull Long id);

    List<Bookstore> findAll(@NotNull SortingAndOrderArguments args);

    int update(@NotNull Long id, @NotBlank String name);
}
