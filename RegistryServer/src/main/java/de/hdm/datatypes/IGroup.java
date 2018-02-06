/*******************************************************************************
 * Copyright 2016-2018 Research group REMEX, Hochschule der Medien (Stuttgart, Germany)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.hdm.datatypes;

import java.util.List;

/**
 * This is the interface for a group.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IGroup {

    /**
     * Getter for the group's id.
     * 
     * @return the group's id, which is an integer greater equals 1 or -1, if the group is not already stored in the
     *         database
     */
    public int getId();

    /**
     * Setter for the group's id.
     * 
     * @param id
     *            the group's id. It must be greater equals 1. Otherwise an {@link IllegalArgumentException} will be
     *            thrown.
     */
    public void setId(int id);

    /**
     * Getter for the group's name.
     * 
     * @return the groups name
     */
    public String getName();

    /**
     * Setter for the group's name.
     * 
     * @param name
     *            the group's name. It must not be null or empty. Otherwise an {@link IllegalArgumentException} will be
     *            thrown.
     */
    public void setName(String name);

    /**
     * Getter for the group's members. If the group has no members, an empty list will be returned.
     * 
     * @return a list with the group's members
     */
    public List<IGroupMember> getMembers();

    /**
     * Setter for the group's members.
     * 
     * @param members
     *            list with the members of this the group. If the group has no member(s) it can be empty but not null.
     *            If it is null, an {@link IllegalArgumentException} will be thrown.
     */
    public void setMembers(List<IGroupMember> members);

    /**
     * Checks whether an user with the given user id {@link IUser#getId()} is an admin of this group. If this user is no
     * member of this group, false will be returned
     * 
     * @param userId
     *            the user's id. It must be greater equals 1. Otherwise an {@link IllegalArgumentException} will be
     *            thrown.
     * @return true if the user is an admin of this group and false if not
     */
    public boolean isUserGroupAdmin(int userId);

}
