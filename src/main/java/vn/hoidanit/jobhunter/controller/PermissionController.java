package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.other.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.permission.ResCreatePermissionDTO;
import vn.hoidanit.jobhunter.domain.response.permission.ResFetchPermissionDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    // Create
    @PostMapping("/permission")
    public ResponseEntity<ResCreatePermissionDTO> createPermission(@RequestBody Permission permission) {
        Permission newPermission = this.permissionService.createPermission(permission);
        ResCreatePermissionDTO resCreatePermissionDTO = this.permissionService.convertToCreatePermissionDTO(newPermission);
        return ResponseEntity.ok().body(resCreatePermissionDTO);
    }

    // Get
    @GetMapping("/permission/{id}")
    public ResponseEntity<ResFetchPermissionDTO> getPermissionById(@PathVariable("id") Long id) throws IdInvalidException {
        if(id == null){
            throw new IdInvalidException("Id không tồn tại");
        }
        Permission permission = this.permissionService.getPermissionById(id);
        ResFetchPermissionDTO resFetchPermissionDTO = this.permissionService.convertToFetchPermissionDTO(permission);
        return ResponseEntity.ok().body(resFetchPermissionDTO);
    }

    @GetMapping("/permissions")
    public ResponseEntity<ResPaginationDTO> getMethodName(Pageable pageable) {
        ResPaginationDTO resPaginationDTO = this.permissionService.getAllPermission(pageable);
        return ResponseEntity.ok().body(resPaginationDTO);
    }

    // Update
    @PutMapping("/permission/{id}")
    public ResponseEntity<ResCreatePermissionDTO> putMethodName(@RequestBody Permission permission) {
        Permission currentPermission = this.permissionService.updatePermission(permission);
        ResCreatePermissionDTO resCreatePermissionDTO = this.permissionService.convertToCreatePermissionDTO(currentPermission);
        return ResponseEntity.ok().body(resCreatePermissionDTO);
    }

    // Delete
    @DeleteMapping("/permission/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") Long id) throws IdInvalidException {
        if(id == null){
            throw new IdInvalidException("Id không tồn tại");
        }
        this.permissionService.deletePermission(id);
        return ResponseEntity.notFound().build();
    }
}
