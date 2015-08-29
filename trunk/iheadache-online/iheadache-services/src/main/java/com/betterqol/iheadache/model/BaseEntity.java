package com.betterqol.iheadache.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class BaseEntity {

	private String id;
    private String revision;
    
    @JsonProperty("_id")
    public String getId() {
            return id;
    }

    @JsonProperty("_id")
    public void setId(String s) {
            id = s;
    }

    @JsonProperty("_rev")
    public String getRevision() {
            return revision;
    }

    @JsonProperty("_rev")
    public void setRevision(String s) {
            revision = s;
    }

}
