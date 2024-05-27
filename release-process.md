# Release Process

This guide provides a chronological steps which goes through release tagging, staging, verification and publishing.


## Check the SNAPSHOT builds and pass the tests

Check that the project builds in java 8 and java 11.

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/  mvn clean package install verify
JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/ mvn clean package install verify
```

## Check Android compatibility

We are using gradle and docker

for main branch
```bash
docker run -it mobiledevops/android-sdk-image:34.0.0-jdk17 /bin/bash
git clone https://github.com/albfernandez/juniversalchardet.git
cd juniversalchardet
gradle check
```

for older branches
```bash
docker run -it mobiledevops/android-sdk-image:34.0.0-jdk17 /bin/bash
git clone https://github.com/albfernandez/juniversalchardet.git
cd juniversalchardet
git checkout 2.5.x
rm src/main/java/module-info.java
gradle check
```


## Set version and build 

```bash
# change release in pom.xml and README.md
mvn clean package install verify -Pdeploy
mvn clean package install verify deploy -Pdeploy
git add -A
git commit -S -m 'Release <v1.0.0>'
git tag -a <v1.0.0> -m "Tagging release <1.0.0>"
git push
git push --tags
```


## Create release and upload artifacts to Github

Manually creating the release in Github project page, and upload generated artifacts.



## Prepare next iteration

```bash
# change release in pom.xml
git add -A
git commit -S -m 'Next release cycle'
git push
```