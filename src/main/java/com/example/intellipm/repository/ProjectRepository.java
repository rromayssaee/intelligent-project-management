package com.example.intellipm.repository;

import com.example.intellipm.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Projets dont l'équipe contient un utilisateur spécifique
    @Query("SELECT p FROM Project p WHERE p.team.id IN " +
            "(SELECT t.id FROM Team t JOIN t.users u WHERE u.id = :userId)")
    List<Project> findProjectsByUserId(@Param("userId") Long userId);
}