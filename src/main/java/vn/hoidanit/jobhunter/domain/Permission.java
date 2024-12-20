package vn.hoidanit.jobhunter.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "permissions")
public class Permission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank(message = "Name không được trống!")
    String name;
    @NotBlank(message = "API path không được trống!")
    String apiPath;
    @NotBlank(message = "Method không được trống!")
    String method;
    @NotBlank(message = "Module không được trống!")
    String module;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    @JsonIgnore
    List<Role> roles;

    @PrePersist
    public void handlerBeforeCreate() {
        this.createdAt = Instant.now();
        this.createdBy = "admin";
    }

    @PreUpdate
    public void handlerBeforeUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = "admin";
    }

    public Permission(@NotBlank(message = "Name không được trống!") String name,
            @NotBlank(message = "API path không được trống!") String apiPath,
            @NotBlank(message = "Method không được trống!") String method,
            @NotBlank(message = "Module không được trống!") String module) {
        this.name = name;
        this.apiPath = apiPath;
        this.method = method;
        this.module = module;
    }
}
