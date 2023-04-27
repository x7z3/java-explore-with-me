@echo off
pushd %~dp0
echo compiling project
call mvn -B clean package
if %ERRORLEVEL% neq 0 goto error
echo clean docker old images
docker compose down --rmi local
echo deploying new docker images
docker-compose up --build
:error
echo compilation error
docker compose down --rmi local