package gov.uscis.vis.api.service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uscis.vis.api.models.IssueList;
import gov.uscis.vis.api.models.SprintList;
import gov.uscis.vis.api.models.StateEnum;

import java.io.File;
import java.io.IOException;

/**
 * Created by cedennis on 2/9/17.
 */
public class StoryServiceSample implements StoryService{

    public SprintList getSprintList(Integer boardId){
        ObjectMapper mapper = new ObjectMapper();
        SprintList sampleSprintList = new SprintList();

        try {
            // Convert JSON string from file to Object
            sampleSprintList = mapper.readValue(new File("./src/main/resources/static/sampleSprintList.json"), SprintList.class);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sampleSprintList;
    }

    public SprintList getSprintListWithState(Integer boardId, StateEnum state){
        ObjectMapper mapper = new ObjectMapper();
        SprintList sampleSprintList = new SprintList();

        try {
            // Convert JSON string from file to Object
            sampleSprintList = mapper.readValue(new File("./src/main/resources/static/sampleSprintList.json"), SprintList.class);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sampleSprintList;
    }

    public IssueList getIssueListForSprint(Long latestCompletedSprintId) {
        ObjectMapper mapper = new ObjectMapper();
        IssueList sampleIssueList = new IssueList();

        try {
            // Convert JSON string from file to Object
            sampleIssueList = mapper.readValue(new File("./src/main/resources/static/sampleIssueList" + latestCompletedSprintId + ".json"), IssueList.class);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sampleIssueList;
    }
}
