package com.example.MyBookShopApp.data;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;

@Repository     // для котейнера spring то что класс bin
public abstract class AbstractHibernateDao<T> {
    // <T> - generic тип потом поменяем на конкретные типы
    @Autowired  // injecttion
    EntityManagerFactory entityManagerFactory;

    private Class<T> clazz;     // поле с типом Class genetic

    public void setClazz(Class<T> clazz) { // метод сеттер для clazz
        this.clazz = clazz;
    }

    public T findOne(Long id) { // метод который ищет generic T
        // тоесть ищет какой-то тип сущности в БД
        return getSession().find(clazz, id);
    }

    public Session getSession(){
        // метод получить экземпляр обьекта session
        return entityManagerFactory.createEntityManager().unwrap(Session.class);
    }
}
