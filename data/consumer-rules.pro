-keepnames class steam.** {
    public static * ADAPTER;
}

-keep class ** implements com.squareup.wire.Service {
    *;
}

-keep class com.squareup.wire.WireRpc {
    *;
}

# Manual keep rules
-keep,allowobfuscation,allowoptimization class steam.twofactor.CTwoFactor_RemoveAuthenticatorViaChallengeStart_Response {
    *;
}

-keep,allowobfuscation,allowoptimization class steam.auth.CAuthentication_UpdateAuthSessionWithMobileConfirmation_Response {
    *;
}

-keep,allowobfuscation,allowoptimization class steam.auth.CAuthentication_RefreshToken_Revoke_Response {
    *;
}

-keep,allowobfuscation,allowoptimization class steam.auth.CAuthentication_UpdateAuthSessionWithSteamGuardCode_Response {
    *;
}

-keep,allowobfuscation,allowoptimization class steam.clientcomm.CClientComm_InstallClientApp_Response {
    *;
}

-keep,allowobfuscation,allowoptimization class steam.clientcomm.CClientComm_SetClientAppUpdateState_Response {
    *;
}

-keep,allowobfuscation,allowoptimization class steam.clientcomm.CClientComm_UninstallClientApp_Response {
    *;
}

-keep,allowobfuscation,allowoptimization class steam.player.CPlayer_Set*_Response {
    *;
}