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
package de.hdm.server;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Manage secure password hashes using PBKDF2 with HMACSHA1.
 */

public class PasswordEncoder {

    /**
     * Secures a password generating a random hash.
     *
     * @param password Plain text password to be encoded
     * @return The password hash
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static String encode(String password) throws PasswordStorage.CannotPerformOperationException {
        return PasswordStorage.createHash(password);
    }

    /**
     * Checks if a plain text password matches a hashed one.
     *
     * @param plainPassword   The plain password
     * @param encodedPassword The encoded password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static boolean matches(String plainPassword, String encodedPassword) throws PasswordStorage.CannotPerformOperationException, PasswordStorage.InvalidHashException {
        return PasswordStorage.verifyPassword(plainPassword, encodedPassword);
    }
}
