package com.moodle.application.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moodle.application.dto.MoodleUsers;
import com.moodle.application.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MoodleUserManagementService {

	private final AuthenticationService authenticationSerivce;

	private final UserRepository userRepository;

	public MoodleUsers getUsersInfo() throws IOException {
		String token = getCurrentLoggedInUserMoodleTokenId();
		String domainName = "http://localhost/moodle";

		String restformat = "json";
		       
		if (restformat.equals("json")) {
		   restformat = "&moodlewsrestformat=" + restformat;
		} 

		else {
		    restformat = "";
		}
		        
		String functionName = "core_user_get_users";
		        
		String urlParameters = "criteria[0][key]=email&criteria[0][value]=%%";        
		              
		String serverurl = domainName + "/webservice/rest/server.php" + "?wstoken=" + token + "&wsfunction=" + functionName + restformat;
		        
		HttpURLConnection connection = (HttpURLConnection) new URL(serverurl).openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type",
		"application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Language", "en-US");
		connection.setDoOutput(true);
		connection.setUseCaches (false);
		connection.setDoInput(true);
		DataOutputStream wr = new DataOutputStream (
		connection.getOutputStream ());
		wr.writeBytes (urlParameters);
		wr.flush ();
		wr.close ();

		InputStream inputStream = connection.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		StringBuilder response = new StringBuilder();
		while ((line = bufferedReader.readLine()) != null) {
		       response.append(line);
		       response.append('\r');
		   }
		        bufferedReader.close();
		        System.out.println(response.toString());

		        GsonBuilder builder = new GsonBuilder();
		        Gson gson = builder.create();

		        String userJson = response.toString();
		        MoodleUsers moodleUsers = gson.fromJson(userJson, MoodleUsers.class);

		        return moodleUsers;
	}

	public MoodleUsers getUserInfo(Long userId) throws IOException {
		String token = getCurrentLoggedInUserMoodleTokenId();
		String domainName = "http://localhost/moodle";

		String restformat = "json";
		       
		if (restformat.equals("json")) {
		   restformat = "&moodlewsrestformat=" + restformat;
		} 

		else {
		    restformat = "";
		}
		        
		String functionName = "core_user_get_users";
		        
		String urlParameters = "criteria[0][key]=id&criteria[0][value]=" + userId.toString();        
		              
		String serverurl = domainName + "/webservice/rest/server.php" + "?wstoken=" + token + "&wsfunction=" + functionName + restformat;
		        
		HttpURLConnection connection = (HttpURLConnection) new URL(serverurl).openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type",
		"application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Language", "en-US");
		connection.setDoOutput(true);
		connection.setUseCaches (false);
		connection.setDoInput(true);
		DataOutputStream wr = new DataOutputStream (
		connection.getOutputStream ());
		wr.writeBytes (urlParameters);
		wr.flush ();
		wr.close ();

		InputStream inputStream = connection.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		StringBuilder response = new StringBuilder();
		while ((line = bufferedReader.readLine()) != null) {
		       response.append(line);
		       response.append('\r');
		   }
		        bufferedReader.close();
		        System.out.println(response.toString());

		        GsonBuilder builder = new GsonBuilder();
		        Gson gson = builder.create();

		        String userJson = response.toString();
		        MoodleUsers moodleUsers = gson.fromJson(userJson, MoodleUsers.class);

		        return moodleUsers;
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
