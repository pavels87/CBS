package com.cbs.schema;

import com.cbs.model.domain.DbVersion;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.envers.tools.hbm2ddl.EnversSchemaGenerator;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SchemaExporter {

    public static final String SCHEMA_FILE_PATH = "dbscripts/schema/schema.sql";
    public static final Class<? extends Dialect> DIALECT_CLASS = PostgreSQL9Dialect.class;

    /**
     * Working folder should reference root folder of project upon launching
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new SchemaExporter().exportSchema();
    }

    public SchemaExporter() {
    }

    public void exportSchema() throws Exception {
        Configuration configuration = createConfiguration();
        configureDialect(configuration, DIALECT_CLASS);
        configureAudit(configuration);
        generateEntityAndAuditTables(configuration);
    }

    private void configureAudit(Configuration configuration) {
        configuration.setProperty("org.hibernate.envers.audit_table_prefix", "AUD_");
        configuration.setProperty("org.hibernate.envers.audit_table_suffix", "");
    }

    private void configureDialect(Configuration cfg, Class<? extends Dialect> dialectClass) {
        cfg.setProperty("hibernate.dialect", dialectClass.getName());
        cfg.setProperty("hibernate.default_schema", "public");
    }

    private void generateEntityAndAuditTables(Configuration cfg) {
        SchemaExport export = new EnversSchemaGenerator(cfg).export();
        export.setDelimiter(";");
        export.setOutputFile(SCHEMA_FILE_PATH);

        export.execute(true, false, false, true);
    }

    private <T> List<T> toList(Iterator<T> it, Class<T> clazz) {
        ArrayList<T> list = new ArrayList<T>();
        if (it != null && it.hasNext()) {
            while (it.hasNext()) {
                list.add(it.next());
            }
        }
        return list;
    }

    private Configuration createConfiguration() throws Exception {
        Configuration cfg = new Configuration();
        cfg.setProperty("hibernate.hbm2ddl.auto", "create");

        for (Class clazz : getClasses(DbVersion.class.getPackage().getName())) {
            cfg.addAnnotatedClass(clazz);
        }
        return cfg;
    }


    private List<Class> getClasses(String packageName) throws Exception {
        List<Class> classes = new ArrayList<Class>();
        File directory = null;
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            String path = packageName.replace('.', '/');
            URL resource = cld.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("No resource for " + path);
            }
            directory = new File(resource.getFile());
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(packageName + " (" + directory
                    + ") does not appear to be a valid package");
        }
        if (directory.exists()) {
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                if (files[i].endsWith(".class")) {
                    // removes the .class extension
                    classes.add(Class.forName(packageName + '.'
                            + files[i].substring(0, files[i].length() - 6)));
                }
            }
        } else {
            throw new ClassNotFoundException(packageName
                    + " is not a valid package");
        }

        return classes;
    }
}
