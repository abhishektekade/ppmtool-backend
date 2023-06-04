package com.intrimproj.ppmtool.services;

import com.intrimproj.ppmtool.domain.Backlog;
import com.intrimproj.ppmtool.domain.Project;
import com.intrimproj.ppmtool.domain.User;
import com.intrimproj.ppmtool.exceptions.ProjectIdException;
import com.intrimproj.ppmtool.exceptions.ProjectNotFoundException;
import com.intrimproj.ppmtool.repositories.BacklogRepository;
import com.intrimproj.ppmtool.repositories.ProjectRepository;
import com.intrimproj.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository  backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username){

        try {
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
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
    public Project findProjectByIdentifier(String projectId, String username){

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project==null){
            throw new ProjectIdException("Project ID'"+ projectId+"'does not exists");

        }
        if (!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project Not Found in your account");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectid, String username){
//        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
//        if (project==null){
//            throw new ProjectIdException("Cannot delete Project with Id '"+projectId+"'this project does not exist");
//
//        }
        projectRepository.delete(findProjectByIdentifier(projectid, username));
    }
}
