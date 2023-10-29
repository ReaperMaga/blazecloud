package sh.maga.blazecloud.backend.group.resource;

import io.quarkus.security.PermissionsAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import sh.maga.blazecloud.backend.exception.ServiceException;
import sh.maga.blazecloud.backend.group.model.Group;
import sh.maga.blazecloud.backend.group.repository.GroupRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Path("/groups")
public class GroupResource {

    @Inject
    GroupRepository repository;

    @GET
    @PermissionsAllowed("groups.view.all")
    public List<Group> getAll() {
        return repository.listAll();
    }

    @GET
    @Path("/{id}")
    @PermissionsAllowed("groups.view.single")
    public Group get(@PathParam("id") UUID id) throws ServiceException {
        Optional<Group> optional = repository.findByIdOptional(id);
        if(optional.isEmpty()) {
            throw new ServiceException(404, "Group not found");
        }
        return optional.get();
    }
}
