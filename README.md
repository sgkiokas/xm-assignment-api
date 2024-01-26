# XM API assignment
## Description
This repo contains the code for the API-related assignment for XM. It uses Java 17 and it's build via `gradle`. In order to keep the
code clean, `checkstyle` has been introduced.

## How to run the tests
The tests are located in `src/test/java/api/test` folder. To build the project, along with checkstyle validation,
please run `./gradlew clean build` on Mac or use the `gradlew.bat` for Windows. To simply run the tests, please run
`./gradlew :test`.