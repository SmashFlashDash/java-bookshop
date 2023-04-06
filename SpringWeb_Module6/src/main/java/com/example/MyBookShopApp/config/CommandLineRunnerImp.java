package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.data.TestEntity;
import com.example.MyBookShopApp.data.TestEntityCrudRepository;
import com.example.MyBookShopApp.data.author.AuthorRepository;
import com.example.MyBookShopApp.data.book.BookRepository;
import com.example.MyBookShopApp.data.author.AuthorService;
import com.example.MyBookShopApp.data.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

@Configuration
public class CommandLineRunnerImp implements CommandLineRunner {
    TestEntityCrudRepository testEntityCrudRepository;
    BookRepository bookRepository;
    AuthorRepository authorRepository;
    BookService bookService;
    AuthorService authorService;
    EntityManagerFactory emf;

    @PersistenceContext
    EntityManager em;

    @Autowired
    public CommandLineRunnerImp(TestEntityCrudRepository testEntityCrudRepository,
                                BookRepository bookRepository,
                                AuthorRepository authorRepository,
                                BookService bookService,
                                AuthorService authorService,
                                EntityManagerFactory emf) {
        this.testEntityCrudRepository = testEntityCrudRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookService = bookService;
        this.authorService = authorService;
        this.emf = emf;
    }

    @Override
    public void run(String... args) throws Exception {
//        Optional<Book> book1 = bookRepository.findById(1);
//        Optional<Book> book2 = bookRepository.findById(2);
//        Optional<Author> author1 = authorRepository.findById(1);

//        Logger.getLogger(CommandLineRunnerImp.class.getSimpleName())
//                .info(book1.get().toString());
//        Logger.getLogger(CommandLineRunnerImp.class.getSimpleName())
//                .info(author1.get().toString());

//        SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
//        Session session = sessionFactory.getCurrentSession();
//        Session session = sessionFactory.openSession();
//        Session session = em.unwrap(Session.class);
//        Transaction tx = session.beginTransaction();
//        List<UserEntity> users  = session.createQuery("from UserEntity").getResultList();

//        System.out.println(author1.get());

//        for (int i=0; i<5; i++){
//            createTestEntity(new TestEntity());
//        }
//        TestEntity readTestEntity = readTestEntityById(3L);
//        if (readTestEntity != null) {
//            Logger.getLogger(CommandLineRunnerImp.class.getSimpleName()).info("read_" + readTestEntity.toString());
//        } else {
//            throw new NullPointerException();
//        }
//        TestEntity updateTestEntity = updateTestEntityById(5L);
//        if (updateTestEntity != null) {
//            Logger.getLogger(CommandLineRunnerImp.class.getSimpleName()).info("update_" + readTestEntity.toString());
//        } else {
//            throw new NullPointerException();
//        }
//
//        deleteTestEntityById(4L);
//
//        Logger.getLogger(CommandLineRunnerImp.class.getSimpleName())
//                .info(bookRepository.findBooksByAuthor_FirstName("Ken").toString());
//        Logger.getLogger(CommandLineRunnerImp.class.getSimpleName())
//                .info(bookRepository.customFindAllBooks().toString());
    }

    private void deleteTestEntityById(Long id) {
        TestEntity testEntity = testEntityCrudRepository.findById(id).get();
        testEntityCrudRepository.delete(testEntity);
    }

    private TestEntity updateTestEntityById(long id) {
        TestEntity testEntity = testEntityCrudRepository.findById(id).get();
        testEntity.setData("NEW DATA");
        testEntityCrudRepository.save(testEntity);
        return testEntity;
    }

    private TestEntity readTestEntityById(long id) {
        return testEntityCrudRepository.findById(id).get();
    }

    private void createTestEntity(TestEntity entity) {
        entity.setData(entity.getClass().getSimpleName() + entity.hashCode());
        testEntityCrudRepository.save(entity);
    }
}
