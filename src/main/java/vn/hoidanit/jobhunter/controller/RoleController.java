package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.other.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.role.ResCreateRoleDTO;
import vn.hoidanit.jobhunter.domain.response.role.ResFetchRoleDTO;
import vn.hoidanit.jobhunter.domain.response.role.ResUpdateRoleDTO;
import vn.hoidanit.jobhunter.service.RoleService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Create
    @PostMapping("/role")
    public ResponseEntity<ResCreateRoleDTO> createRole(@RequestBody Role role) {
        Role newRole = this.roleService.createRole(role);
        ResCreateRoleDTO resCreateRoleDTO = this.roleService.convertToCreateRoleDTO(newRole);
        return ResponseEntity.ok().body(resCreateRoleDTO);
    }

    // Get
    @GetMapping("/role/{id}")
    public ResponseEntity<ResFetchRoleDTO> getRoleById(@RequestBody Role role) {
        Role findRole = this.roleService.getRoleById(role.getId());
        ResFetchRoleDTO resFetchRoleDTO = this.roleService.convertToFetchRoleDTO(findRole);
        return ResponseEntity.ok().body(resFetchRoleDTO);
    }

    @GetMapping("/roles")
    public ResponseEntity<ResPaginationDTO> getAllRole(Pageable pageable) {
        ResPaginationDTO resPaginationDTO = this.roleService.getAllRole(pageable);
        return ResponseEntity.ok().body(resPaginationDTO);
    }

    // Update
    @PutMapping("/role/{id}")
    public ResponseEntity<ResUpdateRoleDTO> updateRole(@RequestBody Role role) {
        Role currentRole = this.roleService.updateRole(role);
        ResUpdateRoleDTO resUpdateRoleDTO = this.roleService.convertToUpdateRoleDTO(currentRole);
        return ResponseEntity.ok().body(resUpdateRoleDTO);
    }

    // Delete
    @DeleteMapping("/role/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        this.roleService.deleteRole(id);
        return ResponseEntity.ok().body(null);
    }
}
