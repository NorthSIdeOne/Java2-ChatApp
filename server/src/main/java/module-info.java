module server {

    requires lib;
    requires java.sql;
    requires java.persistence;
    requires org.hibernate.commons.annotations;
    requires org.hibernate.orm.core;
    requires java.rmi;

    opens server.model;

}