package gov.uscis.vis.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by cedennis on 1/30/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueList {
    private int maxResults;
    private int total;
    private List<Issue> issues;

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    @Override
    public String toString() {
        return "IssueList{" +
                "maxResults=" + maxResults +
                ", total=" + total +
                ", issues=" + issues +
                '}';
    }
}
