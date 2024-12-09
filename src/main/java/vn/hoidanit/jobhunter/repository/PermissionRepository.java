package vn.hoidanit.jobhunter.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsById(Long id);
    boolean existsByApiPathAndMethodAndModule(String apiPath, String method, String module);
}