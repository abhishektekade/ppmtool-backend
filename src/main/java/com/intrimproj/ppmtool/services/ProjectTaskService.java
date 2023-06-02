package com.intrimproj.ppmtool.services;

import com.intrimproj.ppmtool.domain.Backlog;
import com.intrimproj.ppmtool.domain.Project;
import com.intrimproj.ppmtool.domain.ProjectTask;
import com.intrimproj.ppmtool.exceptions.ProjectNotFoundException;
import com.intrimproj.ppmtool.repositories.BacklogRepository;
import com.intrimproj.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){
        try {
            //project task to be added to a project and !=null
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);


            //set the backlog to project task
            projectTask.setBacklog(backlog);
            Integer BacklogSequence = backlog.getPTSequence();
            //update the backlog sequence add to the sequence

            BacklogSequence++;

            backlog.setPTSequence(BacklogSequence);
            //Add Sequence to project Task
            projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);
            //intial prrority of status
            if (projectTask.getPriority()==null){
                projectTask.setPriority(3);
            }
            //Initial status is null
            if (projectTask.getStatus()==""||projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }
            return projectTaskRepository.save(projectTask);
        }catch (Exception e){
            throw new ProjectNotFoundException("Project Not Found");
        }
    }
        public Iterable<ProjectTask>findBacklogById(String id){
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }
}
