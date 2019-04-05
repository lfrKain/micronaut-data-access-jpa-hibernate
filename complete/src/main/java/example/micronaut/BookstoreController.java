package example.micronaut;

import example.micronaut.anotherdomain.Bookstore;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.validation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated // <1>
@Controller("/bookstores") // <2>
public class BookstoreController {

    protected final BookstoreRepository bookstoreRepository;

    public BookstoreController(BookstoreRepository bookstoreRepository) { // <3>
        this.bookstoreRepository = bookstoreRepository;
    }

    @Get(value = "/list{?args*}") // <9>
    public List<Bookstore> list(@Valid SortingAndOrderArguments args) {
        return bookstoreRepository.findAll(args);
    }
}
