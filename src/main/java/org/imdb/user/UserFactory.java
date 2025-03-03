package org.imdb.user;

import org.imdb.enumerations.AccountType;

public class UserFactory {
  /**
   * Creates a new User instance based on the given account type.
   *
   * @param accountType The type of account to create.
   * @return A new User instance of the specified type.
   * @throws IllegalArgumentException if accountType is null.
   */
  public static User createUser(AccountType accountType) {
    if (accountType == null) {
      throw new IllegalArgumentException("AccountType cannot be null.");
    }

    return switch (accountType) {
      case REGULAR -> new Regular();
      case CONTRIBUTOR -> new Contributor();
      case ADMIN -> new Admin();
    };
  }

  /**
   * Creates a new User instance by copying an existing user.
   *
   * @param user The user to copy.
   * @return A new User instance of the same type as the provided user.
   * @throws IllegalArgumentException if user is null.
   */
  public static User createUser(User user) {
    if (user == null) {
      throw new IllegalArgumentException("User cannot be null.");
    }

    return switch (user.getUserType()) {
      case REGULAR -> new Regular(user);
      case CONTRIBUTOR -> new Contributor(user);
      case ADMIN -> new Admin(user);
    };
  }
}
