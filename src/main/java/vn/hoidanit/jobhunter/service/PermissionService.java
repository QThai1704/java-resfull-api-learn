package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.other.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.permission.ResCreatePermissionDTO;
import vn.hoidanit.jobhunter.domain.response.permission.ResFetchPermissionDTO;
import vn.hoidanit.jobhunter.domain.response.permission.ResUpdatePermissionDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    // Create
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public ResCreatePermissionDTO convertToCreatePermissionDTO(Permission permission) {
        ResCreatePermissionDTO resCreatePermissionDTO = new ResCreatePermissionDTO();
        resCreatePermissionDTO.setName(permission.getName());
        resCreatePermissionDTO.setApiPath(permission.getApiPath());
        resCreatePermissionDTO.setMethod(permission.getMethod());
        resCreatePermissionDTO.setModule(permission.getModule());
        resCreatePermissionDTO.setCreatedAt(permission.getCreatedAt());
        resCreatePermissionDTO.setCreatedBy(permission.getCreatedBy());
        return resCreatePermissionDTO;
    }

    // Get
    public Permission getPermissionById(Long id) {
        Optional<Permission> permission = permissionRepository.findById(id);
        if (permission.isPresent()) {
            return permission.get();
        }
        return null;
    }

    public ResFetchPermissionDTO convertToFetchPermissionDTO(Permission permission) {
        ResFetchPermissionDTO resFetchPermissionDTO = new ResFetchPermissionDTO();
        resFetchPermissionDTO.setName(permission.getName());
        resFetchPermissionDTO.setApiPath(permission.getApiPath());
        resFetchPermissionDTO.setMethod(permission.getMethod());
        resFetchPermissionDTO.setModule(permission.getModule());
        resFetchPermissionDTO.setCreatedAt(permission.getCreatedAt());
        resFetchPermissionDTO.setCreatedBy(permission.getCreatedBy());
        resFetchPermissionDTO.setUpdatedAt(permission.getUpdatedAt());
        resFetchPermissionDTO.setUpdatedBy(permission.getUpdatedBy());
        return resFetchPermissionDTO;
    }

    public ResPaginationDTO getAllPermission(Pageable pageable) {
        Page<Permission> pagePermission = this.permissionRepository.findAll(pageable);
        List<Permission> listPermission = pagePermission.getContent();
        List<ResFetchPermissionDTO> listFetchPermissionDTO = listPermission
                .stream()
                .map(permission -> new ResFetchPermissionDTO(
                        permission.getName(),
                        permission.getApiPath(),
                        permission.getMethod(),
                        permission.getModule(),
                        permission.getCreatedAt(),
                        permission.getCreatedBy(),
                        permission.getUpdatedAt(),
                        permission.getUpdatedBy()))
                .collect(Collectors.toList());
        ResPaginationDTO resPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pagePermission.getNumber() + 1);
        meta.setPageSize(pagePermission.getSize());
        meta.setPages(pagePermission.getTotalPages());
        meta.setTotal(pagePermission.getTotalElements());
        resPaginationDTO.setMeta(meta);
        resPaginationDTO.setResult(listFetchPermissionDTO);
        return resPaginationDTO;
    }

    // Update
    public Permission updatePermission(Permission permission) {
        Permission currentPermission = permissionRepository.findById(permission.getId()).get();
        currentPermission.setName(permission.getName());
        currentPermission.setApiPath(permission.getApiPath());
        currentPermission.setMethod(permission.getMethod());
        currentPermission.setModule(permission.getModule());
        return permissionRepository.save(currentPermission);
    }

    public ResUpdatePermissionDTO convertToUpdatePermissionDTO(Permission permission) {
        ResUpdatePermissionDTO resUpdatePermissionDTO = new ResUpdatePermissionDTO();
        resUpdatePermissionDTO.setId(permission.getId());
        resUpdatePermissionDTO.setName(permission.getName());
        resUpdatePermissionDTO.setApiPath(permission.getApiPath());
        resUpdatePermissionDTO.setMethod(permission.getMethod());
        resUpdatePermissionDTO.setModule(permission.getModule());
        resUpdatePermissionDTO.setUpdatedAt(permission.getUpdatedAt());
        resUpdatePermissionDTO.setUpdatedBy(permission.getUpdatedBy());
        return resUpdatePermissionDTO;
    }

    // Delete
    public void deletePermission(Long id) {
        this.permissionRepository.deleteById(id);
    }

    // Check exists
    public boolean checkById(Long id) {
        return this.permissionRepository.existsById(id);
    }

    public boolean checkByApiPathAndMethodAndModule(Permission permission) {
        return this.permissionRepository.existsByApiPathAndMethodAndModule(
                permission.getApiPath(),
                permission.getMethod(),
                permission.getModule());
    }
}
