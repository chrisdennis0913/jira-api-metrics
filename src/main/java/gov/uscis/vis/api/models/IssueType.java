package gov.uscis.vis.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by cedennis on 1/30/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueType {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
