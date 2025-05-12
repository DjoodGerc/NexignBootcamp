package crmApp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum RoleEnum {
    MANAGER(new RolesEntity(1l,"ROLE_MANAGER")),
    SUBS(new RolesEntity(2l,"ROLE_SUBSCRIBER"));
    public RolesEntity roles;
}
