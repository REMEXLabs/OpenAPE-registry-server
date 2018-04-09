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
package de.hdm.databaseaccess.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import de.hdm.databaseaccess.DaoFactory;
import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IGroupDao;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.databaseaccess.IUserDao;
import de.hdm.datatypes.IUser;
import de.hdm.datatypes.User;
import de.hdm.helpers.Checker;

/**
 * This class implements the database access for users on MySql databases.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class MySqlUserDao extends MySqlDao implements IUserDao {

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // attributes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     * Select part of SQL queries which select users.
     */
    private static final String SELECT_USER = "SELECT id, userName, firstName, lastName, institute, eMailAddress, hashOfPassword, hashOfResetPassword, expiryDateOfResetPassword, apiKey, superAdmin, sessionId, numberOfFailedLogins, locked FROM User";

    /**
     * Data access object for the groups.
     */
    private IGroupDao groupDao;




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // constructors
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     * Creates a new MySqlUserDao and initializes the required dependencies.
     */
    public MySqlUserDao() {
        this.groupDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createGroupDao();
    }




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // getters and setters
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************




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
     * @see de.hdm.databaseaccess.IUserDao#insertUser(de.hdm.databaseaccess.IUnitOfWork, de.hdm.datatypes.IUser)
     */
    @Override
    public void insertUser(IUnitOfWork unitOfWork, IUser user) throws DataAccessException {
        Checker.checkNull(user, "user");
        PreparedStatement insertUserStatement;
        Connection connection = null;
        try {
            connection = this.getConnection(unitOfWork);
            insertUserStatement = connection.prepareStatement(
                    "INSERT INTO User (userName, firstName, lastName, institute, eMailAddress, hashOfPassword, hashOfResetPassword, apiKey, superAdmin, sessionId, numberOfFailedLogins, locked) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertUserStatement.setString(1, user.getUserName());
            insertUserStatement.setString(2, user.getFirstName());
            insertUserStatement.setString(3, user.getLastName());
            insertUserStatement.setString(4, user.getInstitute());
            insertUserStatement.setString(5, user.geteMailAddress());
            insertUserStatement.setString(6, user.getHashOfPassword());
            insertUserStatement.setString(7, user.getHashOfResetPassword());
            insertUserStatement.setString(8, user.getApiKey());
            insertUserStatement.setBoolean(9, user.isSuperAdmin());
            insertUserStatement.setString(10, user.getSessionId());
            insertUserStatement.setInt(11, user.getNumberOfFailedLogins());
            insertUserStatement.setBoolean(12, user.isLocked());
            insertUserStatement.executeUpdate();
            insertUserStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("User could not be inserted on account of an error with the database!", e);
        } finally {
            this.closeConnection(unitOfWork, connection);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.databaseaccess.IUserDao#selectUsers(de.hdm.databaseaccess.IUnitOfWork)
     */
    @Override
    public List<IUser> selectUsers(IUnitOfWork unitOfWork) throws DataAccessException {
        Connection connection = this.getConnection(unitOfWork);
        PreparedStatement selectUsersStatement;
        List<IUser> users = new ArrayList<IUser>();
        try {
            selectUsersStatement = connection.prepareStatement(SELECT_USER);
            ResultSet resultSet = selectUsersStatement.executeQuery();
            while (resultSet.next()) {
                users.add(this.createUser(resultSet));
            }
            resultSet.close();
            selectUsersStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Users could not be selected on account of an error with the database!", e);
        } finally {
            this.closeConnection(unitOfWork, connection);
        }
        return users;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.databaseaccess.IUserDao#selectUser(de.hdm.databaseaccess.IUnitOfWork, int)
     */
    @Override
    public IUser selectUser(IUnitOfWork unitOfWork, int userId) throws DataAccessException {
        Checker.checkUserId(userId);
        return this.selectUserWithWhereCondition(unitOfWork, "WHERE id = " + userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.databaseaccess.IUserDao#selectUserIdForApiKey(de.hdm.databaseaccess.IUnitOfWork, java.lang.String)
     */
    @Override
    public int selectUserIdForApiKey(IUnitOfWork unitOfWork, String apiKey) throws DataAccessException {
        Checker.checkNullAndEmptiness(apiKey, "apiKey");
        int userId = -1;
        Connection connection = this.getConnection(unitOfWork);
        PreparedStatement selectUserIdStatement;
        try {
            selectUserIdStatement = connection.prepareStatement("SELECT id FROM User WHERE apiKey = ?");
            selectUserIdStatement.setString(1, apiKey);
            ResultSet resultSet = selectUserIdStatement.executeQuery();
            while (resultSet.next()) {
                userId = resultSet.getInt(1);
            }
            resultSet.close();
            selectUserIdStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("User-Id could not requested on account of an error with the database!", e);
        } finally {
            this.closeConnection(unitOfWork, connection);
        }
        return userId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.databaseaccess.IUserDao#deleteUser(de.hdm.databaseaccess.IUnitOfWork, int)
     */
    @Override
    public void deleteUser(IUnitOfWork unitOfWork, int userId) throws DataAccessException {
        Checker.checkUserId(userId);
        Connection connection = this.getConnection(unitOfWork);
        PreparedStatement deleteUserStatement;
        try {
            deleteUserStatement = connection.prepareStatement("DELETE FROM User WHERE id = ?");
            deleteUserStatement.setInt(1, userId);
            deleteUserStatement.execute();
            deleteUserStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException(
                    "User with the id = " + userId + " could not be deleted on account of an error with the database!",
                    e);
        } finally {
            this.closeConnection(unitOfWork, connection);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.databaseaccess.IUserDao#isUserSuperAdmin(de.hdm.databaseaccess.IUnitOfWork, int)
     */
    @Override
    public boolean isUserSuperAdmin(IUnitOfWork unitOfWork, int userId) throws DataAccessException {
        Checker.checkUserId(userId);
        boolean isSuperAdmin = false;
        Connection connection = this.getConnection(unitOfWork);
        try {
            PreparedStatement selectIsUserSuperAdminStatement = connection
                    .prepareStatement("SELECT superAdmin FROM User WHERE id = ?");
            selectIsUserSuperAdminStatement.setInt(1, userId);
            ResultSet resultSet = selectIsUserSuperAdminStatement.executeQuery();
            while (resultSet.next()) {
                isSuperAdmin = resultSet.getBoolean(1);
            }
            resultSet.close();
            selectIsUserSuperAdminStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Could not select whether user " + userId
                    + " is super admin or not on account of an error with the database!", e);
        } finally {
            this.closeConnection(unitOfWork, connection);
        }
        return isSuperAdmin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.databaseaccess.IUserDao#updateUser(de.hdm.databaseaccess.IUnitOfWork, de.hdm.datatypes.IUser)
     */
    @Override
    public void updateUser(IUnitOfWork unitOfWork, IUser user) throws DataAccessException {
        Checker.checkNull(user, "user");
        
        //System.out.println("in updateUser");
        //System.out.println("user.getHashOfResetPassword() = " + user.getHashOfResetPassword());
        
        Connection connection = this.getConnection(unitOfWork);
        try {
            PreparedStatement updateUserStatement = connection.prepareStatement(
                    "UPDATE User SET userName = ?, firstName = ?, lastName = ?, institute = ?, eMailAddress = ?, hashOfPassword = ?, hashOfResetPassword = ?, expiryDateOfResetPassword = ?, apiKey = ?, superAdmin = ?, sessionId = ?, numberOfFailedLogins = ?, locked = ? WHERE id = ?");
            updateUserStatement.setString(1, user.getUserName());
            updateUserStatement.setString(2, user.getFirstName());
            updateUserStatement.setString(3, user.getLastName());
            updateUserStatement.setString(4, user.getInstitute());
            updateUserStatement.setString(5, user.geteMailAddress());
            updateUserStatement.setString(6, user.getHashOfPassword());
            updateUserStatement.setString(7, user.getHashOfResetPassword());

            if (user.getExpiryDateOfResetPasswordInMilliseconds() == -1) {
                updateUserStatement.setObject(8, null);
            } else {
                GregorianCalendar calendar = new GregorianCalendar();
                // TODO set UTC
                // calendar.setTimeZone(TimeZone.g));
                calendar.setTimeInMillis(user.getExpiryDateOfResetPasswordInMilliseconds());
                updateUserStatement.setString(8, this.convertGregorianCalendarToMySqlDateTime(calendar));
            }

            updateUserStatement.setString(9, user.getApiKey());
            updateUserStatement.setBoolean(10, user.isSuperAdmin());
            updateUserStatement.setString(11, user.getSessionId());
            updateUserStatement.setInt(12, user.getNumberOfFailedLogins());
            updateUserStatement.setBoolean(13, user.isLocked());
            updateUserStatement.setInt(14, user.getId());
            updateUserStatement.executeUpdate();
            updateUserStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException(
                    "Could not update user " + user.getId() + " on account of an error with the database!", e);
        } finally {
            this.closeConnection(unitOfWork, connection);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hdm.databaseaccess.IUserDao#selectUser(de.hdm.databaseaccess.IUnitOfWork, java.lang.String)
     */
    @Override
    public IUser selectUser(IUnitOfWork unitOfWork, String userName) throws DataAccessException {
        Checker.checkNullAndEmptiness(userName, "userName");
        return this.selectUserWithWhereCondition(unitOfWork, "WHERE userName = '" + userName + "'");
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

    private IUser selectUserWithWhereCondition(IUnitOfWork unitOfWork, String whereCondition)
            throws DataAccessException {
        Connection connection = this.getConnection(unitOfWork);
        PreparedStatement selectUserStatement;
        IUser user = null;
        try {
            selectUserStatement = connection.prepareStatement(SELECT_USER + " " + whereCondition);
            ResultSet resultSet = selectUserStatement.executeQuery();
            while (resultSet.next()) {
                user = this.createUser(resultSet);
            }
            resultSet.close();
            selectUserStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException(
                    "User (" + whereCondition + ") could not be selected on account of an error with the database!", e);
        } finally {
            this.closeConnection(unitOfWork, connection);
        }
        
        //System.out.println("in selectUserWithWhereCondition");
        //System.out.println("user.getHashOfResetPassword() = " + user.getHashOfResetPassword());
        
        return user;
    }

    private IUser createUser(ResultSet resultSet) throws SQLException, DataAccessException {
        //System.out.println(resultSet.getString(8));
        IUser user = new User(resultSet.getInt(1));
        user.setUserName(resultSet.getString(2));
        user.setFirstName(resultSet.getString(3));
        user.setLastName(resultSet.getString(4));
        user.setInstitute(resultSet.getString(5));
        user.seteMailAddress(resultSet.getString(6));
        user.setHashOfPassword(resultSet.getString(7));
        user.setHashOfResetPassword(resultSet.getString(8));

        String expiryDateOfResetPasswordAsString = resultSet.getString(9);
        if (expiryDateOfResetPasswordAsString != null) {
            GregorianCalendar expiryDateOfResetPasswordAsGregorianCalendar = this
                    .convertMySqlDateTimeToGregorianCalendar(expiryDateOfResetPasswordAsString);
            user.setExpiryDateOfResetPasswordInMilliseconds(
                    expiryDateOfResetPasswordAsGregorianCalendar.getTimeInMillis());
        } else {
            user.setExpiryDateOfResetPasswordInMilliseconds(-1);
        }

        user.setApiKey(resultSet.getString(10));
        user.setSuperAdmin(resultSet.getBoolean(11));
        user.setSessionId(resultSet.getString(12));
        user.setNumberOfFailedLogins(resultSet.getInt(13));
        user.setLocked(resultSet.getBoolean(14));
        user.setAdminOfAtLeastOneGroup(this.groupDao.isUserAdminOfAtLeastOneGroup(null, user.getId()));
        return user;
    }




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // inner classes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

}
