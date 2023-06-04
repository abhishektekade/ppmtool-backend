package com.intrimproj.ppmtool.services;

import com.intrimproj.ppmtool.domain.Backlog;
import com.intrimproj.ppmtool.domain.Project;
import com.intrimproj.ppmtool.domain.ProjectTask;
import com.intrimproj.ppmtool.exceptions.ProjectNotFoundException;
import com.intrimproj.ppmtool.repositories.BacklogRepository;
import com.intrimproj.ppmtool.repositories.ProjectRepository;
import com.intrimproj.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){
            //project task to be added to a project and !=null
            Backlog backlog =  projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();      //backlogRepository.findByProjectIdentifier(projectIdentifier);


            //set the backlog to project task
            projectTask.setBacklog(backlog);
            Integer BacklogSequence = backlog.getPTSequence();
            //update the backlog sequence add to the sequence

            BacklogSequence++;

            backlog.setPTSequence(BacklogSequence);
            //Add Sequence to project Task
            projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);


            //Initial status is null
            if (projectTask.getStatus()==""||projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }
            //intial prrority of status
            if (projectTask.getPriority()==null||projectTask.getPriority()==0){
                projectTask.setPriority(3);
            }

            return projectTaskRepository.save(projectTask);

    }
        public Iterable<ProjectTask>findBacklogById(String id, String username){
            projectService.findProjectByIdentifier(id,username);
            return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }
    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){
        //make sure we are searching existing backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if (backlog == null){
            throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"'does not exist");
        }

        //make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if (projectTask == null){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"'not found");
        }
        if (!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project task '"+pt_id+"'does not exist in the project'"+backlog_id);
        }
        // make sure that the id in path corresponds to right project

        return projectTask;
    }
    public  ProjectTask updateByProjectSequence(ProjectTask updatedTask,String backlog_id,String pt_id ){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id);

        projectTask = updatedTask;
        return  projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id,String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id);

        projectTaskRepository.delete(projectTask);
    }
}
