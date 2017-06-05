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

	private static final ContactService cs = new ContactService();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		BufferedReader reader = req.getReader();
		cs.saveContact(reader);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        String json = cs.getContacts(pathInfo);

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(json);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        cs.deleteContact(pathInfo);
	}
}
