package com.example.MyBookShopApp.data;

import org.springframework.stereotype.Repository;

@Repository
public class TestEntityDao extends AbstractHibernateDao<TestEntity>{
    public TestEntityDao(){ //перопределим конструктор
        super();            // вызов коснтруктора
        setClazz(TestEntity.class); // установим тип
    }
}
