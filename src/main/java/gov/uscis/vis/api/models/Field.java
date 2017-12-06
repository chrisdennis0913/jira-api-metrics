package gov.uscis.vis.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by cedennis on 1/30/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Field {
    private Resolution resolution;
    private IssueType issuetype;
    private Status status;
    private String customfield_10002;
    private String summary;
    private List<Sprint> closedSprints;

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public IssueType getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(IssueType issuetype) {
        this.issuetype = issuetype;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCustomfield_10002() {
        return customfield_10002;
    }

    public void setCustomfield_10002(String customfield_10002) {
        this.customfield_10002 = customfield_10002;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Sprint> getClosedSprints() {
        return closedSprints;
    }

    public void setClosedSprints(List<Sprint> closedSprints) {
        this.closedSprints = closedSprints;
    }
}
