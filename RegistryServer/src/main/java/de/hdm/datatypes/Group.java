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

import de.hdm.helpers.Checker;

/**
 * This class defines a group. A group has members. Some of those members can also be admins of the group. Group admins
 * are allowed to edit a group and their memberships. The registry server uses groups to manage the access rights for
 * the concepts. For each concept it is defined which group is allowed to read, update and delete it. Thus which access
 * right an user has, depends on his group member ships.
 * 
 * This class is not thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class Group implements IGroup {

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // attributes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     * Group's id.
     */
    private int id;

    /**
     * Group's name.
     */
    private String name;

    /**
     * Users who are members of the group.
     */
    private List<IGroupMember> members;




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // constructors
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     * Create a new group and set's the value of {@link Group#id} to -1. This constructor should be used, to instantiate
     * groups, which are not already stored in the database.
     * 
     * @param name
     *            the name of the group. It must not be null or empty. Otherwise an {@link IllegalArgumentException}
     *            will be thrown.
     * @param members
     *            list with the members of this the group. If the group has no member(s) it can be empty but not null.
     *            If it is null, an {@link IllegalArgumentException} will be thrown.
     */
    public Group(String name, List<IGroupMember> members) {
        this(-1, name, members);
    }

    /**
     * Create a new group. This constructor should be used, to instantiate groups, which are already stored in the
     * database.
     * 
     * @param id
     *            the id of the group. It must be greater equals 1. Otherwise an {@link IllegalArgumentException} will
     *            be thrown.
     * @param name
     *            the name of the group. It must not be null or empty. Otherwise an {@link IllegalArgumentException}
     *            will be thrown.
     * @param members
     *            list with the members of this the group. If the group has no member(s) it can be empty but not null.
     *            If it is null, an {@link IllegalArgumentException} will be thrown.
     */
    public Group(int id, String name, List<IGroupMember> members) {
        this.setId(id);
        this.setName(name);
        this.setMembers(members);
    }




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // getters and setters
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.datatypes.IGroup#getId()
     */
    @Override
    public int getId() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.datatypes.IGroup#setId(int)
     */
    @Override
    public void setId(int id) {
        Checker.checkIntegerGreaterEquals(id, "id", -1);
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.datatypes.IGroup#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.datatypes.IGroup#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        Checker.checkNullAndEmptiness(name, "name");
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.datatypes.IGroup#getMembers()
     */
    @Override
    public List<IGroupMember> getMembers() {
        return this.members;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.datatypes.IGroup#setMembers(java.util.List)
     */
    @Override
    public void setMembers(List<IGroupMember> members) {
        Checker.checkNull(members, "members");
        this.members = members;
    }




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // abstract methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // override methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.datatypes.IGroup#isUserGroupAdmin(int)
     */
    @Override
    public boolean isUserGroupAdmin(int userId) {
        Checker.checkUserId(userId);
        boolean isGroupAdmin = false;
        for (IGroupMember groupMember : this.members) {
            if (groupMember.getUserId() == userId) {
                if (groupMember.isGroupAdmin()) {
                    isGroupAdmin = true;
                }
                break;
            }
        }
        return isGroupAdmin;
    }




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // public methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // protected methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // private methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // inner classes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

}
