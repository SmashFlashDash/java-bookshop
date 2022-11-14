package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.data.book.BookRepository;
import com.example.MyBookShopApp.data.TestEntity;
import com.example.MyBookShopApp.data.TestEntityCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class CommandLineRunnerImp implements CommandLineRunner {
    TestEntityCrudRepository testEntityCrudRepository;
    BookRepository bookRepository;
    @Autowired
    public CommandLineRunnerImp(TestEntityCrudRepository testEntityCrudRepository, BookRepository bookRepository) {
        this.testEntityCrudRepository = testEntityCrudRepository;
        this.bookRepository = bookRepository;
    }
    @Override
    public void run(String... args) throws Exception {
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
        entity.setData(entity.getClass().getSimpleName()+entity.hashCode());
        testEntityCrudRepository.save(entity);
    }
}
