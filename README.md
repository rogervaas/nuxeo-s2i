
# S2I builder image for Nuxeo projects

## Getting started  

This builder image allows to run [Source to image](https://github.com/openshift/source-to-image) on a Maven Nuxeo project. 

Basically, given a [Nuxeo project on Github](https://github.com/nuxeo/nuxeo-sample-project), you can directly build a Docker image of your project with 

	s2i https://github.com/nuxeo/nuxeo-sample-project nuxeo/s2i sample-project
	docker run -ti -p 8080:8080 sample-project
	
## Environment variables

Some environment variables may be passe with the `-e` option to s2i to configure the build

| Variable Name     | default | Description                                      |
|-------------------|---------|--------------------------------------------------|
| `ARTIFACT_DIR`      | target  | define the directory to find JAR to be deployed in the bundle directory |
| `NUXEO_PACKAGE`     |         | define a package to be installed in the image (ex: `marketplace/target/mymarketplace-*.zip` |
| `NUXEO_SMOKE_TEST`  | false   | define if a smoke test is run at the end of the build  |

Additionnaly you can put a `.nuxeo-s2i` file in your project to set the value of those variables instead of using `-e` flag of `s2i`.


### Files and Directories  

| File                   |  Description                                                  |
|------------------------|---------------------------------------------------------------|
| Dockerfile             |  Defines the base builder image                               |
| s2i/bin/assemble       |  Script that builds the application                           |
| s2i/bin/usage          |  Script that prints the usage of the builder                  |
| s2i/bin/run            |  Script that runs the application                             |
| s2i/bin/save-artifacts |  Script for incremental builds that saves the built artifacts |
| test/run               |  Test script for the builder image                            |
| test/test-app          |  Test application source code                                 |


#### Create the builder image
The following command will create a builder image named nuxeo-s2i based on the Dockerfile that was created previously.

```
docker build -t nuxeo-s2i .
```

The builder image can also be created by using the *make* command since a *Makefile* is included.

Once the image has finished building, the command *s2i usage nuxeo-s2i* will print out the help info that was defined in the *usage* script.

#### Testing the builder image
The builder image can be tested using the following commands:

```
docker build -t nuxeo-s2i-candidate .
IMAGE_NAME=nuxeo-s2i-candidate test/run
```

The builder image can also be tested by using the *make test* command since a *Makefile* is included.

#### Creating the application image
The application image combines the builder image with your applications source code, which is served using whatever application is installed via the *Dockerfile*, compiled using the *assemble* script, and run using the *run* script.
The following command will create the application image:

```
s2i build test/test-app nuxeo-s2i nuxeo-s2i-app
---> Building and installing application from source...
```

#### Running the application image
Running the application image is as simple as invoking the docker run command:
```
docker run -d -p 8080:8080 nuxeo-s2i-app
```
The application, which consists of a simple static web page, should now be accessible at  [http://localhost:8080](http://localhost:8080).

#### Using the saved artifacts script
Rebuilding the application using the saved artifacts can be accomplished using the following command:
```
s2i build --incremental=true test/test-app nginx-centos7 nginx-app
---> Restoring build artifacts...
---> Building and installing application from source...
```

# Licensing

Most of the source code in the Nuxeo Platform is copyright Nuxeo and
contributors, and licensed under the Apache License, Version 2.0.

See [licenses](https://github.com/nuxeo/nuxeo/tree/master/licenses) and the documentation page [Licenses](http://doc.nuxeo.com/x/gIK7) for details.

# About Nuxeo

Nuxeo dramatically improves how content-based applications are built, managed and deployed, making customers more agile, innovative and successful. Nuxeo provides a next generation, enterprise ready platform for building traditional and cutting-edge content oriented applications. Combining a powerful application development environment with SaaS-based tools and a modular architecture, the Nuxeo Platform and Products provide clear business value to some of the most recognizable brands including Verizon, Electronic Arts, Sharp, FICO, the U.S. Navy, and Boeing. Nuxeo is headquartered in New York and Paris. More information is available at [www.nuxeo.com](http://www.nuxeo.com).


