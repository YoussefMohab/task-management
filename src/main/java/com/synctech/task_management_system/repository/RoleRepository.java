package com.synctech.task_management_system.repository;

import com.synctech.task_management_system.entity.Role;
import com.synctech.task_management_system.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleName name);
}
