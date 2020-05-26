<h1 align="center">
    Corona-Warn-App Verification Portal
</h1>

<p align="center">
    <a href="https://github.com/corona-warn-app/cwa-verification-portal/commits/" title="Last Commit"><img src="https://img.shields.io/github/last-commit/corona-warn-app/cwa-verification-portal?style=flat"></a>
    <a href="https://github.com/corona-warn-app/cwa-verification-portal/issues" title="Open Issues"><img src="https://img.shields.io/github/issues/corona-warn-app/cwa-verification-portal?style=flat"></a>
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

The goal of this project is to develop the official Corona-Warn-App for Germany based on the exposure notification API from [Apple](https://www.apple.com/covid19/contacttracing/) and [Google](https://www.google.com/covid19/exposurenotifications/). The apps (for both iOS and Android) use Bluetooth technology to exchange anonymous encrypted data with other mobile phones (on which the app is also installed) in the vicinity of an app user's phone. The data is stored locally on each user's device, preventing authorities or other parties from accessing or controlling the data. This repository contains the **verification portal** for the Corona-Warn-App. This implementation is still a **work in progress**, and the code it contains is currently alpha-quality code.

## Status
![ci](https://github.com/corona-warn-app/cwa-verification-portal/workflows/ci/badge.svg)
[![quality gate](https://sonarcloud.io/api/project_badges/measure?project=corona-warn-app_cwa-verification-portal&metric=alert_status)](https://sonarcloud.io/dashboard?id=corona-warn-app_cwa-verification-portal)
[![coverage](https://sonarcloud.io/api/project_badges/measure?project=corona-warn-app_cwa-verification-portal&metric=coverage)](https://sonarcloud.io/dashboard?id=corona-warn-app_cwa-verification-portal)
[![bugs](https://sonarcloud.io/api/project_badges/measure?project=corona-warn-app_cwa-verification-portal&metric=bugs)](https://sonarcloud.io/dashboard?id=corona-warn-app_cwa-verification-portal)

## Architecture Overview
Y

## Development




## Documentation

The full documentation for the Corona-Warn-App can be found in the [cwa-documentation](https://github.com/corona-warn-app/cwa-documentation) repository. The documentation repository contains technical documents, architecture information, and white papers related to this implementation.

## Support and Feedback
The following channels are available for discussions, feedback, and support requests:

| Type                     | Channel                                                |
| ------------------------ | ------------------------------------------------------ |
| **General Discussion**   | <a href="https://github.com/corona-warn-app/cwa-verification-portal/issues/new/choose" title="General Discussion"><img src="https://img.shields.io/github/issues/corona-warn-app/cwa-verification-portal/question.svg?style=flat-square"></a> </a>   |
| **Concept Feedback**    | <a href="https://github.com/corona-warn-app/cwa-verification-portal/issues/new/choose" title="Open Concept Feedback"><img src="https://img.shields.io/github/issues/corona-warn-app/cwa-verification-portal/architecture.svg?style=flat-square"></a>  |
| **Verification Server Issue**    | <a href="https://github.com/corona-warn-app/cwa-verification-portal/issues" title="Open Issues"><img src="https://img.shields.io/github/issues/corona-warn-app/cwa-verification-portal?style=flat"></a>  |
| **Other Requests**    | <a href="mailto:opensource@telekom.de" title="Email CWA Team"><img src="https://img.shields.io/badge/email-CWA%20team-green?logo=mail.ru&style=flat-square&logoColor=white"></a>   |

## How to Contribute

Contribution and feedback is encouraged and always welcome. For more information about how to contribute, the project structure, as well as additional contribution information, see our [Contribution Guidelines](./CONTRIBUTING.md). By participating in this project, you agree to abide by its [Code of Conduct](./CODE_OF_CONDUCT.md) at all times.

## Contributors

The German government has asked SAP AG and Deutsche Telekom AG to develop the Corona-Warn-App for Germany as open source software. Deutsche Telekom is providing the network and mobile technology and will operate and run the backend for the app in a safe, scalable and stable manner. SAP is responsible for the app development, its framework and the underlying platform. Therefore, development teams of SAP and Deutsche Telekom are contributing to this project. At the same time our commitment to open source means that we are enabling -in fact encouraging- all interested parties to contribute and become part of its developer community.

## Repositories

The following public repositories are currently available for the Corona-Warn-App:

| Repository          | Description                                                           |
| ------------------- | --------------------------------------------------------------------- |
| [cwa-documentation] | Project overview, general documentation, and white papers             |
| [cwa-server]        | Backend implementation for the Apple/Google exposure notification API |
| [cwa-verification-server] | Backend implementation of the verification process|
| [cwa-verification-portal] | The portal to interact with the verification server |

[cwa-documentation]: https://github.com/corona-warn-app/cwa-documentation
[cwa-server]: https://github.com/corona-warn-app/cwa-server
[cwa-verification-server]: https://github.com/corona-warn-app/cwa-verification-server
[cwa-verification-portal]: https://github.com/corona-warn-app/cwa-verification-Portal

## Licensing

Copyright (c) 2020 Deutsche Telekom AG.

Licensed under the **Apache License, Version 2.0** (the "License"); you may not use this file except in compliance with the License.

You may obtain a copy of the License at https://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the [LICENSE](./LICENSE) for the specific language governing permissions and limitations under the License.
