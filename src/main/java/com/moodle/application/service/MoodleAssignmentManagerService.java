package com.moodle.application.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.moodle.application.dto.Assignment;
import com.moodle.application.dto.Assignments;
import com.moodle.application.dto.Courses;
import com.moodle.application.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MoodleAssignmentManagerService {

	private final UserRepository userRepository;

	private final AuthenticationService authenticationSerivce;

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

        Gson gson = new Gson();
        Courses courses = gson.fromJson(response.toString(), Courses.class);
        return courses;
	}

	public Assignment getSubmissionForAssignment(Long assignmentId) throws Exception {
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

        Gson gson = new Gson();
        Assignments assignment = gson.fromJson(response.toString(), Assignments.class);
        if (assignment == null) {
        	throw new Exception("No Assignments found");
        }
        
        assignment.getAssignments()
        	.stream()
        	
        	.filter(ass -> ass != null)
        	.map(ass -> ass.getSubmissions())
        	
        	.filter(sub -> sub != null)
        	.flatMap(sub -> sub.stream())
        	.map(sub -> sub.getPlugins())
        	
        	.filter(plug -> plug != null)
        	.flatMap(plug -> plug.stream())
        	.map(plug -> plug.getFileareas())
        	
        	.filter(area -> area != null)
        	.flatMap(area -> area.stream())
        	.map(area -> area.getFiles())
        	
        	.filter(file -> file != null)
        	.flatMap(file -> file.stream())
        	.forEach(file -> {
        		file.setFileurl(file.getFileurl() + "?token=" + token);
        	});

        return assignment.getAssignments().get(0);
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
