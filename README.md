# Small continous integration CI server
### Siyao Liu, Edwin So, Filip Bäck, Tobias Gabi Goobar
#### *Assignment 2 in course DD2480, Spring 2022, KTH*

## Overview
This project is a basic CI-server written in Java. The server is configured for Github-webhooks,
to automatically test a repository each time a new push event occurs.

## Functionality 
The CI-server works by listening to a request from the **GitHub Webhook** which is triggered when a push is made. The CI-server then clones, builds and tests the repo using **Gradle**. Finally an email is sent with information about the results such as an indication of success and, in case of failure, the error output.

Using `git push` triggers the **GitHub Webhook** which sends a `HTTP POST` request to our CI-server that recieves the request and parses information such as 
- URL of repo
- Reference
- Commit hash
- The name of the user who performed the push
- The email of the user who performed the push

With this information the CI-server then
1. clones the repo (`git clone URL`)
2. checks out the branch (`git checkout BRANCH`)
3. performs `gradle check`
4. performs `gradle build`
5. performs `gradle test`

Finally, the pusher is notified by email if the job succeeded or not and in the case where it fails the error output is included in the email.

## How to use

- install gradle and java
- set env vars and export the following
    - GITHUB_USERNAME
    - GITHUB_REPOSTATUSURL
    - GITHUB_TOKEN
- create dir `.ci` for build state storing
- run `gradle run`

## How to test

- install gradle and java
- run `gradle test`

## Team
Currently, we've identified ourselves being in the state of 'Performance'. This means that the team is meeting its commitments,
continuously adapting to changing contexts, as well as identifying and addressing problems without outside help. The work
is efficient with minimal rework, and because of clear and recurring meetings we are avoiding unnecessary rework/wasted work.
Everyone on the team is taking responsibility for their own work and making sure that internal deadlines are met. If any team member
is stuck on any particular issue, the team is notified and a solution is created together. 

## Contributions

#### Siyao Liu

Implemented the WebHookHandler class which parses the http request containing the push event to output the job for the CI-server to hand over to JobRunner, and sets the commit status in order to support the notification of CI results. Also discussed the Team section with Philip.

#### Edwin So

Implemented code skeleton (karmosin), fix JobRunner's bug, implemented Server, Main, ContinuousIntegrationHandler. Setup gradle. Designed the fundamental archtecture of the system. 

#### Filip Bäck
Implemented the ResultEmailer class which includes a Mailgun API endpoint which sends e-mail with
relevant build information once a push event occurs.

#### Tobias Gabi Goobar

Implemented the JobRunner class which handles cloning, checking, building and testing of the repo along with unittests. Also wrote the "Functionality"-part of the README
