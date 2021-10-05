package victor.training.oo.structural.adapter.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import victor.training.oo.structural.adapter.external.LdapUser;
import victor.training.oo.structural.adapter.external.LdapUserWebserviceClient;

@Slf4j
@Service
//User service java smells like a core/Domain Service, and should not be polluted with LDap Thing
public class UserService {
	@Autowired
	private LdapUserWebserviceClient wsClient;

	public void importUserFromLdap(String username) {

		new RuntimeException().printStackTrace(); //HOW to know if u were proxied ?!

		List<LdapUser> list = wsClient.search(username.toUpperCase(), null, null);
		if (list.size() != 1) {
			throw new IllegalArgumentException("There is no single user matching username " + username);
		}
		LdapUser ldapUser = list.get(0);
		String fullName = ldapUser.getfName() + " " + ldapUser.getlName().toUpperCase();
		User user = new User(ldapUser.getuId(), fullName, ldapUser.getWorkEmail());
		
		if (user.getWorkEmail() != null) {
			log.debug("Send welcome email to " + user.getWorkEmail());
		}
		log.debug("Insert user in my database");
	}
	
	public List<User> searchUserInLdap(String username) {
		List<LdapUser> list = wsClient.search(username.toUpperCase(), null, null);
		List<User> results = new ArrayList<>();
		for (LdapUser ldapUser : list) {
			String fullName = ldapUser.getfName() + " " + ldapUser.getlName().toUpperCase();
			User user = new User(ldapUser.getuId(), fullName, ldapUser.getWorkEmail());
			results.add(user);
		}
		return results;
	}
	
}
