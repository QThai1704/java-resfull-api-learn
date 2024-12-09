package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.other.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.role.ResCreateRoleDTO;
import vn.hoidanit.jobhunter.domain.response.role.ResFetchRoleDTO;
import vn.hoidanit.jobhunter.domain.response.role.ResUpdateRoleDTO;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;
    public RoleService(RoleRepository roleRepository, PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.permissionService = permissionService;
    }

    // Create
    public Role createRole(Role role) {
        return this.roleRepository.save(role);
    }

    public ResCreateRoleDTO convertToCreateRoleDTO(Role role) {
        ResCreateRoleDTO resCreateRoleDTO = new ResCreateRoleDTO();
        List<ResCreateRoleDTO.PermissionRole> permissionRole = new ArrayList<ResCreateRoleDTO.PermissionRole>();
        List<Permission> permission = role.getPermissions();
        permission.forEach(item -> {
            Permission findPermission = this.permissionService.getPermissionById(item.getId());
            ResCreateRoleDTO.PermissionRole permissionRoleItem = new ResCreateRoleDTO.PermissionRole();
            permissionRoleItem.setName(findPermission.getName());
            permissionRoleItem.setApiPath(findPermission.getApiPath());
            permissionRoleItem.setMethod(findPermission.getMethod());
            permissionRoleItem.setModule(findPermission.getModule());
            permissionRole.add(permissionRoleItem);
        });
        resCreateRoleDTO.setName(role.getName());
        resCreateRoleDTO.setDescription(role.getDescription());
        resCreateRoleDTO.setActive(role.isActive());
        resCreateRoleDTO.setCreatedAt(role.getCreatedAt());
        resCreateRoleDTO.setCreatedBy(role.getCreatedBy());
        resCreateRoleDTO.setPermission(permissionRole);
        return resCreateRoleDTO;
    }

    // Get
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).get();
    }

    public ResFetchRoleDTO convertToFetchRoleDTO(Role role) {
        ResFetchRoleDTO resFetchRoleDTO = new ResFetchRoleDTO();
        List<ResFetchRoleDTO.PermissionRole> permissionRole = new ArrayList<ResFetchRoleDTO.PermissionRole>();
        List<Permission> permission = role.getPermissions();
        permission.forEach(item -> {
            Permission findPermission = this.permissionService.getPermissionById(item.getId());
            ResFetchRoleDTO.PermissionRole permissionRoleItem = new ResFetchRoleDTO.PermissionRole();
            permissionRoleItem.setName(findPermission.getName());
            permissionRoleItem.setApiPath(findPermission.getApiPath());
            permissionRoleItem.setMethod(findPermission.getMethod());
            permissionRoleItem.setModule(findPermission.getModule());
            permissionRole.add(permissionRoleItem);
        });
        resFetchRoleDTO.setName(role.getName());
        resFetchRoleDTO.setDescription(role.getDescription());
        resFetchRoleDTO.setActive(role.isActive());
        resFetchRoleDTO.setCreatedAt(role.getCreatedAt());
        resFetchRoleDTO.setCreatedBy(role.getCreatedBy());
        resFetchRoleDTO.setUpdatedAt(role.getUpdatedAt());
        resFetchRoleDTO.setUpdatedBy(role.getUpdatedBy());
        resFetchRoleDTO.setPermission(permissionRole);
        return resFetchRoleDTO;
    }

    public ResPaginationDTO getAllRole(Pageable pageable) {
        Page<Role> pageRole = this.roleRepository.findAll(pageable);
        List<Role> listRole = pageRole.getContent();
        List<ResFetchRoleDTO> listFetchRoleDTO = listRole
                .stream()
                .map(role ->
                    new ResFetchRoleDTO(
                        role.getName(),
                        role.getDescription(),
                        role.isActive(),
                        role.getCreatedAt(),
                        role.getCreatedBy(),
                        role.getUpdatedAt(),
                        role.getUpdatedBy(),
                        role.getPermissions().stream()
                        .map(permission -> {
                            ResFetchRoleDTO.PermissionRole permissionRole = new ResFetchRoleDTO.PermissionRole();
                            permissionRole.setName(permission.getName());
                            permissionRole.setApiPath(permission.getApiPath());
                            permissionRole.setMethod(permission.getMethod());
                            permissionRole.setModule(permission.getModule());
                            return permissionRole;
                        }).collect(Collectors.toList()
                    )))
                .collect(Collectors.toList());
        ResPaginationDTO resPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageRole.getNumber() + 1);
        meta.setPageSize(pageRole.getSize());
        meta.setPages(pageRole.getTotalPages());
        meta.setTotal(pageRole.getTotalElements());
        resPaginationDTO.setMeta(meta);
        resPaginationDTO.setResult(listFetchRoleDTO);
        return resPaginationDTO;
    }

    // Update
    public Role updateRole(Role role) {
        Role currentRole = roleRepository.findById(role.getId()).get();
        currentRole.setName(role.getName());
        currentRole.setDescription(role.getDescription());
        currentRole.setActive(role.isActive());
        currentRole.setPermissions(role.getPermissions());
        return roleRepository.save(currentRole);
    }

    public ResUpdateRoleDTO convertToUpdateRoleDTO(Role role) {
        ResUpdateRoleDTO resUpdateRoleDTO = new ResUpdateRoleDTO();
        List<ResUpdateRoleDTO.PermissionRole> permissionRole = new ArrayList<ResUpdateRoleDTO.PermissionRole>();
        List<Permission> permission = role.getPermissions();
        permission.forEach(item -> {
            Permission findPermission = this.permissionService.getPermissionById(item.getId());
            ResUpdateRoleDTO.PermissionRole permissionRoleItem = new ResUpdateRoleDTO.PermissionRole();
            permissionRoleItem.setName(findPermission.getName());
            permissionRoleItem.setApiPath(findPermission.getApiPath());
            permissionRoleItem.setMethod(findPermission.getMethod());
            permissionRoleItem.setModule(findPermission.getModule());
            permissionRole.add(permissionRoleItem);
        });
        resUpdateRoleDTO.setId(role.getId());
        resUpdateRoleDTO.setName(role.getName());
        resUpdateRoleDTO.setDescription(role.getDescription());
        resUpdateRoleDTO.setActive(role.isActive());
        resUpdateRoleDTO.setUpdatedAt(role.getUpdatedAt());
        resUpdateRoleDTO.setUpdatedBy(role.getUpdatedBy());
        resUpdateRoleDTO.setPermission(permissionRole);
        return resUpdateRoleDTO;
    }

    // Delete
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    // Check exists
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }
}