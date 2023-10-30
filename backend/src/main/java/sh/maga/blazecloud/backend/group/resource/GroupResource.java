package sh.maga.blazecloud.backend.group.resource;

import io.quarkus.security.PermissionsAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import sh.maga.blazecloud.backend.exception.ServiceException;
import sh.maga.blazecloud.backend.exception.ValidationGroups;
import sh.maga.blazecloud.backend.group.GroupService;
import sh.maga.blazecloud.backend.group.model.Group;
import sh.maga.blazecloud.backend.group.repository.GroupRepository;

import java.net.URI;
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

    @Inject
    GroupService service;

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

    @POST
    @PermissionsAllowed("groups.view.create")
    public Response create(@Valid @ConvertGroup(to = ValidationGroups.Post.class) Group group) throws ServiceException {
        if(repository.existsByName(group.getName())) {
           throw new ServiceException(403, "Group already exists");
        }
        Group created = service.create(group.getName());
        repository.persist(created);
        return Response.created(URI.create("/groups/".concat(created.getId().toString()))).entity(created).build();
    }

    @PATCH
    @Path("/{id}")
    @PermissionsAllowed("groups.view.update")
    public Group update(@PathParam("id") UUID id, Group group) throws ServiceException {
        Optional<Group> optional = repository.findByIdOptional(id);
        if(optional.isEmpty()) {
            throw new ServiceException(404, "Group not found");
        }
        Group persistent = optional.get();
        if(group.getName() != null) {
            persistent.setName(group.getName());
        }
        if(group.getPermissions() != null) {
            persistent.setPermissions(group.getPermissions());
        }
        repository.update(persistent);
        return persistent;
    }

    @DELETE
    @Path("/{id}")
    @PermissionsAllowed("groups.view.delete")
    public Group delete(@PathParam("id") UUID id) throws ServiceException {
        Optional<Group> optional = repository.findByIdOptional(id);
        if(optional.isEmpty()) {
            throw new ServiceException(404, "Group not found");
        }
        Group group = optional.get();
        repository.delete(group);
        return group;
    }
}
