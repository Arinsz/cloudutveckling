package com.arins.techstore.cognito;
import com.arins.techstore.aws.Xray.XRayTimed;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;


public class CognitoHandler {

    private static CognitoIdentityProviderClient getClient() {
        return CognitoIdentityProviderClient.builder()
                .region(Region.EU_NORTH_1)
                .build();
    }



    public static void deleteUser(String userId) {
        try {
            AdminDeleteUserRequest deleteUserRequest = AdminDeleteUserRequest.builder()
                    .userPoolId("eu-north-1_oUHHULFJL")  // Replace with your user pool ID if necessary
                    .username(userId)
                    .build();

            CognitoIdentityProviderClient cognitoClient = getClient();
            cognitoClient.adminDeleteUser(deleteUserRequest);

            System.out.println("Successfully deleted user from Cognito.");
        } catch (CognitoIdentityProviderException e) {
            System.err.println("Error deleting user from Cognito: " + e.awsErrorDetails().errorMessage());
        }
    }

    public static boolean isUserInGroup(String username, String userPoolId, String groupName) {
        try {
            AdminListGroupsForUserRequest request = AdminListGroupsForUserRequest.builder()
                    .username(username)
                    .userPoolId(userPoolId)
                    .build();

            CognitoIdentityProviderClient cognitoClient = getClient();
            AdminListGroupsForUserResponse response = cognitoClient.adminListGroupsForUser(request);

            return response.groups().stream()
                    .anyMatch(group -> group.groupName().equals(groupName));
        } catch (CognitoIdentityProviderException e) {
            System.err.println("Error checking user group: " + e.awsErrorDetails().errorMessage());
            return false;
        }

    }


}
