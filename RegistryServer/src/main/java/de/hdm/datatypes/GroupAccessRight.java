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

import de.hdm.helpers.Checker;

/**
 * This class defines the access rights of one group for one concept. There exists four access rights.
 * The read right means, that users, which are member of the group with the id {@link GroupAccessRight#groupId} are
 * allowed to read the concept with the id {@link GroupAccessRight#conceptId}.
 * The read update means, that users, which are member of the group with the id {@link GroupAccessRight#groupId} are
 * allowed to update the concept with the id {@link GroupAccessRight#conceptId}.
 * The read delete means, that users, which are member of the group with the id {@link GroupAccessRight#groupId} are
 * allowed to delete the concept with the id {@link GroupAccessRight#conceptId}.
 * The read change rights right means, that users, which are member of the group with the id
 * {@link GroupAccessRight#groupId} are allowed to change the access rights of this group and all other groups for the
 * concept with the id {@link GroupAccessRight#conceptId}.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class GroupAccessRight implements IGroupAccessRight {

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // attributes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     * Id of the group, for which the access rights count.
     */
    private int groupId;

    /**
     * Id of the concept, for which the access rights are defined.
     */
    private String conceptId;

    /**
     * Read right. True means, that the group members are allowed to read the concept and false that not.
     */
    private boolean readRight;

    /**
     * Update right. True means, that the group members are allowed to update the concept and false that not.
     */
    private boolean updateRight;

    /**
     * Delete right. True means, that the group members are allowed to delete the concept and false that not.
     */
    private boolean deleteRight;

    /**
     * Change rights right. True means, that the group members are allowed to change the access rights for the concept
     * and false that not.
     */
    private boolean changeRightsRight;




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // constructors
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     * Create a GroupAccessRight.
     * 
     * @param groupId
     *            id of the group. The groupId must be greater equals 1. Otherwise an {@link IllegalArgumentException}
     *            will be thrown.
     * @param conceptId
     *            id of the concept. The concept id must not be null or empty. Otherwise an
     *            {@link IllegalArgumentException} will be thrown.
     * @param readRight
     *            true if the group is allowed to read the concept and false if not
     * @param updateRight
     *            true if the group is allowed to update the concept and false if not
     * @param deleteRight
     *            true if the group is allowed to delete the concept and false if not
     * @param changeRightsRight
     *            true if the group is allowed to change the access rights for the concept and false if not
     */
    public GroupAccessRight(int groupId, String conceptId, boolean readRight, boolean updateRight, boolean deleteRight,
            boolean changeRightsRight) {
        this.setGroupId(groupId);
        this.setConceptId(conceptId);
        this.setReadRight(readRight);
        this.setUpdateRight(updateRight);
        this.setDeleteRight(deleteRight);
        this.setChangeRightsRight(changeRightsRight);
    }




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // getters and setters
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    private void setGroupId(int groupId) {
        Checker.checkIntegerGreaterEquals(groupId, "groupId", 1);
        this.groupId = groupId;
    }

    private void setReadRight(boolean readRight) {
        this.readRight = readRight;
    }

    private void setUpdateRight(boolean updateRight) {
        this.updateRight = updateRight;
    }

    private void setDeleteRight(boolean deleteRight) {
        this.deleteRight = deleteRight;
    }

    private void setChangeRightsRight(boolean changeRightsRight) {
        this.changeRightsRight = changeRightsRight;
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
     * @see de.hdm.datatypes.IGroupAccessRight#getGroupId()
     */
    @Override
    public int getGroupId() {
        return this.groupId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.datatypes.IGroupAccessRight#setConceptId(java.lang.String)
     */
    @Override
    public void setConceptId(String conceptId) {
        Checker.checkEmptiness(conceptId, "conceptId");
        this.conceptId = conceptId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.datatypes.IGroupAccessRight#getConceptId()
     */
    @Override
    public String getConceptId() {
        return this.conceptId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.datatypes.IGroupAccessRight#hasReadRight()
     */
    @Override
    public boolean hasReadRight() {
        return this.readRight;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.datatypes.IGroupAccessRight#hasUpdateRight()
     */
    @Override
    public boolean hasUpdateRight() {
        return this.updateRight;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.datatypes.IGroupAccessRight#hasDeleteRight()
     */
    @Override
    public boolean hasDeleteRight() {
        return this.deleteRight;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.datatypes.IGroupAccessRight#hasChangeRightsRight()
     */
    @Override
    public boolean hasChangeRightsRight() {
        return this.changeRightsRight;
    }

    /**
     * Compares another object with this group access right. The other object is equals to it, if it is not null, also
     * an instance of {@link GroupAccessRight} and the attributes group id {@link #groupId}, concept id
     * {@link #conceptId}, read right {@link #readRight}, update right {@link #updateRight}, delete right
     * {@link #deleteRight}, change rights right {@link #changeRightsRight} are equal.
     * 
     * @return true if otherObject is equal and false if not
     */
    @Override
    public boolean equals(Object otherObject) {
        boolean result = true;
        if (otherObject == null || otherObject instanceof IGroupAccessRight == false) {
            result = false;
        } else {
            IGroupAccessRight otherGroupAccessRight = (IGroupAccessRight) otherObject;
            if (this.groupId != otherGroupAccessRight.getGroupId()) {
                result = false;
            }
            if (this.conceptId.equals(otherGroupAccessRight.getConceptId()) == false) {
                result = false;
            }
            if (this.readRight != otherGroupAccessRight.hasReadRight()) {
                result = false;
            }
            if (this.updateRight != otherGroupAccessRight.hasUpdateRight()) {
                result = false;
            }
            if (this.deleteRight != otherGroupAccessRight.hasDeleteRight()) {
                result = false;
            }
            if (this.changeRightsRight != otherGroupAccessRight.hasChangeRightsRight()) {
                result = false;
            }
        }
        return result;
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
