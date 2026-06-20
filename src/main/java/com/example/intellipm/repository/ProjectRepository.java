package com.example.intellipm.repository;

import com.example.intellipm.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Projets dont au moins une équipe contient un utilisateur spécifique (MEMBRE)
    @Query("SELECT DISTINCT p FROM Project p JOIN p.teams t JOIN t.users u WHERE u.id = :userId")
    List<Project> findProjectsByUserId(@Param("userId") Long userId);

    // Projets dont le chef de projet est l'utilisateur connecté (CHEF_PROJET)
    List<Project> findByChefProjetId(Long chefProjetId);
}