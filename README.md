# Corona-Warn-App Verification Portal
<p align="center">
    <a href="https://github.com/corona-warn-app/cwa-verification-portal/commits/" title="Last Commit"><img src="https://img.shields.io/github/last-commit/corona-warn-app/cwa-portal-server?style=flat"></a>
    <a href="https://github.com/corona-warn-app/cwa-verification-portal/issues" title="Open Issues"><img src="https://img.shields.io/github/issues/corona-warn-app/cwa-verification-server?style=flat"></a>
    <a href="https://github.com/corona-warn-app/cwa-verification-portal/blob/master/LICENSE" title="License"><img src="https://img.shields.io/badge/License-Apache%202.0-green.svg?style=flat"></a>
</p>
<p align="center">
  <a href="#development">Development</a> •
  <a href="#documentation">Documentation</a> •
  <a href="#support-and-feedback">Support</a> •
  <a href="#how-to-contribute">Contribute</a> •
  <a href="#contributors">Contributors</a> •
  <a href="#repositories">Repositories</a> •
  <a href="#licensing">Licensing</a>
</p>
The goal of this project is to develop the official Corona-Warn-App for Germany based on the exposure notification API from [Apple](https://www.apple.com/covid19/contacttracing/) and [Google](https://www.google.com/covid19/exposurenotifications/). The apps (for both iOS and Android) use Bluetooth technology to exchange anonymous encrypted data with other mobile phones (on which the app is also installed) in the vicinity of an app user's phone. The data is stored locally on each user's device, preventing authorities or other parties from accessing or controlling the data. This repository contains the **verification service** for the Corona-Warn-App. This implementation is still a **work in progress**, and the code it contains is currently alpha-quality code.  

## Status

**TBD: Sonarcloud Shields**


## About this component
For all People who are willing to join the tracing process, but have not been tested by one of the participating labors there will be a serviceline one can call to get a temporaly transactionnumber (TAN) to join the process. The agents at the serviceline will tell the caller some questions to verify his status and then provide one with the transactionnumber.
This component provides a simple user interface for the servicelines agents to generate the temporaly TAN. For security reasons it comes along with an wide spread IAM (**I**dentity**A**cess**M**anagement) which uses 2-Factor-Authentification for User Authentification, which will be hosted in another [project](https://github.com/corona-warn-app/cwa-verification-iam) in the corona-warn-app org.

## Development

This component can be locally build in order to test the functionality of the interfaces and verify the concepts it is build upon. 
There are two ways to build:

 - [Maven](https:///maven.apache.org) build - to run this component as spring application on your local machine
 - [Docker](https://www.docker.com) build - to run it as docker container build from the provided docker build [file](https://github.com/corona-warn-app/cwa-verification-server/blob/master/Dockerfile)
 ### Prerequisites
 [Open JDK 11](https://openjdk.java.net)  
 [Maven](https://apache.maven.org)  
 *(optional)*: [Docker](https://www.docker.com)  

### Build
 Whether you cloned or downloaded the 'zipped' sources you will either find the sources in the chosen checkout-directory or get a zip file with the source code, which you can expand to a folder of your choice.

 In either case open a terminal pointing to the directory you put the sources in. The local build process is described afterwards depending on the way you choose.
#### Maven based build
For actively take part on the development this is the way you should choose.   
Please check, whether following prerequisites are fulfilled
- [Open JDK 11](https://openjdk.java.net) or a similar JDK 11 compatible VM  
- [Maven](https://apache.maven.org)  

is installed on your machine.  
You can then open a terminal pointing to the root directory of the verification server and do the following:

    mvn package
    java -jar target/cwa-verification-portal-0.0.1-SNAPSHOT.jar  

The verificationportal will start up and run locally on your machine available on port 8080.
Please keep in mind, that you need another component [cwa-verification-iam] the get this running in a sensable manner.

#### Docker based build  
We recommend that you first check the prerequisites to ensure that  
- [Docker](https://www.docker.com)  

is installed on you machine  

On the commandline do the following:
```bash
docker build -f|--file <path to dockerfile>  -t <imagename>  <path-to-verificationportalserver-root>
docker run -p 127.0.0.1:8080:8080/tcp -it <imagename>
```
or simply  
```bash
docker build --pull --rm -f "Dockerfile" -t cwa-verificationportal "."
docker run -p 127.0.0.1:8080:8080/tcp -it cwa-verificationportal
```
if you are in the root of the checked out repository.  
The docker image will then run on your local machine on port 8080 assuming you configured docker for shared network mode.
## Code of Conduct

This project has adopted the [Contributor Covenant](https://www.contributor-covenant.org/) in version 2.0 as our code of conduct. Please see the details in our [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md). All contributors must abide by the code of conduct.

## Working Language

We are building this application for Germany. We want to be as open and transparent as possible, also to interested parties in the global developer community who do not speak German. Later on this application might also serve as a template for other projects outside of Germany. For these reasons, we decided to apply _English_ as the primary project language.  

Consequently, all content will be made available primarily in English. We also ask all interested people to use English as language to create issues, in their code (comments, documentation etc.) and when you send requests to us. The application itself, documentation and all end-user facing content will - of course - be made available in German (and probably other languages as well). We also try to make some developer documentation available in German, but please understand that focussing on the _Lingua Franca_ of the global developer community makes the development of this application as efficient as possible.

## Documentation

Documents describing this component and its underlying process can be found under the [documentation subdir](docs)

## Support and Feedback
The following channels are available for discussions, feedback, and support requests:

| Type                     | Channel                                                |
| ------------------------ | ------------------------------------------------------ |
| **General Discussion**   | <a href="https://github.com/corona-warn-app/cwa-verification-server/issues/new/choose" title="General Discussion"><img src="https://img.shields.io/github/issues/corona-warn-app/cwa-verification-server/question.svg?style=flat-square"></a> </a>   |
| **Concept Feedback**    | <a href="https://github.com/corona-warn-app/cwa-verification-server/issues/new/choose" title="Open Concept Feedback"><img src="https://img.shields.io/github/issues/corona-warn-app/cwa-verification-server/architecture.svg?style=flat-square"></a>  |
| **Verification Server Issue**    | <a href="https://github.com/corona-warn-app/cwa-verification-server/issues" title="Open Issues"><img src="https://img.shields.io/github/issues/corona-warn-app/cwa-verification-server?style=flat"></a>  |
| **Other Requests**    | <a href="mailto:opensource@telekom.de" title="Email CWA Team"><img src="https://img.shields.io/badge/email-CWA%20team-green?logo=mail.ru&style=flat-square&logoColor=white"></a> 

## How to contribute

Please see our [CONTRIBUTING.md](CONTRIBUTING.md) for details on how to contribute, our team setup, the project structure and additional details which you need to know to work with us.

## Contributors

The German government has asked SAP AG and Deutsche Telekom AG to develop the Corona-Warn-App for Germany as open source software. Deutsche Telekom is providing the network and mobile technology and will operate and run the backend for the app in a safe, scalable and stable manner. SAP is responsible for the app development, its framework and the underlying platform. Therefore, development teams of SAP and Deutsche Telekom are contributing to this project. At the same time our commitment to open source means that we are enabling -in fact encouraging- all interested parties to contribute and become part of its developer community.

## Repositories

The following public repositories are currently available for the Corona-Warn-App:

| Repository          | Description                                                           |
| ------------------- | --------------------------------------------------------------------- |
| [cwa-documentation] | Project overview, general documentation, and white papers             |
| [cwa-server]        | Backend implementation for the Apple/Google exposure notification API |
| [cwa-verification-server] | Backend implementation of the verification process|
| [cwa-verification-portal] | Portal for the Hotline to manually verify COVID-19 infected people|
[cwa-documentation]: (https://github.com/corona-warn-app/cwa-documentation)
[cwa-server]: (https://github.com/corona-warn-app/cwa-server)
[cwa-verification-server]: (https://github.com/corona-warn-app/cwa-verification-server)
[cwa-verification-portal]: (https://github.com/corona-warn-app/cwa-verification-portal)

## Licensing

Copyright (c) 2020 Deutsche Telekom AG.

Licensed under the **Apache License, Version 2.0** (the "License"); you may not use this file except in compliance with the License.

You may obtain a copy of the License at https://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the [LICENSE](./LICENSE) for the specific language governing permissions and limitations under the License.
