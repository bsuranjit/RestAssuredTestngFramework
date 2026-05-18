FROM maven:3.8.6-openjdk-11
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve -q
COPY src ./src
CMD ["mvn", "clean", "test", "-DBASE_URI=https://api.spotify.com", "-DACCOUNT_BASE_URI=https://accounts.spotify.com"]
