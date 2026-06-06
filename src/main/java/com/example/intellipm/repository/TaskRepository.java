package com.example.intellipm.repository;

import com.example.intellipm.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProjectId(Long projectId);

    List<Task> findByProjectIdAndAssignedUserId(Long projectId, Long userId);

    // Désassigner toutes les tâches d'un user dans les projets d'une équipe
    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.assignedUser = null, t.statut = 'CREEE' " +
            "WHERE t.assignedUser.id = :userId " +
            "AND t.project.id IN (SELECT p.id FROM Project p WHERE p.team.id = :teamId)")
    void desassignerTachesUserDansEquipe(@Param("userId") Long userId,
                                         @Param("teamId") Long teamId);
}