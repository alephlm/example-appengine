package br.com.aurum.astrea.service;

import br.com.aurum.astrea.dao.ContactDao;
import br.com.aurum.astrea.domain.Contact;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class ContactService {

	private static final ContactDao DAO = new ContactDao();
	private static final Gson gson = new Gson();

    public String getContacts(String pathInfo){
        List<Contact> lstContact;

        if(pathInfo != null) {
            String[] pathParts = pathInfo.split("/");
            if(pathParts.length > 2){
                String param = pathParts[1];
                String finding = pathParts[2];
                lstContact = DAO.listBy(param, finding);
            } else {
                lstContact = new ArrayList<>();
            }
        } else {
            lstContact = DAO.list();
        }

        return gson.toJson(lstContact);
    }

    public void saveContact(BufferedReader reader){
        Contact contact = gson.fromJson(reader, Contact.class);
        DAO.save(contact);
    }

    public void deleteContact(String pathInfo){
        String[] pathParts = pathInfo.split("/");
        String id = pathParts[1];
        DAO.delete(Long.parseLong(id));
    }
}
