package com.intrimproj.ppmtool.services;

import com.intrimproj.ppmtool.domain.Backlog;
import com.intrimproj.ppmtool.domain.Project;
import com.intrimproj.ppmtool.exceptions.ProjectIdException;
import com.intrimproj.ppmtool.repositories.BacklogRepository;
import com.intrimproj.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository  backlogRepository;

    public Project saveOrUpdateProject(Project project){

        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if (project.getId()==null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            if (project.getId()!=null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project ID'"+ project.getProjectIdentifier().toUpperCase()+"'already exists");
        }

//        return projectRepository.save(project);

    }
    public Project findByIdentifier(String projectId){

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project==null){
            throw new ProjectIdException("Project ID'"+ projectId+"'does not exists");

        }

        return project;
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project==null){
            throw new ProjectIdException("Cannot delete Project with Id '"+projectId+"'this project does not exist");

        }
        projectRepository.delete(project);
    }
}
