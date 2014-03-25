package com.lognet.protojpa;

import com.lognet.protojpa.testdb.TestDB;
import db.entities.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by alexf on 2/27/14.
 */
public class DemoMain {
    private static Logger log = LoggerFactory.getLogger(DemoMain.class);
    public static void main(String[] args) {

        SessionFactory sessionFactory = null;
        Session session = null;
        try {
            TestDB.start();
            Configuration cfg =  new Configuration().configure();
            final ServiceRegistryBuilder builder = new ServiceRegistryBuilder().applySettings(cfg.getProperties());

            sessionFactory = cfg.buildSessionFactory(builder.buildServiceRegistry());

            session = sessionFactory.openSession();
            enumerateCustomers(session);
            serializeTest(session);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null!=session){
                session.close();
            }
            if(null!=sessionFactory){
                sessionFactory.close();
            }
            TestDB.stop();
        }
    }
    public static void enumerateCustomers(Session session){
        List<Customer> customers= Collections.checkedList(session.createCriteria(Customer.class).list(), Customer.class);
//        for(Customer customer:customers){
//            final db.entities.Demo.Customer protoCustomer = customer.toProtoMessage();
//            log.info(String.format("Customer's %s invoice list:",protoCustomer.getFirstname()));
//            for(db.entities.Demo.Invoice invoice:protoCustomer.getInvoicesList()){
//                log.info(String.format("\tInvoice id %d, total %d", invoice.getId(), invoice.getTotal()));
//                for(db.entities.Demo.Item item :invoice.getItemsList()){
//                    log.info(String.format("\t\tProduct %s, price %d", item.getProduct().getName(), item.getProduct().getPrice()));
//                }
//
//            }
//        }
    }
    public static void serializeTest(Session session) throws IOException {
//        final List customers = session.createCriteria(Customer.class).list();
//        if(customers.size()>0){
//            Customer customer = (Customer) customers.get(0);
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            final Demo.Customer demoCustomer = customer.toProtoMessage();
//            demoCustomer.writeTo(stream);
//            final Demo.Customer demoCustomerRestored = Demo.Customer.parseFrom(stream.toByteArray());
//            Assert.assertEquals(demoCustomer, demoCustomerRestored);
//
//        }
    }
}
