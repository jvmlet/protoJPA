package com.lognet.protojpa.test;

import com.lognet.protojpa.DemoMain;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.*;

import java.io.IOException;

/**
 * Created by alexf on 2/27/14.
 */
public class SimpleCase {

    private static  SessionFactory sessionFactory;
    private Session session;
    @BeforeClass
    public static void bootstrapHibernate(){
        Configuration cfg =  new Configuration().configure();
        final ServiceRegistryBuilder builder = new ServiceRegistryBuilder().applySettings(cfg.getProperties());
        sessionFactory = cfg.buildSessionFactory(builder.buildServiceRegistry());

    }
    @AfterClass
    public static void shutdownHibernate(){
        sessionFactory.close();
    }

    @Before
    public void setup(){
        session = sessionFactory.openSession();
    }
    @After
    public void tearDown(){
        session.close();
    }

    @Test
    public void enumerateTest(){

        DemoMain.enumerateCustomers(session);
    }

    @Test
    public void serializeTest() throws IOException {
        DemoMain.serializeTest(session);
    }

}
