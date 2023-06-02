package com.intrimproj.ppmtool.exceptions;

import org.apache.coyote.Response;

public class ProjectNotFoundExceptionResponse {

    private String ProjectNotFound;
    public ProjectNotFoundExceptionResponse(String projectNotFound){

    }

    public String getProjectNotFound() {
        return ProjectNotFound;
    }

    public void setProjectNotFound(String projectNotFound) {
        ProjectNotFound = projectNotFound;
    }
}
