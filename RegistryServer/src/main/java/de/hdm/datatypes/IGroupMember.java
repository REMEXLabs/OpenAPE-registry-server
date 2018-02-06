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

/**
 * This is the interface for a group member. A group member belongs to a group {@link IGroup} and describes its members
 * {@link IGroup#getMembers()}.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IGroupMember {

    /**
     * Getter for the group members user id {@link User#getId()}.
     * 
     * @return the group member's user id.
     */
    public int getUserId();

    /**
     * Setter for the group member's user id {@link User#getId()}.
     * 
     * @param userId
     *            the group meber's user id. It must be greater equals 1. Otherwise an {@link IllegalArgumentException}
     *            will be thrown.
     */
    public void setUserId(int userId);

    /**
     * Getter whether the group member is a group admin or not.
     * 
     * @return true if the group member is a group admin of the group or false if not.
     */
    public boolean isGroupAdmin();

    /**
     * Setter whether the group member is a group admin or not.
     * 
     * @param groupAdmin
     *            true if the group member should be a group admin and false if not.
     */
    public void setGroupAdmin(boolean groupAdmin);
}
