package br.com.aurum.astrea.service;

import br.com.aurum.astrea.domain.Contact;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cache.AsyncCacheFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ContactServiceTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private final ContactService contactService = new ContactService();
	private static final Gson gson = new Gson();
    private Closeable session;

    @BeforeClass
    public static void setUpBeforeClass() {
        ObjectifyService.setFactory(new ObjectifyFactory());
        ObjectifyService.register(Contact.class);
    }

    @Before
    public void setUp() throws Exception {
        this.session = ObjectifyService.begin();
        this.helper.setUp();
    }

    @After
    public void tearDown() throws Exception {
        AsyncCacheFilter.complete();
        this.session.close();
        this.helper.tearDown();
    }

    @Test
    public void getContacts() throws Exception {
        Contact contact = new Contact();
        contact.setName("yam");
        Contact contact2 = new Contact();
        contact2.setName("yem");

        ObjectifyService.ofy().save().entity(contact).now();
        ObjectifyService.ofy().save().entity(contact2).now();
        String contacts = contactService.getContacts(null);
        List<Contact> retrieved = gson.fromJson(contacts, List.class);
        System.out.println(retrieved);

        assertEquals(2, retrieved.size());
    }

    @Test
    public void saveContact() throws Exception {
        Contact contact = new Contact();
        contact.setName("jdoe");
        contactService.saveContact(contact);
        Contact retrieved = ObjectifyService.ofy().load().type(Contact.class).filter("name", "jdoe").first().now();

        assertNotNull(retrieved);
    }

    @Test
    public void deleteContact() throws Exception {
        Contact contact = new Contact();
        contact.setName("yum");
        ObjectifyService.ofy().save().entity(contact).now();
        Contact toDelete = ObjectifyService.ofy().load().type(Contact.class).filter("name", "yum").first().now();
        contactService.deleteContact("/" + toDelete.getId().toString());

        String contacts = contactService.getContacts(null);
        List<Contact> retrieved = gson.fromJson(contacts, List.class);

        assertEquals(0, retrieved.size());
    }

    @Test
    public void getContactByName() throws Exception {
        Contact contact = createDefault();
        doGetBy(contact, "name", contact.getName());

    }

    @Test
    public void getContactByCPF() throws Exception {
        Contact contact = createDefault();
        doGetBy(contact, "cpf", contact.getCpf());

    }

    @Test
    public void getContactByEmail() throws Exception {
        Contact contact = createDefault();
        doGetBy(contact, "emails", contact.getEmails().get(0));
    }

    @Test
    public void getUnindexedProperty() throws Exception {
        Contact contact = createDefault();
        ObjectifyService.ofy().save().entity(contact).now();
        String contacts = contactService.getContacts("/rg/" + contact.getRg());
        List<Contact> retrieved = gson.fromJson(contacts, List.class);
        System.out.println(retrieved);

        assertTrue(retrieved.size() == 0);
    }

    private Contact createDefault(){
        Contact contact = new Contact();
        contact.setName("yum");
        contact.setCpf("1234567890");
        contact.setRg("abc123");
        List<String> emails = new ArrayList<>();
        emails.add("mail1@mail.com");
        emails.add("mail2@mail.com");
        contact.setEmails(emails);
        return contact;
    }

    private void doGetBy(Contact contact, String param, String prop) {
        ObjectifyService.ofy().save().entity(contact).now();
        String contacts = contactService.getContacts("/" + param + "/" + prop);
        List<Contact> retrieved = gson.fromJson(contacts, List.class);
        System.out.println(retrieved);

        assertTrue(retrieved.size() > 0);
    }
}