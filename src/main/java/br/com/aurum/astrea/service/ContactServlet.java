package br.com.aurum.astrea.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.aurum.astrea.dao.ContactDao;
import br.com.aurum.astrea.domain.Contact;
import com.google.appengine.repackaged.com.google.gson.Gson;

@SuppressWarnings("serial")
public class ContactServlet extends HttpServlet {
	
	private static final ContactDao DAO = new ContactDao();
	private static final Gson gson = new Gson();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		BufferedReader reader = req.getReader();
		Contact contact = gson.fromJson(reader, Contact.class);

		DAO.save(contact);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
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

		String json = gson.toJson(lstContact);

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(json);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo(); // /{value}/test
        String[] pathParts = pathInfo.split("/");
        String id = pathParts[1];
        DAO.delete(Long.parseLong(id));
	}
}
