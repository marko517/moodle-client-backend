package com.moodle.application.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.moodle.application.dto.Assignments;
import com.moodle.application.dto.Courses;
import com.moodle.application.repository.UserRepository;

@Service
public class MoodleAssignmentManagerService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationService authenticationSerivce;

	public Courses getAssignmentsForCourse(Long courseId) throws MalformedURLException, IOException {
		String token = getCurrentLoggedInUserMoodleTokenId();
        String domainName = "http://localhost/moodle";

        String restformat = "json";

        if (restformat.equals("json")) {
            restformat = "&moodlewsrestformat=" + restformat;
        } else {
            restformat = "";
        }

        String functionName = "mod_assign_get_assignments";
        String urlParameters = "courseids[0]=" + courseId.toString() + "&includenotenrolledcourses=1";

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
        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        System.out.println(response.toString());
        Gson gson = new Gson();
        Courses courses = gson.fromJson(response.toString(), Courses.class);
        return courses;
	}

	public Assignments getSubmissionForAssignment(Long assignmentId) throws Exception {
		String token = getCurrentLoggedInUserMoodleTokenId();
        String domainName = "http://localhost/moodle";

        String restformat = "json";

        if (restformat.equals("json")) {
            restformat = "&moodlewsrestformat=" + restformat;
        } else {
            restformat = "";
        }

        String functionName = "mod_assign_get_submissions";
        String urlParameters = "assignmentids[0]=" + assignmentId.toString();

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
        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        System.out.println(response.toString());
        Gson gson = new Gson();
        Assignments assignment = gson.fromJson(response.toString(), Assignments.class);
        return assignment;
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
