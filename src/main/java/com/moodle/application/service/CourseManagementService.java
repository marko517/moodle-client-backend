package com.moodle.application.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.moodle.application.dto.Course;
import com.moodle.application.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseManagementService {

	private final AuthenticationService authenticationSerivce;

	private final UserRepository userRepository;

	public List<Course> getCourseInformation() throws Exception {
		String token = getCurrentLoggedInUserMoodleTokenId();
		String domainName = "http://localhost/moodle";

		String restformat = "json";
		       
		if (restformat.equals("json")) {
		   restformat = "&moodlewsrestformat=" + restformat;
		} 

		else {
		    restformat = "";
		}
		        
		String functionName = "core_course_get_courses";
		        
		String urlParameters = "";       
		              
		String serverurl = domainName + "/webservice/rest/server.php" + "?wstoken=" + token + "&wsfunction=" + functionName + restformat;
		        
		HttpURLConnection con = (HttpURLConnection) new URL(serverurl).openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type",
		"application/x-www-form-urlencoded");
		con.setRequestProperty("Content-Language", "en-US");
		con.setDoOutput(true);
		con.setUseCaches (false);
		con.setDoInput(true);
		DataOutputStream wr = new DataOutputStream (
		con.getOutputStream ());
		wr.writeBytes (urlParameters);
		wr.flush ();
		wr.close ();

		InputStream is =con.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuilder response = new StringBuilder();
		while((line = rd.readLine()) != null) 
		   {
		       response.append(line);
		       response.append('\r');
		   }
		        rd.close();
		        System.out.println(response.toString());


		        GsonBuilder builder = new GsonBuilder();
		        Gson gson = builder.create();

		        Type collectionType = new TypeToken<Collection<Course>>() {}.getType();
		        List<Course> coursesCollection = gson.fromJson(response.toString(), collectionType);


		return coursesCollection;
	}

	private String getCurrentLoggedInUserMoodleTokenId() {
		User loggedInUser = getLoggedInUser();
		String username = loggedInUser.getUsername();

		com.moodle.application.model.User user = userRepository.findByUsername(username).orElseThrow(() ->
		new UsernameNotFoundException("No user found"));
		
		return user.getTokenId();
	}

	private User getLoggedInUser() {
		User loggedInUser = authenticationSerivce.getCurrentLoggedInUser().orElseThrow(
				()-> new IllegalArgumentException("There is no logged in user"));
		return loggedInUser;
	}
}
