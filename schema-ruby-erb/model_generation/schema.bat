::  Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA

::  Licensed under the Apache License, Version 2.0 (the "License");
::  you may not use this file except in compliance with the License.
::  You may obtain a copy of the License at
 
::       http://www.apache.org/licenses/LICENSE-2.0
 
::  Unless required by applicable law or agreed to in writing, software
::  distributed under the License is distributed on an "AS IS" BASIS,
::  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
::  See the License for the specific language governing permissions and
::  limitations under the License.

@echo off


set full_path=%~dp0

::  Print helpFunction in case parameters are empty
if "%~1" == "" (
  echo "No params found. Use -h to get more info";
  exit /b
)


for %%x in (%*) do ( 
 IF "%%x"=="-s" (
   ruby %full_path%/main.rb "%full_path%" "swift"
   goto:eof)
 IF "%%x"=="-b" (
   ruby %full_path%/main.rb "%full_path%" "kotlinBackend"
   goto:eof)
 IF "%%x"=="-a" (
   ruby %full_path%/main.rb "%full_path%" "kotlinAndroid"
   goto:eof)
 IF "%%x"=="-k" (
   ruby %full_path%/main.rb "%full_path%" "kotlin"
   goto:eof)
 IF "%%x"=="-t" (
   ruby %full_path%/main.rb "%full_path%" "ts"
   goto:eof)
 IF "%%x"=="-w" (
   ruby %full_path%/main.rb "%full_path%" "all"
   goto:eof)
 IF "%%x"=="-h" (
   call:helpFunction
   goto:eof)
 IF %%x GTR 2 (
   echo "Invalid option '%%x'. Use -h to get more info"
   exit /b)
)



echo.&pause&goto:eof

:helpFunction
echo.    ================================================================
echo.                            Beagle Schema
echo.    ================================================================
echo.    DESCRIPTION
echo.        This script uses Ruby to generate Beagle models
echo.        for other supported languages
echo.    OPTIONS
echo.        -w                       Generates code for all the supported languages
echo.        -s                       Generates code in Swift
echo.        -k                       Generates code in Kotlin (Backend and Android)
echo.        -b                       Generates code in Kotlin Backend
echo.        -a                       Generates code in Kotlin Android
echo.        -t                       Generates code in TypeScript
echo.        -h                       Show help
echo.    EXAMPLES
echo.        ./schema.sh -s
echo.        ./schema.sh -t                         
echo.    ================================================================                                           
echo.    IMPLEMENTATION
echo.        author          Zup
echo.        copyright       Copyright (c) https://www.zup.com.br
echo.        license         Apache-2.0 License
echo.    ================================================================
goto:eof

