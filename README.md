Converted project notes
-----------------------

What I changed:
- Replaced SQL Server settings with MySQL in src/main/resources/application.properties.
- Replaced mssql-jdbc dependency with mysql-connector-j in pom.xml.
- Updated Hibernate dialect to MySQL8Dialect.
- Created minimal Java classes (entity, repository, service, controller, main app).
- Added simple Thymeleaf templates and static assets.
- Created a basic SecurityConfig (note: spring-boot-starter-security was NOT added to pom; if you want security enabled, add the dependency).

Defaults:
- MySQL username/password in application.properties: root / rusiru_12345
  -> Change these to match your local MySQL setup.
- Database name: pharmacydb (the app will create tables automatically if the DB exists and the user has rights).

How to run:
1. Ensure you have MySQL running and a database named 'pharmacydb'. Example:
   - mysql -u root -p
   - CREATE DATABASE pharmacydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
2. Adjust spring.datasource.username and spring.datasource.password in application.properties if needed.
3. Build with: mvn clean package
4. Run with: java -jar target/medicalsystem-0.0.1-SNAPSHOT.jar
5. Open http://localhost:8081/

Files included: full project skeleton + converted pom and properties.

If you'd like:
- I can add spring-boot-starter-security and a default user.
- I can keep your original SQL Server configuration as a profile.
- I can add more controllers, DTOs, or the original HTML files if you provide them.
