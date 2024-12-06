package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Create
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }
    // Get
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).get();
    }

    public Page<Role> getAllRole(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    // Update
    public Role updateRole(Role role) {
        Role currentRole = roleRepository.findById(role.getId()).get();
        currentRole.setName(role.getName());
        currentRole.setDescription(role.getDescription());
        currentRole.setActive(role.isActive());
        return roleRepository.save(currentRole);
    }

    // Delete
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
