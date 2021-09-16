# Chess WebApp

## Development - Java App

Code location: `./app`

Install [**Gradle**](https://docs.gradle.org/current/userguide/installation.html)

Run application

```
gradle run
```

### Tests

run tests

```
gradle test
```

Output is minimal if tests succeed. Verbose if they don't. A pretty HTML test report is generated at `./build/reports/index.html`. Open in your browser after running test suite.

Test framework is [JUnit 5 (Jupiter)](https://junit.org/junit5/docs/current/user-guide/#writing-tests)

Add test files in `./app/src/test/java/chess/<path_to_class>/<Class>Test.java`; mirror directory structure of `./app/src/main/java/chess/<path_to_class><Class>.java`


