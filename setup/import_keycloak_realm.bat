@echo off

setlocal

rem replace this with your own installation directory of keycloak
set KEYCLOAK_HOME=D:\Programas\keycloak-11.0.3

%KEYCLOAK_HOME%\bin\standalone.bat -Dkeycloak.migration.action=import -Dkeycloak.migration.realmName=workshop -Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=keycloak_realm_workshop.json -Dkeycloak.migration.strategy=OVERWRITE_EXISTING
